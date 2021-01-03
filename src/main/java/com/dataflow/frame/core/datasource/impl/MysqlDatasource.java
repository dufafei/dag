package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.BaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatasourceTypeAccess;
import org.apache.commons.lang3.StringUtils;

/*
 * 已测试
 */
@Datasource(
        type = "Mysql",
        typeDescription = "Mysql数据源"
)
public class MysqlDatasource extends BaseDatasource {

    @Override
    public DatasourceTypeAccess[] getAccessTypeList() {
        return new DatasourceTypeAccess[]{DatasourceTypeAccess.TYPE_ACCESS_JDBC};
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if (getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
            return 3306;
        }
        return null;
    }

    @Override
    public String getDriverClass() {
        if(getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
            return "org.gjt.mm.mysql.Driver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if(getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
            if (StringUtils.isEmpty(getPort())) {
                return "jdbc:mysql://" + getHost() + "/" + getDb();
            } else {
                return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDb();
            }
        }
        return null;
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
    public void addDefaultOptions() {

        addExtraOption("useSSL", "false");
        /*
         * 批处理只针对更新(增、删、改),没有查询什么事。
         * mysql默认批处理是关闭的，需要在url参数后面加上?rewriteBatchedStatement=true
         */
        addExtraOption("rewriteBatchedStatements", "true");
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
