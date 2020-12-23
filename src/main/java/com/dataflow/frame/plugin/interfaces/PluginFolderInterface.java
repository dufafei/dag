package com.dataflow.frame.plugin.interfaces;

import org.apache.commons.vfs2.FileObject;

/**
 * 插件目录
 */
public interface PluginFolderInterface {

    public String getFolder();

    public FileObject[] findJarFiles() throws Exception;
}
