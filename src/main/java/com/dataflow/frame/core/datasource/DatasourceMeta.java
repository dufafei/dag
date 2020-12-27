package com.dataflow.frame.core.datasource;

import com.dataflow.frame.core.plugin.PluginInterface;
import com.dataflow.frame.core.plugin.PluginRegistry;

public class DatasourceMeta {

    private String dbType;
    private String dbAccess;
    private DatasourceInterface datasourceInterface;
    public static final int TYPE_ACCESS_JDBC = 0;
    public static final int TYPE_ACCESS_ODBC = 1;
    public static final int TYPE_ACCESS_OCI = 2;
    public static final int TYPE_ACCESS_Plugin = 3;
    public static final int TYPE_ACCESS_JNDI = 4;
    public static final int TYPE_ACCESS_SPARK_THRIFT = 5; // SPARK-HIVE

    public static final String[] dbAccessTypeCode = {
            "Native (JDBC)", "ODBC", "OCI", "Plugin", "JNDI","SPARK_THRIFT"
    };

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

    public int getAccessType(String dbAccess) {
        for(int i = 0; i < dbAccessTypeCode.length; i++) {
            if (dbAccessTypeCode[i].equalsIgnoreCase(dbAccess)) {
                return i;
            }
        }
        return TYPE_ACCESS_JDBC;
    }

    public String getDbType() { return dbType; }

    public DatasourceInterface getDatasourceInterface() { return datasourceInterface; }

    public String getDbAccess() { return dbAccess; }

    public String getDriverClass() { return datasourceInterface.getDriverClass(); }

    public String getHost() { return datasourceInterface.getHost(); }

    public String getPort() { return datasourceInterface.getPort(); }

    public String getDb() { return datasourceInterface.getDb(); }

    public String getUsername() { return datasourceInterface.getUser(); }

    public String getPassword() { return datasourceInterface.getPass(); }
}
