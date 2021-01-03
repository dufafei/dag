package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.BaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatasourceTypeAccess;
import org.apache.commons.lang3.StringUtils;

/*
 * 已测试
 */
@Datasource(
        type = "Dm",
        typeDescription = "达梦数据源"
)
public class DmDatasource extends BaseDatasource {

    @Override
    public DatasourceTypeAccess[] getAccessTypeList() {
        return new DatasourceTypeAccess[]{DatasourceTypeAccess.TYPE_ACCESS_JDBC};
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if (getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
            return 5236;
        }
        return null;
    }

    @Override
    public String getDriverClass() {
        if(getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
            return "dm.jdbc.driver.DmDriver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if(getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
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
