package com.dataflow.frame.core.database;

import com.dataflow.frame.core.plugin.PluginInterface;
import com.dataflow.frame.core.plugin.PluginRegistry;

public class DatasourceMeta {

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

    public DatasourceMeta(String type, String access,
                          String host, String port,
                          String db, String user, String pass) throws Exception {
        setValues(type, access, host, port, db, user, pass);
    }

    public void setValues(String type, String access,
                          String host, String port, String db,
                          String user, String pass) throws Exception {
        datasourceInterface = getDatabaseInterface(type);
        datasourceInterface.setAccessType(getAccessType(access));
        datasourceInterface.setHost(host);
        datasourceInterface.setPort(port);
        datasourceInterface.setDb(db);
        datasourceInterface.setUser(user);
        datasourceInterface.setPass(pass);
    }

    public DatasourceInterface getDatabaseInterface(String databaseType) throws Exception {
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

    public DatasourceInterface getDatasourceInterface() { return datasourceInterface; }

    public static String[] getDbAccessTypeCode() { return dbAccessTypeCode; }

    public String getDriver() { return datasourceInterface.getDriverClass(); }

    public String getUrl() { return datasourceInterface.getURL(); }

    public String getUser() { return datasourceInterface.getUser(); }

    public String getPass() { return datasourceInterface.getPass(); }
}
