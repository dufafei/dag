package com.dataflow.frame.core.env;

import com.google.common.util.concurrent.SettableFuture;
import com.dataflow.frame.core.plugin.PluginRegistry;
import com.dataflow.frame.core.plugin.PluginTypeInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Environment {

    private static List<PluginTypeInterface> pluginTypes = new ArrayList<>();
    private static AtomicReference<Object> initialized = new AtomicReference<>(null);

    private Environment() {}

    public static void addPluginType(PluginTypeInterface pluginTypeInterface) {
        pluginTypes.add(pluginTypeInterface);
    }

    @SuppressWarnings("unchecked")
    public static void init() throws Exception {
        SettableFuture ready;
        if (initialized.compareAndSet(null, ready = SettableFuture.create())) {
            for (PluginTypeInterface pluginType: pluginTypes) {
                PluginRegistry.addPluginType(pluginType);
            }
            PluginRegistry.init();
            ready.set(true);
        }
    }

}
