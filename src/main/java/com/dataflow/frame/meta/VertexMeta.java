package com.dataflow.frame.meta;

/**
 * 点的属性
 * @param <T>
 */
public abstract class VertexMeta<T extends FlowMeta> {

    private String nodeId; // 节点标识
    private String nodeName; // 节点名称
    private boolean connected; // 是否被线连接
    private boolean enabled; // 是否启用
    private T parent;
    private Integer index; // 节点的索引位置

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public T getParent() { return parent; }
    public void setParent(T parent) { this.parent = parent; }
    public Integer getIndex() { return index; }
    public void setIndex(Integer index) { this.index = index; }
}
