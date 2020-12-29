package com.dataflow.frame.core.datasource;

import java.util.Map;
import java.util.Properties;

public interface DatasourceInterface extends Cloneable {

    String getName();

    DatasourceTypeAccess[] getAccessTypeList();

    Integer getDefaultDatabasePort();

    String getDriverClass();

    String getURL();

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

    // url后面是否支持添加选项
    boolean supportsOptionsInURL();

    // url和选项之间的分隔符 一般是?
    String getExtraOptionIndicator();

    // 选项与选项值的分隔符 一般是=
    String getExtraOptionValueSeparator();

    // 选项之间的分隔符 一般是&
    String getExtraOptionSeparator();

    // 添加url选项
    void addExtraOption(String option, String value);

    // 添加url默认选项
    void addDefaultOptions();

    // 获取url选项
    Map<String, String> getExtraOptions();

    boolean isPartitioned();

    void setPartitioned(boolean clustered);

    // spark专用
    //Map<String, String> getSparkReadOptions();
    //Map<String, String> getSparkWriteOptions();
}
