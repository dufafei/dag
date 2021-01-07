package com.dataflow.frame.core.plugin;

import com.dataflow.frame.core.consts.Const;
import org.apache.commons.lang3.StringUtils;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PluginRegistry {

    // 插件类型
    private static List<PluginTypeInterface> pluginTypes;
    // 插件类型-> 该类型的插件组件
    private Map<Class<? extends PluginTypeInterface>, List<PluginInterface>> pluginMap;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final PluginRegistry pluginRegistry = new PluginRegistry();
    private PluginRegistry() {
        pluginTypes =  new ArrayList<>();
        pluginMap = new HashMap<>();
    }
    public static PluginRegistry getInstance() {
        return pluginRegistry;
    }

    public static synchronized void addPluginType(PluginTypeInterface type) {
        pluginTypes.add(type);
    }

    public List<PluginTypeInterface> getPluginTypes() {
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

    public String getPluginId(Class<? extends PluginTypeInterface> pluginType, Object pluginClass) {
        String className = pluginClass.getClass().getName();
        for (PluginInterface plugin : getPlugins(pluginType)) {
            /*for (String check : plugin.getClassMap().values()) {
                if (check != null && check.equals(className)) {
                    return plugin.getIds()[0];
                }
            }*/
            if(plugin.getClassName().equals(className)) {
                return plugin.getId();
            }
        }
        return null;
    }

    public PluginInterface getPlugin( Class<? extends PluginTypeInterface> pluginType, Object pluginClass ) {
        String pluginId = getPluginId( pluginType, pluginClass );
        if ( pluginId == null ) {
            return null;
        }
        return getPlugin(pluginType, pluginId);
    }

    /**
     * classpath 下使用Class.forName
     * jar包下使用 urlClassLoader.loadClass
     */
    @SuppressWarnings("unchecked")
    public <T> T loadClass(PluginInterface plugin) {
        try {
            if(plugin.isNative()) {
                Class<? extends T> cl = (Class<? extends T>) Class.forName(plugin.getClassName());
                return (T) Class.forName(plugin.getClassName());
            } else {
                String className = plugin.getClassName();
                URLClassLoader ucl = plugin.getUrlClassLoader();
                Class<? extends T> cl = (Class<? extends T>) ucl.loadClass(className);
                return cl.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
