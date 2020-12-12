package org.mxgraph.flow.core.cflow.database;

import java.util.Properties;

public interface DatasourceInterface extends Cloneable {

    String getName(); // 连接类型

    int[] getAccessTypeList(); //连接方式

    int getDefaultDatabasePort(); //默认端口

    String getDriverClass(); //驱动类

    String getURL(); //URL

    Properties getAttributes(); // 连接参数

    // access 连接方式
    int getAccessType();
    void setAccessType(int accessType);

    // host
    String getHost();
    void setHost(String host);

    // port
    String getPort();
    void setPort(String port);

    // db 数据库
    String getDb();
    void setDb(String db);

    // username
    String getUser();
    void setUser(String user);

    // password
    String getPass();
    void setPass(String pass);

    // servername
    String getServername();
    void setServername(String serverName);

    //
    String getDataTablespace();
    void setDataTablespace(String dataTablespace);

    //
    String getIndexTablespace();
    void setIndexTablespace(String IndexTablespace);


}
