package org.mxgraph.flow.core.cflow.plugin;

import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

public interface PluginInterface {

    Class<? extends PluginTypeInterface> getPluginType();

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

    public boolean isNativePlugin();
}
