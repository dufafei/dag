package com.dataflow.frame.core.plugin;

import com.dataflow.frame.consts.Const;
import org.apache.commons.lang3.StringUtils;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PluginRegistry {

    // 插件类型
    private static List<PluginTypeInterface> pluginTypes = new ArrayList<>();
    // 插件类型-> 该类型的插件列表
    private Map<Class<? extends PluginTypeInterface>, List<PluginInterface>> pluginMap = new HashMap<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final PluginRegistry pluginRegistry = new PluginRegistry();
    private PluginRegistry() {};
    public static PluginRegistry getInstance() {
        return pluginRegistry;
    }

    public static synchronized void addPluginType(PluginTypeInterface type) {
        pluginTypes.add(type);
    }
    public List<PluginTypeInterface> getPluginType() {
        lock.readLock().lock();
        try {
            return pluginTypes;
        } finally {
            lock.readLock().unlock();
        }
    }

    public static synchronized void init() throws Exception {
        for (final PluginTypeInterface pluginType : pluginTypes) {
            getInstance().registerType(pluginType);
        }
    }

    private void registerType(PluginTypeInterface pluginType) throws Exception {
        registerType(pluginType.getClass());
        pluginType.searchPlugins();
    }

    private void registerType(Class<? extends PluginTypeInterface> pluginType) {
        lock.writeLock().lock();
        try {
            pluginMap.computeIfAbsent(pluginType, k -> new ArrayList<>());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void registerPlugin(Class<? extends PluginTypeInterface> pluginType, PluginInterface plugin) throws Exception {
        lock.writeLock().lock();
        try {
            if(plugin.getId() == null) {
                throw new Exception("Not a valid id specified in plugin :" + plugin);
            }
            List<PluginInterface> list = pluginMap.get(pluginType);
            list.add(plugin);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void unRegisterPlugin(Class<? extends PluginTypeInterface> pluginType, PluginInterface plugin) {
        lock.writeLock().lock();
        try {
            List<PluginInterface> list = pluginMap.get(pluginType);
            list.remove(plugin);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginInterface, K extends PluginTypeInterface> List<T> getPlugins(Class<K> type) {
        Set<T> set = new HashSet<>();
        lock.readLock().lock();
        try {
            for (Class<? extends PluginTypeInterface> pi : pluginMap.keySet()) {
                if (Const.classIsOrExtends(pi, type)) {
                    List<PluginInterface> mapList = pluginMap.get(pi);
                    if (mapList != null) {
                        for (PluginInterface p : mapList) {
                            T t = (T) p;
                            set.add(t);
                        }
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return new ArrayList<>(set);
    }

    public PluginInterface getPlugin(Class<? extends PluginTypeInterface> pluginType, String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        for (PluginInterface plugin : getPlugins(pluginType)) {
            if (plugin.getId().equals(id)) {
                return plugin;
            }
        }
        return null;
    }

    /**
     * classpath 下使用Class.forName
     * jar包下使用 urlClassLoader.loadClass
     */
    @SuppressWarnings("unchecked")
    public <T> T loadClass(PluginInterface plugin) throws Exception {
        String className = plugin.getClassName();
        URLClassLoader ucl = plugin.getUrlClassLoader();
        Class<? extends T> cl = (Class<? extends T>) ucl.loadClass(className);
        return cl.newInstance();
    }
}
