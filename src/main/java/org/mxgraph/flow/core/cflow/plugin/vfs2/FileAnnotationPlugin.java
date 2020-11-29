package org.mxgraph.flow.core.cflow.plugin.vfs2;

import java.net.URL;

/**
 * 封装jar里面的注解信息
 */
public class FileAnnotationPlugin {

    private URL pluginFolder;
    private URL jarFile;
    private String className;

    public FileAnnotationPlugin(URL pluginFolder, URL jarFile, String className) {
        this.pluginFolder = pluginFolder;
        this.jarFile = jarFile;
        this.className = className;
    }

    public URL getPluginFolder() { return pluginFolder; }
    public void setPluginFolder(URL pluginFolder) { this.pluginFolder = pluginFolder; }
    public URL getJarFile() { return jarFile; }
    public void setJarFile(URL jarFile) { this.jarFile = jarFile; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    @Override
    public String toString() { return jarFile.toString(); }
}
