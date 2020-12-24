package com.dataflow.frame.core.vfs2;

import com.dataflow.frame.consts.Const;
import com.dataflow.frame.core.env.Variable;
import com.dataflow.frame.core.env.VariableSpace;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.cache.WeakRefFilesCache;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
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
 * 这些文件系统中的文件被封装成了FileObject这个类，文件的读写操作通过此类来操作。
 * 文件的路径采用了URL的方式。
 */
public class FileVFS2 {

    private static final FileVFS2 FILE_VFS_2 = new FileVFS2();
    private static Variable defaultVariable;
    private static DefaultFileSystemManager fsm;

    private FileVFS2() {
        defaultVariable = new VariableSpace();
        fsm = new StandardFileSystemManager();
        try {
            fsm.setFilesCache(new WeakRefFilesCache());
            fsm.init();
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                fsm.close();
            } catch (Exception ignored) {
                // Exceptions can be thrown due to a closed classloader
            }
        }));
    }

    public static FileVFS2 getInstance() {
        return FILE_VFS_2;
    }

    public static FileSystemManager getFileSystemManager() {
        return fsm;
    }

    public static FileObject getFileObject(String vfsFilename) throws Exception {
        return getFileObject(vfsFilename, defaultVariable);
    }

    public static FileObject getFileObject(String vfsFilename, Variable space) throws Exception {
        return getFileObject(vfsFilename, space, null);
    }

    public static FileObject getFileObject(String vfsFilename,
                                           Variable space,
                                           FileSystemOptions fsOptions) throws Exception {
        FileSystemManager fsManager = getFileSystemManager();
        boolean relativeFilename = true;
        String[] schemes = fsManager.getSchemes();
        for (int i = 0; i < schemes.length && relativeFilename; i++) {
            if (vfsFilename.startsWith(schemes[i] + ":")) {
                relativeFilename = false;
                // 为文件系统驱动加载配置选项
                fsOptions = FileSystemConfigFactory.buildFsOptions(space, fsOptions, vfsFilename, schemes[i]);
            }
        }
        // 处理文件路径
        String filename;
        if (vfsFilename.startsWith("\\\\")) {
            File file = new File(vfsFilename);
            filename = file.toURI().toString();
        } else {
            if (relativeFilename) {
                File file = new File(vfsFilename);
                filename = file.getAbsolutePath();
            } else {
                filename = vfsFilename;
            }
        }
        // 获取FileObject
        FileObject fileObject;
        if (fsOptions != null) {
            fileObject = fsManager.resolveFile(filename, fsOptions);
        } else {
            fileObject = fsManager.resolveFile(filename);
        }
        return fileObject;
    }

    /**
     * Windows:
     * 处理前：file:///E:/
     * 处理后：E:
     */
    public static String getFilename(FileObject fileObject) {
        FileName fileName = fileObject.getName();
        String root = fileName.getRootURI();
        if (!root.startsWith("file:")) {
            // nothing we can do about non-normal files.
            return fileName.getURI();
        }
        if (root.startsWith("file:////")) {
            // we'll see 4 forward slashes for a windows/smb network share
            return fileName.getURI();
        }
        if (root.endsWith(":/")) {
            // Windows
            root = root.substring(8, 10);
        } else {
            // *nix & OSX
            root = "";
        }
        String fileString = root + fileName.getPath();
        if ( !"/".equals(Const.FILE_SEPARATOR)) {
            fileString = Const.replace(fileString, "/", Const.FILE_SEPARATOR);
        }
        return fileString;
    }
}
