package com.dataflow.frame.core.plugin.folder;

import com.dataflow.frame.core.vfs2.FileVFS2;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

public class PluginFolder implements PluginFolderInterface {

    private String folder;

    public PluginFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public String getFolder() { return folder; }

    @Override
    public FileObject[] findJarFiles(boolean includeLibJars) throws Exception {
        try {
            // 在这个目录中查找所有的jar文件
            FileObject folderObject = FileVFS2.getFileObject(this.getFolder());
            return folderObject.findFiles(new FileSelector() {

                @Override
                public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
                    FileObject fileObject = fileSelectInfo.getFile();
                    FileObject ignore = fileObject.getChild(".ignore");
                    String folder = fileObject.getName().getBaseName();
                    return includeLibJars || ( ignore == null && !"lib".equals(folder) );
                }

                @Override
                public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
                    FileObject file = fileSelectInfo.getFile();
                    return file.isFile() && file.toString().endsWith(".jar");
                }

            });
        } catch (Exception e) {
            throw new Exception("Unable to list jar files in plugin folder '" + toString() + "'", e);
        }
    }

    @Override
    public String toString() {
        return folder;
    }
}
