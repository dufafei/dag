package com.github.dufafei.dag.meta;

public abstract class BaseNodeMeta {

    private String id;
    private String name;
    private Integer serialNumber;
    private Boolean enabled;
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
