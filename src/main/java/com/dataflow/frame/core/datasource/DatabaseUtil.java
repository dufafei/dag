package com.dataflow.frame.core.datasource;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 272558733@qq.com
 * @Date: 2021/1/19 1:32
 * @Description:
 */
public class DatabaseUtil {

    public static final String DATASOURCE = "datasource";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String DB = "db";
    public static final String TABLE = "table";

    private DatabaseUtil() {}

    public static Map<String, String> resolverUrl(String jdbcUrl) {
        Map<String, String> map = new HashMap<>();
        int pos1 = jdbcUrl.indexOf(':', 5);
        if(!jdbcUrl.startsWith("jdbc:") || pos1 == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        } else {
            String id = jdbcUrl.substring(5, pos1);
            map.put(DATASOURCE, id);
            int pos2 = jdbcUrl.indexOf("/", pos1 + 3);
            String hostAndPort = jdbcUrl.substring(pos1 + 3, pos2);
            if(hostAndPort.contains(":")) {
                String[] arr = hostAndPort.split(":");
                map.put(HOST, arr[0]);
                map.put(PORT, arr[1]);
            } else {
                map.put(HOST, hostAndPort);
                DatabaseMeta databaseMeta = new DatabaseMeta(id);
                map.put(PORT, String.valueOf(databaseMeta.getDefaultPort()));
            }
            int pos3 = jdbcUrl.indexOf("?", pos2);
            String db = jdbcUrl.substring(pos2 + 1, pos3);
            map.put(DB, db);
        }
        return map;
    }

    public static List<String> resolverSql(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        return tablesNamesFinder.getTableList(statement);
    }
}
