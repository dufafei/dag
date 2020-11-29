package org.mxgraph.flow.core.cflow.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 插件类型定义的接口
 */
public interface PluginTypeInterface {

    /**
     * 获取插件类型的ID
     */
    String getId();

    /**
     * 获取插件类型的名称
     */
    String getName();

    /**
     * 获取此类插件存放的目录
     */
    List<PluginFolderInterface> getPluginFolders();

    /**
     * 搜索此类型下的插件
     */
    void searchPlugins() throws Exception;

    /**
     * 处理此类型下的注解插件
     */
    void handlePluginAnnotation(Class<?> clazz,
                                URLClassLoader urlClassLoader,
                                java.lang.annotation.Annotation annotation,
                                List<String> libraries,
                                boolean nativePlugin,
                                URL pluginFolder) throws Exception;
}
