package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.DatabaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatabaseTypeAccess;
import org.apache.commons.lang3.StringUtils;

/*
 * https://www.jianshu.com/p/43f78c8a025b?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io
 * clickhouse不支持事务操作
 */
@Datasource(
        type = "ClickHouse",
        typeDescription = "ClickHouse数据源"
)
public class ClickHouseDatasource extends DatabaseDatasource {

    @Override
    public DatabaseTypeAccess[] getAccessTypeList() {
        return new DatabaseTypeAccess[]{DatabaseTypeAccess.TYPE_ACCESS_JDBC};
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return 9000;
        }
        return null;
    }

    /**
     * clickhouse支持两种jdbc驱动实现。
     * 一种是官方自带的8123端口的。
     * 这种方式是http协议实现的，整体性能差了很多 经常会出现超时的问题，且对数据压缩支持不好，
     * 因压缩速度跟不上写入速度，数据写入的过程中数据目录会快速膨胀 导致磁盘空间打满。
     * 一种是来自第三方实现的驱动，9000端口基于tcp协议。
     * 这种方式支持高性能写入，数据按列组织并压缩。
     */
    @Override
    public String getDriverClass() {
        if(getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return "com.github.housepower.jdbc.ClickHouseDriver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if(getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            if (StringUtils.isEmpty(getPort())) {
                return "jdbc:clickhouse://" + getHost() + "/" + getDb();
            } else {
                return "jdbc:clickhouse://" + getHost() + ":" + getPort() + "/" + getDb();
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
        return false;
    }

    @Override
    public boolean supportsPreparedStatementMetadataRetrieval() {
        return false;
    }

    @Override
    public boolean supportsTruncateTable() {
        return false;
    }
}
