package com.dataflow.frame.core.datasource;

import java.util.Map;
import java.util.Properties;

public interface DatasourceInterface extends Cloneable {

    String getName();

    DatasourceTypeAccess[] getAccessTypeList();

    Integer getDefaultDatabasePort();

    String getDriverClass();

    String getURL();

    Properties getAttributes();

    DatasourceTypeAccess getAccessType();

    void setAccessType(DatasourceTypeAccess accessType);

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

    // spark专用
    Map<String, String> getSparkOptions();
}
