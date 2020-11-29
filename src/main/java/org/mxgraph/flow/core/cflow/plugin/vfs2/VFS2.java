package org.mxgraph.flow.core.cflow.plugin.vfs2;

import org.mxgraph.flow.core.cflow.Const;
import org.mxgraph.flow.core.cflow.env.variable.VariableSpace;
import org.mxgraph.flow.core.cflow.env.variable.Variables;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.cache.WeakRefFilesCache;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileObject;
import java.io.File;

/**
 * VFS把对各种各样的的文件系统的访问封装成统一的应用程序接口
 * FTP
 * Local Files
 * HTTP and HTTPS
 * SFTP
 * Temporary Files
 * Zip, Jar and Tar (uncompressed, tgz or tbz2)
 * gzip and bzip2
 * res
 * ram
 * 这些文件系统中的文件被封装成了FileObject这个类，文件的读写操作通过此类来操作。文件的路径采用了URL的方式。
 */
public class VFS2 {

    private static final VFS2 vfs2 = new VFS2();
    private static VariableSpace defaultVariableSpace;
    private final DefaultFileSystemManager fsm;

    private VFS2() {
        defaultVariableSpace = new Variables(); // 创建一个新的变量空间
        fsm = new StandardFileSystemManager();
        try {
            fsm.setFilesCache(new WeakRefFilesCache()); //
            fsm.init();  //
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                fsm.close();
            } catch ( Exception ignored ) {
                // Exceptions can be thrown due to a closed classloader
            }
        }));
    }

    public static VFS2 getInstance() {
        return vfs2;
    }

    public FileSystemManager getFileSystemManager() {
        return fsm;
    }


    public static FileObject getFileObject(String vfsFilename) throws Exception {
        return getFileObject(vfsFilename, defaultVariableSpace);
    }

    public static FileObject getFileObject(String vfsFilename, VariableSpace space) throws Exception {
        return getFileObject(vfsFilename, space, null);
    }

    /**
     * 文件名是否以一个已知的协议开始，如:file: zip: ram: smb: jar:等等。
     * 如果不是，我们将他看做一个目录。
     */
    public static FileObject getFileObject(String vfsFilename, VariableSpace space, FileSystemOptions fsOptions) throws Exception {
        FileSystemManager fsManager = getInstance().getFileSystemManager();
        boolean relativeFilename = true;
        String[] schemes = fsManager.getSchemes();
        for (int i = 0; i < schemes.length && relativeFilename; i++) {
            if (vfsFilename.startsWith(schemes[i] + ":")) {
                relativeFilename = false;
                // We have a VFS URL, load any options for the file system driver
                // fsOptions = buildFsOptions(space, fsOptions, vfsFilename, schemes[i]);
            }
        }
        String filename;
        if (vfsFilename.startsWith("\\\\")) {
            File file = new File(vfsFilename);
            filename = file.toURI().toString();
        } else {
            if (relativeFilename) {
                File file = new File(vfsFilename);
                // 返回的是相对路径，但不会处理“.”和“..”的情况
                filename = file.getAbsolutePath();
            } else {
                filename = vfsFilename;
            }
        }
        FileObject fileObject = null;
        if (fsOptions != null) {
            fileObject = fsManager.resolveFile(filename, fsOptions);
        } else {
            fileObject = fsManager.resolveFile(filename);
        }
        if (fileObject instanceof SftpFileObject) {
            /*fileObject = new SftpFileObjectWithWindowsSupport((SftpFileObject) fileObject,
                    SftpFileSystemWindowsProvider.getSftpFileSystemWindows( (SftpFileObject) fileObject ) );*/
        }
        return fileObject;
    }

    public static String getFilename( FileObject fileObject ) {
        FileName fileName = fileObject.getName();
        String root = fileName.getRootURI();
        if (!root.startsWith("file:")) {
            return fileName.getURI(); // nothing we can do about non-normal files.
        }
        if (root.startsWith( "file:////")) {
            return fileName.getURI(); // we'll see 4 forward slashes for a windows/smb network share
        }
        if (root.endsWith( ":/" )) { // Windows
            root = root.substring( 8, 10 );
        } else { // *nix & OSX
            root = "";
        }
        String fileString = root + fileName.getPath();
        if ( !"/".equals(Const.FILE_SEPARATOR)) {
            fileString = Const.replace(fileString, "/", Const.FILE_SEPARATOR);
        }
        return fileString;
    }
}
