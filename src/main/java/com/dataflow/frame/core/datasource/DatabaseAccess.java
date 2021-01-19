package com.dataflow.frame.core.datasource;

/**
 * 数据库连接方式
 */
public enum DatabaseAccess {

    TYPE_ACCESS_JDBC("JDBC"),
    TYPE_ACCESS_ODBC("ODBC"),
    TYPE_ACCESS_OCI("OCI"),
    TYPE_ACCESS_Plugin("Plugin"),
    TYPE_ACCESS_JNDI("JNDI"),
    ;

    private String code;

    DatabaseAccess(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DatabaseAccess getAccess(String code) throws Exception {
        for (DatabaseAccess value : DatabaseAccess.values()) {
            if(value.getCode().equals(code)) {
                return value;
            }
        }
        throw new RuntimeException("not found");
    }
}
