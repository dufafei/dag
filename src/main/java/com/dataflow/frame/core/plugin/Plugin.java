package com.dataflow.frame.core.plugin;

import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

public class Plugin implements PluginInterface {

    private String id;
    private String name;
    private String description;
    private String icon;
    private String category;
    private String className;
    private List<String> libraries;
    private URLClassLoader urlClassLoader;
    private Map<String, String> extensionOptions;

    public Plugin(String id, String name, String description,String icon, String category,
                  String className, List<String> libraries, URLClassLoader urlClassLoader,
                  Map<String, String> extensionOptions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.category = category;
        this.className = className;
        this.libraries = libraries;
        this.urlClassLoader = urlClassLoader;
        this.extensionOptions = extensionOptions;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() { return description; }

    @Override
    public String getIcon() { return icon; }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public List<String> getLibraries() { return libraries; }

    @Override
    public URLClassLoader getUrlClassLoader() { return urlClassLoader; }

    @Override
    public Map<String, String> getExtensionOptions() {
        return extensionOptions;
    }

    @Override
    public void addExtensionOption(String key, String value) {
        extensionOptions.put(key, value);
    }
}
