package com.dataflow.frame.core.vfs2;

import org.apache.commons.vfs2.FileObject;

public interface FolderInterface {

    String getFolder();

    FileObject[] findJarFiles(final boolean includeLibJars) throws Exception;
}
