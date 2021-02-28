package com.basics.frame.dag.meta;

public abstract class BaseHopMeta<T extends BaseNodeMeta> {

    private T source;
    private T target;
    private Boolean enabled;

    public BaseHopMeta() {}

    public T getSource() { return source; }

    public void setSource(T source) {
        source.setConnected(true);
        this.source = source;
    }

    public T getTarget() { return target; }

    public void setTarget(T target) {
        target.setConnected(true);
        this.target = target;
    }

    public Boolean getEnabled() { return enabled; }

    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
