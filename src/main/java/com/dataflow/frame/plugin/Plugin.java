package com.dataflow.frame.plugin;

import com.dataflow.frame.plugin.interfaces.PluginInterface;

import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plugin implements PluginInterface {

    private String id;
    private String name;
    private String description;
    private String category;
    private String icon;
    private URLClassLoader urlClassLoader;
    private String className;
    private List<String> libraries;
    private boolean nativePlugin;
    private Map<String, String> extensionOptions = new HashMap<>();

    public Plugin(String id, String name, String description, String category,
                  URLClassLoader urlClassLoader, String icon, String className, List<String> libraries,
                  boolean nativePlugin, Map<String, String> extensionOptions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.urlClassLoader = urlClassLoader;
        this.icon = icon;
        this.className = className;
        this.libraries = libraries;
        this.nativePlugin = nativePlugin;
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
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }

    @Override
    public String getIcon() { return icon; }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public List<String> getLibraries() {
        return libraries;
    }

    @Override
    public boolean isNativePlugin() {
        return nativePlugin;
    }

    @Override
    public Map<String, String> getExtensionOptions() {
        return extensionOptions;
    }

    @Override
    public void addExtensionOption(String key, String value) {
        extensionOptions.put(key, value);
    }
}
