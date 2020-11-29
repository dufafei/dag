package org.mxgraph.flow.core.cflow.plugin;

import org.apache.commons.vfs2.FileObject;

public interface PluginFolderInterface {

    public String getFolder();

    public boolean isPluginXmlFolder();

    public boolean isPluginAnnotationsFolder();

    public FileObject[] findJarFiles() throws Exception;
}
