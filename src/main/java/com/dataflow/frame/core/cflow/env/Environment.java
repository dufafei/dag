package com.dataflow.frame.core.cflow.env;

import com.google.common.util.concurrent.SettableFuture;
import com.dataflow.frame.plugin.registry.PluginRegistry;
import com.dataflow.frame.plugin.interfaces.PluginTypeInterface;

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
