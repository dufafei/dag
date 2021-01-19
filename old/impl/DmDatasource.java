package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.DatabaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatabaseTypeAccess;
import org.apache.commons.lang3.StringUtils;

/*
 * 已测试
 */
@Datasource(
        type = "Dm",
        typeDescription = "达梦数据源"
)
public class DmDatasource extends DatabaseDatasource {

    @Override
    public DatabaseTypeAccess[] getAccessTypeList() {
        return new DatabaseTypeAccess[]{DatabaseTypeAccess.TYPE_ACCESS_JDBC};
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return 5236;
        }
        return null;
    }

    @Override
    public String getDriverClass() {
        if(getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return "dm.jdbc.driver.DmDriver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if(getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            if (StringUtils.isEmpty(getPort())) {
                return "jdbc:dm://" + getHost() + "/" + getDb();
            } else {
                return "jdbc:dm://" + getHost() + ":" + getPort() + "/" + getDb();
            }
        }
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
