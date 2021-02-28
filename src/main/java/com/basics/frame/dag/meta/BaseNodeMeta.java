package com.basics.frame.dag.meta;

public abstract class BaseNodeMeta {

    // 节点标识
    private String id;
    // 节点名称
    private String name;
    // 节点编号
    private Integer serialNumber;
    // 节点是否禁用
    private Boolean enabled;
    // 节点是否连线
    private Boolean connected;

    public BaseNodeMeta() {}

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Integer getSerialNumber() { return serialNumber; }

    public void setSerialNumber(Integer serialNumber) { this.serialNumber = serialNumber; }

    public Boolean getEnabled() { return enabled; }

    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Boolean getConnected() { return connected; }

    public void setConnected(Boolean connected) { this.connected = connected; }
}
