package org.mxgraph.flow.core.cflow.database;

import org.mxgraph.flow.core.cflow.database.datasource.HiveDataSource;
import org.mxgraph.flow.core.cflow.database.datasource.MysqlDatasource;

public enum DatasourceEnum {

    MYSQL("mysql", new MysqlDatasource()),
    HIVE("hive", new HiveDataSource()),
    ;

    private String code;
    private DatasourceInterface datasourceInterface;

    DatasourceEnum(String code, DatasourceInterface datasourceInterface) {
        this.code = code;
        this.datasourceInterface = datasourceInterface;
    }

    public String getCode() { return code; }

    public DatasourceInterface getDatasourceInterface() {
        return datasourceInterface;
    }

    /*public DatasourceEnum getDatasourceByCode(String code) {

    }*/
}
