package com.dataflow.frame.core.plugin;

import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

/**
 * 插件详情
 */
public interface PluginInterface {

    /**
     *
     * @return 插件ID
     */
    String getId();

    /**
     *
     * @return 插件名称
     */
    String getName();

    /**
     *
     * @return 插件描述
     */
    String getDescription();

    /**
     *
     * @return 插件图片
     */
    String getIcon();

    /**
     *
     * @return 插件分类
     */
    String getCategory();

    /**
     *
     * @return 插件主类
     */
    String getClassName();

    /**
     *
     * @return 插件依赖
     */
    List<String> getLibraries();

    /**
     *
     * @return 插件加载器
     */
    URLClassLoader getUrlClassLoader();

    /**
     *
     * @return 插件扩展信息
     */
    Map<String, Object> getExtensionOptions();
}
