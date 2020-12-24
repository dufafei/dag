package com.dataflow.frame.meta;

/**
 * 点的属性
 * @param <T>
 */
public abstract class VertexMeta<T extends FlowMeta> {

    private String nodeId; // 节点唯一标识
    private String nodeName; // 节点名称
    private String nodeType; // 节点类型
    private boolean connected; // 是否被线连接
    private boolean enabled; // 是否启用
    private T parent;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public T getParent() { return parent; }
    public void setParent(T parent) { this.parent = parent; }
}
