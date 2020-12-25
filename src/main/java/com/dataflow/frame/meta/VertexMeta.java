package com.dataflow.frame.meta;

public abstract class VertexMeta<T extends FlowMeta> {

    private String nodeId;
    private String nodeName;
    private boolean connected;
    private boolean enabled;
    private T parent;
    private String index;

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

    public String getIndex() { return index; }

    public void setIndex(String index) { this.index = index; }
}
