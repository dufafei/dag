package com.dataflow.frame.core.datasource;

import java.util.*;

public abstract class BaseDatasource implements DatasourceInterface {

    // The prefix for all the extra options attributes
    public static final String ATTRIBUTE_PREFIX_EXTRA_OPTION = "EXTRA_OPTION_";
    //  A flag to determine if the connection is clustered or not.
    public static final String ATTRIBUTE_IS_CLUSTERED = "IS_CLUSTERED";

    protected String name;
    protected DatasourceTypeAccess accessType;
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

    @Override
    public DatasourceTypeAccess getAccessType() { return accessType; }

    @Override
    public void setAccessType(DatasourceTypeAccess accessType) { this.accessType = accessType; }

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

    @Override
    public void addExtraOption(String option, String value) {
        attributes.put(ATTRIBUTE_PREFIX_EXTRA_OPTION + option, value);
    }

    @Override
    public void addDefaultOptions() {}

    @Override
    public Map<String, String> getExtraOptions() {
        Map<String, String> map = new Hashtable<>();
        for (Enumeration<Object> keys = attributes.keys(); keys.hasMoreElements();) {
            String attribute = (String) keys.nextElement();
            if (attribute.startsWith(ATTRIBUTE_PREFIX_EXTRA_OPTION) ) {
                String value = attributes.getProperty(attribute, "");
                map.put(attribute.substring(ATTRIBUTE_PREFIX_EXTRA_OPTION.length()), value);
            }
        }
        return map;
    }

    @Override
    public boolean isPartitioned() {
        String isClustered = attributes.getProperty(ATTRIBUTE_IS_CLUSTERED);
        return "Y".equalsIgnoreCase(isClustered);
    }

    @Override
    public void setPartitioned(boolean clustered) {
        attributes.setProperty(ATTRIBUTE_IS_CLUSTERED, clustered ? "Y" : "N");
    }
}
