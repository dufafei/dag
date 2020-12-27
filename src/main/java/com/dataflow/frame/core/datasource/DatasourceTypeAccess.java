package com.dataflow.frame.core.datasource;

public enum DatasourceTypeAccess {
    TYPE_ACCESS_JDBC("JDBC", 0),
    TYPE_ACCESS_ODBC("ODBC",1),
    TYPE_ACCESS_OCI("OCI",2),
    TYPE_ACCESS_Plugin("Plugin",3),
    TYPE_ACCESS_JNDI("JNDI",4),
    TYPE_ACCESS_SPARK_THRIFT("SPARK_THRIFT",5),
    ;

    private String type;
    private Integer code;

    public String getType() {
        return type;
    }

    public Integer getCode() {
        return code;
    }

    DatasourceTypeAccess(String type, Integer code) {
        this.type = type;
        this.code = code;
    }

    public static DatasourceTypeAccess getTypeAccess(String type) {
        for (DatasourceTypeAccess value : DatasourceTypeAccess.values()) {
            if(value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
