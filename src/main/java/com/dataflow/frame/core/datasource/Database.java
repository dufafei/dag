package com.dataflow.frame.core.datasource;

import com.dataflow.frame.core.row.RowMeta;
import com.dataflow.frame.core.row.RowMetaInterface;
import com.dataflow.frame.core.row.ValueMeta;
import com.dataflow.frame.core.row.ValueMetaInterface;
import org.apache.commons.lang3.StringUtils;
import java.sql.*;

public class Database {

    private DatasourceMeta datasourceMeta;
    private Connection connection;

    public Database(DatasourceMeta datasourceMeta) {
        this.datasourceMeta = datasourceMeta;
    }

    public void connect() throws Exception {
        connect(null);
    }

    public void connect(String partitionId) throws Exception {
        //
        if(datasourceMeta.getAccessType() == DatasourceTypeAccess.TYPE_ACCESS_JDBC) {
            /*PluginInterface plugin =
                    PluginRegistry.getInstance().getPlugin(DatasourcePluginType.class, datasourceMeta.getDatasourceInterface());
            URLClassLoader urlClassLoader = plugin.getUrlClassLoader();
            Class<?> driverClass = urlClassLoader.loadClass(datasourceMeta.getDriverClass());*/
            Class<?> driverClass = Class.forName(datasourceMeta.getDriverClass());
            String url;
            if (datasourceMeta.isPartitioned() && !StringUtils.isEmpty(partitionId)) {
                url = datasourceMeta.getURL(partitionId);
            } else {
                url = datasourceMeta.getURL();
            }

            String clusterUsername = null;
            String clusterPassword = null;
            /*if ( datasourceMeta.isPartitioned() && !StringUtils.isEmpty( partitionId ) ) {
                // Get the cluster information...
                PartitionDatabaseMeta partition = datasourceMeta.getPartitionMeta( partitionId );
                if ( partition != null ) {
                    clusterUsername = partition.getUsername();
                    clusterPassword = Encr.decryptPasswordOptionallyEncrypted( partition.getPassword() );
                }
            }*/

            String username;
            String password;
            if (!StringUtils.isEmpty(clusterUsername)) {
                username = clusterUsername;
                password = clusterPassword;
            } else {
                username = datasourceMeta.getUsername();
                password = datasourceMeta.getPassword();
            }
            connection = DriverManager.getConnection(url, username, password);
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                //
            }
        }
    }

    public RowMetaInterface getQueryFieldsFromPreparedStatement(String sql) throws Exception {
        String newSql = datasourceMeta.stripCR(sql);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                newSql,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY)) {
            preparedStatement.setMaxRows(1);
            ResultSetMetaData metaData = preparedStatement.getMetaData();
            return getQueryFields(metaData, false, false);
        }
    }

    /**
     *
     * @param rm 要查询的结果集元数据
     * @param ignoreLength true，如果你想忽略长度(解决MySQL bug/问题)
     * @param lazyConversion 如果需要在可能的情况下启用延迟转换，则为true
     */
    private RowMetaInterface getQueryFields(ResultSetMetaData rm,
                                            boolean ignoreLength,
                                            boolean lazyConversion) throws Exception {
        RowMetaInterface rowMeta = new RowMeta();
        try {
            int nrCols = rm.getColumnCount();
            for (int i = 1; i <= nrCols; i++) {
                ValueMetaInterface value = new ValueMeta(rm.getColumnName(i));
                rowMeta.addValueMeta(value);
            }
            return rowMeta;
        } catch (SQLException ex) {
            throw new Exception("Error getting row information from database: ", ex);
        }
    }
}
