package com.dataflow.frame.core.plugin.folder;

import org.apache.commons.vfs2.FileObject;

/**
 * 插件目录
 */
public interface PluginFolderInterface {

    /**
     *
     * @return 插件目录名称
     */
    public String getFolder();

    /**
     *
     * 搜索插件目录下的jar包
     * @return FileObject[]
     */
    public FileObject[] findJarFiles(final boolean includeLibJars) throws Exception;
}
