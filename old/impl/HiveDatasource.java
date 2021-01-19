package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.DatabaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatabaseTypeAccess;

@Datasource(
        type = "Hive",
        typeDescription = "Hive数据源"
)
public class HiveDatasource extends DatabaseDatasource {

    @Override
    public DatabaseTypeAccess[] getAccessTypeList() {
        return new DatabaseTypeAccess[] {
            DatabaseTypeAccess.TYPE_ACCESS_JDBC,
            /*DatasourceTypeAccess.TYPE_ACCESS_SPARK_THRIFT*/
        };
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if(getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return 10000;
        }
        /*if(getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_SPARK_THRIFT) {
            return 9083;
        }*/
        return null;
    }

    @Override
    public String getDriverClass() {
        if(getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return "org.apache.hive.jdbc.HiveDriver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if(getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return "jdbc:hive2://" + getHost() + ":" + getPort() + "/";
        }
        /*if(getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_SPARK_THRIFT) {
            return "thrift://" + host + ":" + port + "/";
        }*/
        return null;
    }

    @Override
    public boolean supportsOptionsInURL() {
        return false;
    }

    @Override
    public String getExtraOptionIndicator() {
        return null;
    }

    @Override
    public String getExtraOptionValueSeparator() {
        return null;
    }

    @Override
    public String getExtraOptionSeparator() {
        return null;
    }

    @Override
    public boolean supportsNewLinesInSQL() {
        return true;
    }

    @Override
    public boolean supportsPreparedStatementMetadataRetrieval() {
        return true;
    }

    @Override
    public boolean supportsTruncateTable() {
        return true;
    }
}
