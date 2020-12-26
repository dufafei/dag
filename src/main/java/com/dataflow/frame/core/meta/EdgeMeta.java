package com.dataflow.frame.core.meta;

public abstract class EdgeMeta<T extends VertexMeta> {

    private T startNode;
    private T endNode;
    private boolean enabled;

    public T getStartNode() { return startNode; }

    public void setStartNode(T startNode) {
        startNode.setConnected(true);
        this.startNode = startNode;
    }

    public T getEndNode() { return endNode; }

    public void setEndNode(T endNode) {
        endNode.setConnected(true);
        this.endNode = endNode;
    }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
