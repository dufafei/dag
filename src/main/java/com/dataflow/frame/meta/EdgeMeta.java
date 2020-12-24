package com.dataflow.frame.meta;

/**
 * 边的属性
 * @param <T>
 */
public abstract class EdgeMeta<T extends VertexMeta> {

    private T startNode; // 起始点
    private T endNode; // 终止点
    private boolean enabled; // 启用/禁用

    public T getStartNode() { return startNode; }
    public void setStartNode(T startNode) { this.startNode = startNode; }
    public T getEndNode() { return endNode; }
    public void setEndNode(T endNode) { this.endNode = endNode; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
