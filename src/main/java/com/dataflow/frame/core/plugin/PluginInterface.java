package com.dataflow.frame.core.plugin;

import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

/**
 * 插件接口
 */
public interface PluginInterface {

    String getId();

    String getName();

    String getDescription();

    String getIcon();

    String getCategory();

    String getClassName();

    List<String> getLibraries();

    URLClassLoader getUrlClassLoader();

    Map<String, Object> getExtensionOptions();
}
