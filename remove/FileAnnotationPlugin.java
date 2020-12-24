package com.dataflow.frame.core.vfs2;

import java.net.URL;

public class FileAnnotationPlugin {

    private URL pluginFolder; // 插件目录
    private URL pluginFile; // 插件jar
    private String className; // 主类

    public FileAnnotationPlugin(URL pluginFolder, URL pluginFile, String className) {
        this.pluginFolder = pluginFolder;
        this.pluginFile = pluginFile;
        this.className = className;
    }

    public URL getPluginFolder() { return pluginFolder; }
    public void setPluginFolder(URL pluginFolder) { this.pluginFolder = pluginFolder; }
    public URL getPluginFile() { return pluginFile; }
    public void setPluginFile(URL pluginFile) { this.pluginFile = pluginFile; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    @Override
    public String toString() { return pluginFile.toString(); }
}
