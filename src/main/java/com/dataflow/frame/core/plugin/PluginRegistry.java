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
    private Map<PluginTypeInterface, List<PluginInterface>> pluginMap;
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
        lock.writeLock().lock();
        try {
            pluginMap.computeIfAbsent(pluginType, k -> new ArrayList<>());
            pluginType.searchPlugins();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void registerPlugin(PluginTypeInterface pluginType, PluginInterface plugin) throws Exception {
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

    public void unRegisterPlugin(PluginTypeInterface pluginType, PluginInterface plugin) {
        lock.writeLock().lock();
        try {
            List<PluginInterface> list = pluginMap.get(pluginType);
            list.remove(plugin);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginInterface, K extends PluginTypeInterface> List<T> getPlugins(K type) {
        Set<T> set = new HashSet<>();
        lock.readLock().lock();
        try {
            for (PluginTypeInterface pi : pluginMap.keySet()) {
                if (Const.classIsOrExtends(pi.getClass(), type.getClass())) {
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

    public PluginInterface getPlugin(PluginTypeInterface pluginType, String id) {
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

    public String getPluginId(PluginTypeInterface pluginType, Object pluginClass) {
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

    public PluginInterface getPlugin(PluginTypeInterface pluginType, Object pluginClass ) {
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
        Class<? extends T> cl;
        try {
            if(plugin.isNative()) {
                cl = (Class<? extends T>) Class.forName(plugin.getClassName());
                return cl.newInstance();
            } else {
                URLClassLoader ucl = plugin.getUrlClassLoader();
                cl = (Class<? extends T>) ucl.loadClass(plugin.getClassName());
                return cl.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
