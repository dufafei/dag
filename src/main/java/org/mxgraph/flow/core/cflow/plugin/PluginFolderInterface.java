package org.mxgraph.flow.core.cflow.plugin;

import org.apache.commons.vfs2.FileObject;

public interface PluginFolderInterface {

    public String getFolder();

    public FileObject[] findJarFiles() throws Exception;
}
