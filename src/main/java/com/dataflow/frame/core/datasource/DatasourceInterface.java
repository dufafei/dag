package com.dataflow.frame.core.datasource;

import java.util.Map;

/**
 * 数据源接口
 */
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

    // 如果数据库支持SQL语句中的换行，则为true。
    boolean supportsNewLinesInSQL();

    // 如果数据库支持从准备好的语句检索查询元数据，则为true。
    // 如果查询为False需要先执行。
    boolean supportsPreparedStatementMetadataRetrieval();

    // 是否支持truncate,不是所有的数据库都支持truncate table,其中PostgresDialect就不支持
    boolean supportsTruncateTable();
}
