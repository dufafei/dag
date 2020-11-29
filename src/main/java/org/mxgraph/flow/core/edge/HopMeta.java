package org.mxgraph.flow.core.edge;

import org.mxgraph.flow.core.vertex.VertexMeta;

public class HopMeta<T extends VertexMeta> {

    private T from; // 起始点
    private T to; // 终止点
    private boolean enabled; // 是否启用

    public HopMeta(){}
    public T getFrom() {
        return from;
    }
    public void setFrom(T from) {
        this.from = from;
    }
    public T getTo() {
        return to;
    }
    public void setTo(T to) {
        this.to = to;
    }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
