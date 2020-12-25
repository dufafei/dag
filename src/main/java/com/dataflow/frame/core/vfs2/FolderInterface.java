package com.dataflow.frame.core.vfs2;

import org.apache.commons.vfs2.FileObject;

public interface FolderInterface {

    /**
     *
     * @return 目录名称
     */
    public String getFolder();

    /**
     *
     * 搜索指定目录下的jar包
     * @return FileObject[]
     */
    public FileObject[] findJarFiles(final boolean includeLibJars) throws Exception;
}
