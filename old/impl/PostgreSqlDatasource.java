package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.DatabaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatabaseTypeAccess;

@Datasource(
        type = "PostgreSql",
        typeDescription = "PostgreSql数据源"
)
public class PostgreSqlDatasource extends DatabaseDatasource {

    @Override
    public DatabaseTypeAccess[] getAccessTypeList() {
        return new DatabaseTypeAccess[]{DatabaseTypeAccess.TYPE_ACCESS_JDBC};
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return 5432;
        }
        return null;
    }

    @Override
    public String getDriverClass() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_ODBC) {
            return "sun.jdbc.odbc.JdbcOdbcDriver";
        } else {
            return "org.postgresql.Driver";
        }
    }

    @Override
    public String getURL() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_ODBC) {
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
