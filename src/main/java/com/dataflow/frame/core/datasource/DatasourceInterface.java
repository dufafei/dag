package com.dataflow.frame.core.datasource;

import java.util.Properties;

public interface DatasourceInterface extends Cloneable {

    String getName();

    TypeAccess[] getAccessTypeList();

    TypeAccess getDefaultDatabasePort();

    String getDriverClass();

    String getURL();

    Properties getAttributes();

    TypeAccess getAccessType();

    void setAccessType(TypeAccess accessType);

    String getHost();

    void setHost(String host);

    String getPort();

    void setPort(String port);

    String getDb();

    void setDb(String db);

    String getUser();
    void setUser(String user);

    String getPass();

    void setPass(String pass);

    String getServername();

    void setServername(String serverName);

    String getDataTablespace();

    void setDataTablespace(String dataTablespace);

    String getIndexTablespace();

    void setIndexTablespace(String IndexTablespace);
}
