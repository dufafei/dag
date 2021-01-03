package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.BaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatasourceTypeAccess;

@Datasource(
        type = "PostgreSql",
        typeDescription = "PostgreSql数据源"
)
public class PostgreSqlDatasource extends BaseDatasource {

    @Override
    public DatasourceTypeAccess[] getAccessTypeList() {
        return new DatasourceTypeAccess[]{DatasourceTypeAccess.TYPE_ACCESS_JDBC};
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if (getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
            return 5432;
        }
        return null;
    }

    @Override
    public String getDriverClass() {
        if (getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_ODBC) {
            return "sun.jdbc.odbc.JdbcOdbcDriver";
        } else {
            return "org.postgresql.Driver";
        }
    }

    @Override
    public String getURL() {
        if (getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_ODBC) {
            return "jdbc:odbc:" + getDb();
        } else {
            return "jdbc:postgresql://" + getHost() + ":" + port + "/" + getDb();
        }
    }

    @Override
    public boolean supportsOptionsInURL() {
        return true;
    }

    @Override
    public String getExtraOptionIndicator() {
        return "?";
    }

    @Override
    public String getExtraOptionValueSeparator() {
        return "=";
    }

    @Override
    public String getExtraOptionSeparator() {
        return "&";
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
