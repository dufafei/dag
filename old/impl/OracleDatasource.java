package com.dataflow.frame.core.datasource.impl;

import com.dataflow.frame.core.datasource.DatabaseDatasource;
import com.dataflow.frame.core.datasource.Datasource;
import com.dataflow.frame.core.datasource.DatabaseTypeAccess;
import org.apache.commons.lang3.StringUtils;

@Datasource(
        type = "Oracle",
        typeDescription = "Oracle数据源"
)
public class OracleDatasource extends DatabaseDatasource {

    @Override
    public DatabaseTypeAccess[] getAccessTypeList() {
        return new DatabaseTypeAccess[]{DatabaseTypeAccess.TYPE_ACCESS_JDBC};
    }

    @Override
    public Integer getDefaultDatabasePort() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return 1521;
        }
        return null;
    }

    @Override
    public String getDriverClass() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            return "oracle.jdbc.driver.OracleDriver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if (getAccessType() == DatabaseTypeAccess.TYPE_ACCESS_JDBC) {
            // the database name can be a SID (starting with :) or a Service (starting with /)
            // <host>:<port>/<service>
            // <host>:<port>:<SID>
            if (!StringUtils.isEmpty(getDb()) && (getDb().startsWith("/") || getDb().startsWith(":"))) {
                return "jdbc:oracle:thin:@" + getHost() + ":" + getPort() + getDb();
            } else if (StringUtils.isEmpty(getHost()) && (StringUtils.isEmpty(getPort()) || getPort().equals("-1"))) {
                // -1 when file based stored                                                                                                    // connection
                // support RAC with a self defined URL in databaseName like
                // (DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = host1-vip)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST =
                // host2-vip)(PORT = 1521))(LOAD_BALANCE = yes)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME =
                // db-service)(FAILOVER_MODE =(TYPE = SELECT)(METHOD = BASIC)(RETRIES = 180)(DELAY = 5))))
                // or
                // (DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)
                // (HOST=PRIMARY_NODE_HOSTNAME)(PORT=1521))
                // (ADDRESS=(PROTOCOL=TCP)(HOST=SECONDARY_NODE_HOSTNAME)(PORT=1521)))
                // (CONNECT_DATA=(SERVICE_NAME=DATABASE_SERVICENAME)))
                // or
                // (DESCRIPTION=(FAILOVER=ON)(ADDRESS_LIST=(LOAD_BALANCE=ON)
                // (ADDRESS=(PROTOCOL=TCP)(HOST=xxxxx)(PORT=1526))
                // (ADDRESS=(PROTOCOL=TCP)(HOST=xxxx)(PORT=1526)))(CONNECT_DATA=(SERVICE_NAME=somesid)))
                return "jdbc:oracle:thin:@" + getDb();
            } else {
                // by default we assume a SID
                return "jdbc:oracle:thin:@" + getHost() + ":" + getPort() + ":" + getDb();
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
