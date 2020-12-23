package com.dataflow.frame.plugin.interfaces;

import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

/**
 * 插件详情
 */
public interface PluginInterface {

    String getId();

    String getName();

    String getDescription();

    String getCategory();

    URLClassLoader getUrlClassLoader();

    String getIcon();

    String getClassName();

    List<String> getLibraries();

    Map<String, String> getExtensionOptions();

    void addExtensionOption(String key, String value);

    boolean isNativePlugin();
}
