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

    // 是否是本地插件
    boolean isNative();

    List<String> getLibraries();

    URLClassLoader getUrlClassLoader();

    Map<String, Object> getExtensionOptions();
}
