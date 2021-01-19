package com.dataflow.frame.core.datasource;

import org.apache.commons.lang3.StringUtils;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: 272558733@qq.com
 * @Date: 2021/1/19 1:20
 * @Description:
 */
public class DatabaseMeta {

    private DatabaseDatasource databaseDatasource;

    public DatabaseMeta(String id) {
        this.databaseDatasource = (DatabaseDatasource) DatasourcePluginType
                .getInstance()
                .getPluginInstance(id);
    }

    public DatabaseMeta(String id,
                        DatabaseAccess access,
                        String host,
                        Integer port,
                        String db,
                        String username,
                        String password) throws Exception {
        this(id);
        databaseDatasource.setAccess(access);
        databaseDatasource.setHost(host);
        databaseDatasource.setPort(port);
        databaseDatasource.setDb(db);
        databaseDatasource.setUsername(username);
        databaseDatasource.setPassword(password);
        databaseDatasource.addDefaultOptions();
    }

    public DatabaseDatasource getDatabaseInterface() { return databaseDatasource; }

    public String getId() { return databaseDatasource.getId(); }

    public boolean isPartitioned() { return databaseDatasource.isPartitioned(); }

    public DatabaseAccess getAccess() { return databaseDatasource.getAccess(); }

    public Integer getDefaultPort() { return databaseDatasource.getDefaultPort(); }

    public String getDriverClass() { return databaseDatasource.getDriverClass(); }

    public String getHost() { return databaseDatasource.getHost(); }

    public Integer getPort() { return databaseDatasource.getPort(); }

    public String getDb() { return databaseDatasource.getDb(); }

    public String getUsername() { return databaseDatasource.getUsername(); }

    public String getPassword() { return databaseDatasource.getPassword(); }

    public PartitionDatabaseMeta[] getPartitioningInformation() {
        if (!isPartitioned()) {
            return new PartitionDatabaseMeta[] {};
        }
        return databaseDatasource.getPartitioningInformation();
    }

    public PartitionDatabaseMeta getPartitionMeta(String partitionId) {
        PartitionDatabaseMeta[] partitionInfo = getPartitioningInformation();
        for (PartitionDatabaseMeta partitionDatabaseMeta : partitionInfo) {
            if (partitionDatabaseMeta.getPartitionId().equals(partitionId)) {
                return partitionDatabaseMeta;
            }
        }
        return null;
    }

    public String getUrl() throws Exception {
        return getUrl(null);
    }

    public String getUrl(String partitionId) throws Exception {
        String url;
        String host;
        String port;
        String db;
        if (isPartitioned() && StringUtils.isNotEmpty(partitionId)) {
            PartitionDatabaseMeta partition = getPartitionMeta(partitionId);
            host = partition.getHostname();
            port = partition.getPort();
            db = partition.getDatabaseName();
        } else {
            host = getHost();
            port = String.valueOf(getPort());
            db = getDb();
        }
        url = databaseDatasource.getUrl(host, port, db);
        if (databaseDatasource.supportsOptionsInURL()) {
            url = appendExtraOptions(url, databaseDatasource.getOptions());
        }
        return url;
    }

    private String appendExtraOptions(String url, Map<String, String> extraOptions) {
        if (extraOptions.isEmpty()) {
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        final String optionIndicator = databaseDatasource.getExtraOptionIndicator();
        final String optionSeparator = databaseDatasource.getExtraOptionSeparator();
        final String valueSeparator = databaseDatasource.getExtraOptionValueSeparator();
        Iterator<String> iterator = extraOptions.keySet().iterator();
        boolean first = true;
        while (iterator.hasNext()) {
            String parameter = iterator.next();
            final String value = extraOptions.get(parameter);
            if (first && !url.contains(valueSeparator)) {
                urlBuilder.append(optionIndicator);
            } else {
                urlBuilder.append(optionSeparator);
            }
            urlBuilder.append(parameter).append(valueSeparator).append(value);
            first = false;
        }
        return urlBuilder.toString();
    }

    public String stripCR(String sql) {
        if (sql == null) { return null; }
        return stripCR(new StringBuilder(sql));
    }

    private String stripCR(StringBuilder sql) {
        if (!databaseDatasource.supportsNewLinesInSQL()) {
            for (int i = sql.length() - 1; i >= 0; i--) {
                if (sql.charAt(i) == '\n' || sql.charAt(i) == '\r') {
                    sql.setCharAt(i, ' ');
                }
            }
        }
        return sql.toString();
    }
}
