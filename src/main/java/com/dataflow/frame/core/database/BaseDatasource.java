package com.dataflow.frame.core.database;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class BaseDatasource implements DatasourceInterface {

    protected String name;
    protected int accessType;
    protected String host;
    protected String port;
    protected String db;
    protected String user;
    protected String pass;
    protected String servername; // Informix only!
    protected String dataTablespace; // data storage location, For Oracle & perhaps others
    protected String indexTablespace; // index storage location, For Oracle & perhaps others
    protected Properties attributes; // 连接参数

    public BaseDatasource(){
        attributes = new Properties();
        if (getAccessTypeList() != null && getAccessTypeList().length > 0) {
            // 默认值
            accessType = getAccessTypeList()[0];
        }
    }

    @Override
    public String getName() { return name; }

    public Properties getAttributes() { return attributes; }

    @Override
    public int getAccessType() { return accessType; }

    @Override
    public void setAccessType(int accessType) { this.accessType = accessType; }

    @Override
    public String getHost() { return host; }

    public void setHost(String host) { this.host = host; }

    @Override
    public String getPort() { return port; }

    @Override
    public void setPort(String port) { this.port = port; }

    @Override
    public String getDb() { return db; }

    @Override
    public void setDb(String db) { this.db = db; }

    @Override
    public String getUser() { return user; }

    @Override
    public void setUser(String user) { this.user = user; }

    @Override
    public String getPass() { return pass; }

    @Override
    public void setPass(String pass) { this.pass = pass; }

    @Override
    public String getServername() { return servername; }

    @Override
    public void setServername(String servername) { this.servername = servername; }

    @Override
    public String getDataTablespace() { return dataTablespace; }

    @Override
    public void setDataTablespace(String dataTablespace) { this.dataTablespace = dataTablespace; }

    @Override
    public String getIndexTablespace() { return indexTablespace; }

    @Override
    public void setIndexTablespace(String indexTablespace) { this.indexTablespace = indexTablespace; }

    public String urlJoint(String prefix, String separator) {
        List<String> params = new ArrayList<>();
        for (Object o : attributes.keySet()) {
            String key = (String) o;
            String value = attributes.getProperty(key);
            params.add(key + "=" + value);
        }
        return prefix + StringUtils.join(params, separator);
    }

    public String urlJoint() {
        return urlJoint("?", "&");
    }
}
