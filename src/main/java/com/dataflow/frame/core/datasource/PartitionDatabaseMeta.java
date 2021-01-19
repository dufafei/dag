package com.dataflow.frame.core.datasource;

/**
 * @Author: 272558733@qq.com
 * @Date: 2021/1/19 11:47
 * @Description:
 */
public class PartitionDatabaseMeta {

    private String partitionId;
    private String hostname;
    private String port;
    private String databaseName;
    private String username;
    private String password;

    public PartitionDatabaseMeta(String partitionId, String hostname, String port, String database) {
        this.partitionId = partitionId;
        this.hostname = hostname;
        this.port = port;
        this.databaseName = database;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
