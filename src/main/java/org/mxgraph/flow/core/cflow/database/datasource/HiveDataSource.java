package org.mxgraph.flow.core.cflow.database.datasource;

import org.mxgraph.flow.core.cflow.database.BaseDatasource;
import org.mxgraph.flow.core.cflow.database.DatasourceMeta;

/**
 * 连接类型：Hadoop Hive 2
 * hive JDBC的连接方式，只能获取到hive执行的最终结果。
 * 如果想要获取执行过程中的状态，并且使用取消执行的功能。需要使用hiveServer2 thrift的方式。
 */
public class HiveDataSource extends BaseDatasource {

    @Override
    public int[] getAccessTypeList() { return new int[] {
            DatasourceMeta.TYPE_ACCESS_JDBC,
            DatasourceMeta.TYPE_ACCESS_SPARK_THRIFT
      };
    }

    @Override
    public int getDefaultDatabasePort() {
        if(getAccessType() == DatasourceMeta.TYPE_ACCESS_JDBC) {
            return 10000;
        }
        if(getAccessType() == DatasourceMeta.TYPE_ACCESS_SPARK_THRIFT) {
            return 9083;
        }
        return -1;
    }

    @Override
    public String getDriverClass() {
        if(getAccessType() == DatasourceMeta.TYPE_ACCESS_JDBC) {
            return "org.apache.hive.jdbc.HiveDriver";
        }
        return null;
    }

    @Override
    public String getURL() {
        if(getAccessType() == DatasourceMeta.TYPE_ACCESS_JDBC) {
            return "jdbc:hive2://" + host + ":" + port + "/";
        }
        if(getAccessType() == DatasourceMeta.TYPE_ACCESS_SPARK_THRIFT) {
            return "thrift://" + host + ":" + port + "/";
        }
        return null;
    }
}
