package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.DatabaseAccess;
import com.dataflow.frame.core.datasource.DatabaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import org.apache.commons.lang3.StringUtils;

@Datasource(
        id = "mysql",
        category = "database",
        description = "Mysql数据源"
)
public class MysqlDatasource extends DatabaseDatasource {

    @Override
    public Integer getDefaultPort() {
        if (getAccess() == DatabaseAccess.TYPE_ACCESS_JDBC) {
            return 3306;
        }
        return null;
    }

    @Override
    public DatabaseAccess[] getAccessList() {
        return new DatabaseAccess[]{ DatabaseAccess.TYPE_ACCESS_JDBC };
    }

    @Override
    public String getDriverClass() {
        if(getAccess() == DatabaseAccess.TYPE_ACCESS_JDBC) {
            return "org.gjt.mm.mysql.Driver";
        }
        return null;
    }

    @Override
    public String getUrl(String host, String port, String db) {
        if(getAccess() == DatabaseAccess.TYPE_ACCESS_JDBC) {
            if (StringUtils.isEmpty(port)) {
                return "jdbc:mysql://" + host + "/" + db;
            } else {
                return "jdbc:mysql://" + host + ":" + port + "/" + db;
            }
        }
        return null;
    }

    @Override
    public boolean supportsOptionsInURL() {
        return true;
    }

    @Override
    public boolean supportsNewLinesInSQL() {
        return true;
    }

    @Override
    public boolean supportsPreparedStatementMetadataRetrieval() { return true; }

    @Override
    public boolean supportsTruncateTable() { return true; }

    @Override
    public void addDefaultOptions() {
        addExtraOption("useSSL", "false");
        // 批处理只针对更新(增、删、改)。
        // mysql默认批处理是关闭的，需要在url参数后面加上?rewriteBatchedStatement=true
        addExtraOption("rewriteBatchedStatements", "true");
    }
}
