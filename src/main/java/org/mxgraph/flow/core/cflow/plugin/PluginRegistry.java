package org.mxgraph.flow.core.cflow.plugin;

import org.mxgraph.flow.core.cflow.Const;
import org.apache.commons.lang3.StringUtils;
import org.mxgraph.flow.core.cflow.plugin.vfs2.FileCache;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 插件注册类。
 * 保存所有注册了的插件信息。
 */
public class PluginRegistry {

    private static final PluginRegistry pluginRegistry = new PluginRegistry();
    // 插件类型的列表
    private static final List<PluginTypeInterface> pluginTypes = new ArrayList<>();
    // 插件类型-> 该类型插件列表
    private final Map<Class<? extends PluginTypeInterface>, List<PluginInterface>> pluginMap = new HashMap<>();
    // 插件类型-> 该类型插件的类目
    private final Map<Class<? extends PluginTypeInterface>, List<String>> categoryMap = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private PluginRegistry() {};

    public static PluginRegistry getInstance() {
        return pluginRegistry;
    }

    public static synchronized void addPluginType( PluginTypeInterface type ) {
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

    /**
     * 注册插件类型并加载它们各自的插件
     */
    public static synchronized void init() throws Exception {
        init(false);
    }

    public static synchronized void init(boolean keepCache) throws Exception {
        final PluginRegistry registry = getInstance();
        for (final PluginTypeInterface pluginType : pluginTypes) {
            registry.registerType(pluginType);
        }
        if (!keepCache) {
            FileCache.getInstance().clear();
        }
    }

    private void registerType(PluginTypeInterface pluginType) throws Exception {
        registerType(pluginType.getClass());
        pluginType.searchPlugins();
    }

    /**
     * 加锁
     */
    private void registerType(Class<? extends PluginTypeInterface> pluginType) {
        lock.writeLock().lock();
        try {
            pluginMap.computeIfAbsent(pluginType, k -> new ArrayList<>());
            categoryMap.computeIfAbsent(pluginType, k -> new ArrayList<>());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 注册插件
     */
    public void registerPlugin(Class<? extends PluginTypeInterface> pluginType, PluginInterface plugin) throws Exception {
        lock.writeLock().lock();
        try {
            if(plugin.getId() == null) {
                throw new Exception("Not a valid id specified in plugin :" + plugin);
            }
            List<PluginInterface> list = pluginMap.computeIfAbsent(pluginType, k -> new ArrayList<>());
            int index = list.indexOf(plugin);
            if (index < 0) {
                // 添加插件
                list.add(plugin);
            } else {
                list.set(index, plugin);
            }
            // 对插件列表按照名称排序
            list.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
            if (StringUtils.isNotBlank(plugin.getCategory())) {
                // 对插件的类目进行排序
                List<String> categories = categoryMap.computeIfAbsent(pluginType, k -> new ArrayList<>());
                if (!categories.contains(plugin.getCategory())) {
                    // 添加类目
                    categories.add(plugin.getCategory());
                    // 对类目列表排序
                    Collections.sort(categories);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings( "unchecked" )
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

    @SuppressWarnings("unchecked")
    public <T> T loadClass(PluginInterface plugin) throws Exception {
        String className = plugin.getClassName();
        Class<? extends T> cl;
        if (plugin.isNativePlugin()) {
            cl = (Class<? extends T>) Class.forName(className) ;
        } else {
            URLClassLoader ucl = plugin.getUrlClassLoader();
            cl = (Class<? extends T>) ucl.loadClass(className);
        }
        return cl.newInstance();
    }
}
