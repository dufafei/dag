package com.dataflow.frame.core.database.datasource;

import org.apache.commons.lang3.StringUtils;
import com.dataflow.frame.core.database.BaseDatasource;
import com.dataflow.frame.core.database.DatasourceMeta;

public class MysqlDatasource extends BaseDatasource {

    @Override
    public int[] getAccessTypeList() {
        return new int[]{DatasourceMeta.TYPE_ACCESS_JDBC, DatasourceMeta.TYPE_ACCESS_ODBC};
    }

    @Override
    public int getDefaultDatabasePort() {
        if (getAccessType() == DatasourceMeta.TYPE_ACCESS_JDBC) {
            return 3306;
        }
        return -1;
    }

    @Override
    public String getDriverClass() {
        if(getAccessType() == DatasourceMeta.TYPE_ACCESS_JDBC) {
            return "org.gjt.mm.mysql.Driver";
        }
        if(getAccessType() == DatasourceMeta.TYPE_ACCESS_ODBC) {
            return "sun.jdbc.odbc.JdbcOdbcDriver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if ( getAccessType() == DatasourceMeta.TYPE_ACCESS_ODBC ) {
            return "jdbc:odbc:" + db;
        } else {
            if (StringUtils.isBlank(port)) {
                return "jdbc:mysql://" + host + "/" + db;
            } else {
                return "jdbc:mysql://" + host + ":" + port + "/" + db;
            }
        }
    }
}
