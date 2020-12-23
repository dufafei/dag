package com.dataflow.frame.plugin;

import com.dataflow.frame.plugin.interfaces.PluginFolderInterface;
import com.dataflow.frame.plugin.vfs2.VFS2;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

public class PluginFolder implements PluginFolderInterface {

    private String folder;
    private boolean searchLibDir;

    public PluginFolder(String folder, boolean searchLibDir) {
        this.folder = folder;
        this.searchLibDir = searchLibDir;
    }

    public PluginFolder(String folder) {
        this(folder, false);
    }

    @Override
    public String getFolder() {
        return folder;
    }

    @Override
    public FileObject[] findJarFiles() throws Exception {
        return findJarFiles(searchLibDir);
    }

    public FileObject[] findJarFiles(final boolean includeLibJars) throws Exception {
        try {
            // 在这个目录中查找所有的jar文件
            FileObject folderObject = VFS2.getFileObject(this.getFolder());
            return folderObject.findFiles(new FileSelector() {

                /**
                 * 判断这个目录是否应该被遍历，如果这个方法返回真的话，
                 * 对每个这个目录的子文件都会调用includeFile(FileSelectInfo info)方法，
                 * 并且每个子目录都递归遍历。对于一个目录，这个方法在includeFile方法调用之前调用。
                 */
                @Override
                public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
                    FileObject fileObject = fileSelectInfo.getFile();
                    String folder1 = fileObject.getName().getBaseName();
                    FileObject ignore = fileObject.getChild(".ignore");
                    return includeLibJars || ( ignore == null && !"lib".equals(folder1) );
                }

                /**
                 * 判断是否这个文件或者目录应该被选择，这个方法使用深度优先，首先检查子文件
                 */
                @Override
                public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
                    FileObject file = fileSelectInfo.getFile();
                    return file.isFile() && file.toString().endsWith(".jar");
                }
            } );
        } catch ( Exception e ) {
            throw new Exception("Unable to list jar files in plugin folder '" + toString() + "'", e);
        }
    }

    @Override
    public String toString() {
        return folder;
    }
}
