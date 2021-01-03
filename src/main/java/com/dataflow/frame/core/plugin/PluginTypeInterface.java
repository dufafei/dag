package com.dataflow.frame.core.plugin;

import com.dataflow.frame.core.vfs2.FolderInterface;
import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.List;

public interface PluginTypeInterface {

    String getId();

    String getName();

    Class<? extends Annotation> getPluginType();

    List<FolderInterface> getPluginFolders();

    void searchPlugins() throws Exception;

    void handlePluginAnnotation(Class<?> clazz,
                                boolean nativePlugin,
                                List<String> libraries,
                                URLClassLoader classLoader) throws Exception;
}
