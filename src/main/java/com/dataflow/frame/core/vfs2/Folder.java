package com.dataflow.frame.core.vfs2;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

public class Folder implements FolderInterface {

    private String folder;

    public Folder(String folder) {
        this.folder = folder;
    }

    @Override
    public String getFolder() { return folder; }

    @Override
    public FileObject[] findJarFiles(boolean includeLibJars) throws Exception {
        try {
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
            throw new Exception("Unable to list jar files in folder '" + toString() + "'", e);
        }
    }

    @Override
    public String toString() {
        return folder;
    }
}
