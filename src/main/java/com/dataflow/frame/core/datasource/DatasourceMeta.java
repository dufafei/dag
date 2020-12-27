package com.dataflow.frame.core.datasource;

import com.dataflow.frame.core.plugin.PluginInterface;
import com.dataflow.frame.core.plugin.PluginRegistry;

public class DatasourceMeta {

    private String dbType;
    private String dbAccess;
    private DatasourceInterface datasourceInterface;

    public DatasourceMeta(String dbType, String dbAccess,
                          String host, String port,
                          String db, String user, String pass) throws Exception {
        this.dbType = dbType;
        this.datasourceInterface = getDatabaseInterface(dbType);
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
        return registry.loadClass(sp);
    }

    private TypeAccess getAccessType(String dbAccess) {
        return TypeAccess.getTypeAccess(dbAccess);
    }

    public String getDbType() { return dbType; }

    public String getDbAccess() { return dbAccess; }

    public DatasourceInterface getDatasourceInterface() { return datasourceInterface; }

    public TypeAccess getAccessType() { return datasourceInterface.getAccessType(); }

    public String getHost() { return datasourceInterface.getHost(); }

    public String getPort() { return datasourceInterface.getPort(); }

    public String getDb() { return datasourceInterface.getDb(); }

    public String getUsername() { return datasourceInterface.getUser(); }

    public String getPassword() { return datasourceInterface.getPass(); }

    public String getDriverClass() { return datasourceInterface.getDriverClass(); }

    public String getUrl() { return datasourceInterface.getURL(); }
}
