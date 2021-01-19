package com.dataflow.frame.core.datasource;

import java.util.*;

/**
 * 数据库类型的数据源基类
 */
public abstract class DatabaseDatasource implements DatasourceInterface {

    private static final String ATTRIBUTE_IS_CLUSTERED = "IS_CLUSTERED";
    private static final String ATTRIBUTE_CLUSTER_PARTITION_PREFIX = "CLUSTER_PARTITION_";
    private static final String ATTRIBUTE_CLUSTER_HOSTNAME_PREFIX = "CLUSTER_HOSTNAME_";
    public static final String ATTRIBUTE_CLUSTER_PORT_PREFIX = "CLUSTER_PORT_";
    public static final String ATTRIBUTE_CLUSTER_DBNAME_PREFIX = "CLUSTER_DBNAME_";

    private static final String ATTRIBUTE_SUPPORTS_BOOLEAN_DATA_TYPE = "SUPPORTS_BOOLEAN_DATA_TYPE";
    private static final String ATTRIBUTE_SUPPORTS_TIMESTAMP_DATA_TYPE = "SUPPORTS_TIMESTAMP_DATA_TYPE";

    // 连接方式
    private DatabaseAccess access;

    // ip
    private String host;

    // 端口
    private Integer port;

    // 数据库
    private String db;

    // 用户名
    private String username;

    // 密码
    private String password;

    // Informix only!
    private String servername;

    // 数据表空间, For Oracle & perhaps others
    private String dataTablespace;

    // 索引表空间, For Oracle & perhaps others
    private String indexTablespace;

    // 参数
    private Map<String, String> properties;

    // 选项
    private Map<String, String> option;

    public DatabaseDatasource() {
        properties = new HashMap<>();
        option = new HashMap<>();
        if (getAccessList() != null && getAccessList().length > 0) {
            access = getAccessList()[0];
        }
    }

    public String getId() { return this.getClass().getAnnotation(Datasource.class).id(); }

    public DatabaseAccess getAccess() { return access; }

    public void setAccess(DatabaseAccess access) { this.access = access; }

    @Override
    public String getHost() { return host; }

    @Override
    public void setHost(String host) { this.host = host; }

    @Override
    public Integer getPort() { return port; }

    @Override
    public void setPort(Integer port) { this.port = port; }

    public String getDb() { return db; }

    public void setDb(String db) { this.db = db; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getServername() { return servername; }

    public void setServername(String servername) { this.servername = servername; }

    public String getDataTablespace() { return dataTablespace; }

    public void setDataTablespace(String dataTablespace) { this.dataTablespace = dataTablespace; }

    public String getIndexTablespace() { return indexTablespace; }

    public void setIndexTablespace(String indexTablespace) { this.indexTablespace = indexTablespace; }

    public abstract DatabaseAccess[] getAccessList();

    // 是否是集群
    public boolean isPartitioned() {
        String isClustered = properties.get(ATTRIBUTE_IS_CLUSTERED);
        return "Y".equalsIgnoreCase(isClustered);
    }

    public void setPartitioned(boolean clustered) {
        properties.put(ATTRIBUTE_IS_CLUSTERED, clustered ? "Y" : "N");
    }

    public PartitionDatabaseMeta[] getPartitioningInformation() {
        int nr = 0;
        while ((properties.get(ATTRIBUTE_CLUSTER_HOSTNAME_PREFIX + nr)) != null) {
            nr++;
        }
        PartitionDatabaseMeta[] clusterInfo = new PartitionDatabaseMeta[nr];
        /*for ( nr = 0; nr < clusterInfo.length; nr++ ) {
            String partitionId = properties.get(ATTRIBUTE_CLUSTER_PARTITION_PREFIX + nr);
            String hostname = properties.get(ATTRIBUTE_CLUSTER_HOSTNAME_PREFIX + nr);
            String port = properties.get(ATTRIBUTE_CLUSTER_PORT_PREFIX + nr);
            String dbName = properties.get(ATTRIBUTE_CLUSTER_DBNAME_PREFIX + nr);
            String username = properties.getProperty(ATTRIBUTE_CLUSTER_USERNAME_PREFIX + nr);
            String password = properties.getProperty(ATTRIBUTE_CLUSTER_PASSWORD_PREFIX + nr);
            clusterInfo[nr] = new PartitionDatabaseMeta(partitionId, hostname, port, dbName);
            clusterInfo[nr].setUsername(username);
            clusterInfo[nr].setPassword(Encr.decryptPasswordOptionallyEncrypted(password));
        }*/
        return clusterInfo;
    };

    // 是否支持布尔数据类型
    public boolean supportsBooleanDataType() {
        String supportBool = properties.get(ATTRIBUTE_SUPPORTS_BOOLEAN_DATA_TYPE);
        return "Y".equalsIgnoreCase(supportBool);
    }

    public void setSupportsBooleanDataType(boolean b) {
        properties.put(ATTRIBUTE_SUPPORTS_BOOLEAN_DATA_TYPE, b ? "Y" : "N");
    }

    // 是否支持时间戳数据类型
    public boolean supportsTimestampDataType() {
        String supportTimestamp = properties.get(ATTRIBUTE_SUPPORTS_TIMESTAMP_DATA_TYPE);
        return "Y".equalsIgnoreCase(supportTimestamp);
    }

    public void setSupportsTimestampDataType(boolean b) {
        properties.put(ATTRIBUTE_SUPPORTS_TIMESTAMP_DATA_TYPE, b ? "Y" : "N");
    }

    // url后面是否支持添加选项
    public abstract boolean supportsOptionsInURL();

    // url和选项之间的分隔符 默认?
    public String getExtraOptionIndicator() { return "?"; }

    // 选项之间的分隔符 默认&
    public String getExtraOptionSeparator() { return "&"; }

    // 选项与选项值的分隔符 默认=
    public String getExtraOptionValueSeparator() { return "="; }

    // 添加额外选项
    protected void addExtraOption(String key, String value) { option.put(key, value); }

    // 添加默认选项
    public void addDefaultOptions() {}

    // 获取选项
    public Map<String, String> getOptions() { return option; }

    // 获取驱动类
    public abstract String getDriverClass();

    // 获取url
    public abstract String getUrl(String host, String port, String db);

    // 如果数据库支持SQL语句中的换行，则为true。
    public abstract boolean supportsNewLinesInSQL();

    // 如果数据库支持从准备好的语句检索查询元数据，则为true。
    // 如果查询为False需要先执行。
    public abstract boolean supportsPreparedStatementMetadataRetrieval();

    // 是否支持truncate,不是所有的数据库都支持truncate table,其中PostgresDialect就不支持
    public abstract boolean supportsTruncateTable();
}
