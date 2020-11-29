package org.mxgraph.flow.core.vertex;

import org.mxgraph.flow.core.AbstractMeta;

/**
 * 保存点的属性，用于将点转换成插件
 * @param <T>
 */
public abstract class VertexMeta<T extends AbstractMeta> {

    private String id; // 唯一标识
    private String name; // 名称
    private String pluginId; // 对应的插件ID
    private T parent;
    private boolean enabled; // 是否启用
    private boolean isConnect; // 是否被线连接

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPluginId() { return pluginId; }
    public void setPluginId(String pluginId) { this.pluginId = pluginId; }
    public T getParent() { return parent; }
    public void setParent(T parent) { this.parent = parent; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isConnect() { return isConnect; }
    public void setConnect(boolean connect) { isConnect = connect; }
}
