package com.dataflow.frame.core.datasource;

import com.dataflow.frame.core.plugin.PluginInterface;
import com.dataflow.frame.core.plugin.PluginRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class DatasourceMeta {

    /**
     * The value to store in the attributes so that an empty value doesn't get lost...
     */
    public static final String EMPTY_OPTIONS_STRING = "><EMPTY><";

    private String dbType;
    private String dbAccess;
    private DatasourceInterface datasourceInterface;

    private DatasourceMeta() {}

    public DatasourceMeta(String dbType, String dbAccess,
                          String host, String port,
                          String db, String user, String pass) throws Exception {
        this.dbType = dbType;
        this.datasourceInterface = getDatabaseInterface(dbType);
        this.datasourceInterface.addDefaultOptions();
        this.dbAccess = dbAccess;
        this.datasourceInterface.setAccessType(getAccessType(dbAccess));
        this.datasourceInterface.setHost(host);
        this.datasourceInterface.setPort(port);
        this.datasourceInterface.setDb(db);
        this.datasourceInterface.setUser(user);
        this.datasourceInterface.setPass(pass);
    }

    private DatasourceInterface getDatabaseInterface(String databaseType) throws Exception {
        PluginRegistry registry = PluginRegistry.getInstance();
        PluginInterface sp = registry.getPlugin(DatasourcePluginType.class, databaseType);
        if(sp == null) {
            throw new Exception("missing datasource plugin:" + databaseType);
        }
        return registry.loadClass(sp);
    }

    private DatasourceTypeAccess getAccessType(String dbAccess) {
        return DatasourceTypeAccess.getTypeAccess(dbAccess);
    }

    public String getDbType() { return dbType; }

    public String getDbAccess() { return dbAccess; }

    public DatasourceInterface getDatasourceInterface() { return datasourceInterface; }

    public DatasourceTypeAccess getAccessType() { return datasourceInterface.getAccessType(); }

    public String getHost() { return datasourceInterface.getHost(); }

    public String getPort() { return datasourceInterface.getPort(); }

    public String getDb() { return datasourceInterface.getDb(); }

    public String getUsername() { return datasourceInterface.getUser(); }

    public String getPassword() { return datasourceInterface.getPass(); }

    public String getDriverClass() { return datasourceInterface.getDriverClass(); }

    public String getURL() throws Exception {
        return getURL(null);
    }

    public String getURL(String partitionId) throws Exception {
        String url;
        if (isPartitioned() && !StringUtils.isEmpty(partitionId)) {
            return null;
        } else {
            url = datasourceInterface.getURL();
        }
        if (datasourceInterface.supportsOptionsInURL()) {
            url = appendExtraOptions(url, getExtraOptions());
        }
        return url;
    }

    private String appendExtraOptions(String url, Map<String, String> extraOptions) {
        if (extraOptions.isEmpty()) {
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        final String optionIndicator = getExtraOptionIndicator();
        final String optionSeparator = getExtraOptionSeparator();
        final String valueSeparator = getExtraOptionValueSeparator();
        Iterator<String> iterator = extraOptions.keySet().iterator();
        boolean first = true;
        while (iterator.hasNext()) {
            String parameter = iterator.next();
            final String value = extraOptions.get(parameter);
            if (StringUtils.isEmpty(value) || value.equals(EMPTY_OPTIONS_STRING)) {
                // skip this science no value is provided
                continue;
            }
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

    /**
     * @return true if the connection contains partitioning information
     */
    public boolean isPartitioned() { return datasourceInterface.isPartitioned(); }

    public String getExtraOptionIndicator() { return datasourceInterface.getExtraOptionIndicator(); }

    public String getExtraOptionValueSeparator() { return datasourceInterface.getExtraOptionValueSeparator(); }

    public String getExtraOptionSeparator() { return datasourceInterface.getExtraOptionSeparator(); }

    public Map<String, String> getExtraOptions() { return datasourceInterface.getExtraOptions(); }
}
