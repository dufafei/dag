package org.mxgraph.flow.core.cflow.database;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class BaseDatasource implements DatasourceInterface {

    protected String name; // 连接类型
    protected int accessType; // 连接方式
    protected String host; // ip
    protected String port; // 端口
    protected String db; // 数据库名称
    protected String user; // 用户
    protected String pass; // 密码
    protected String servername; // Informix only!
    protected String dataTablespace; // data storage location, For Oracle & perhaps others
    protected String indexTablespace; // index storage location, For Oracle & perhaps others
    protected Properties attributes; // 连接参数

    public BaseDatasource(){
        attributes = new Properties();
        if (getAccessTypeList() != null && getAccessTypeList().length > 0) {
            // 默认值
            accessType = getAccessTypeList()[0];
        }
    }

    @Override
    public String getName() { return name; }

    public Properties getAttributes() { return attributes; }

    @Override
    public int getAccessType() { return accessType; }

    @Override
    public void setAccessType(int accessType) { this.accessType = accessType; }

    @Override
    public String getHost() { return host; }

    public void setHost(String host) { this.host = host; }

    @Override
    public String getPort() { return port; }

    @Override
    public void setPort(String port) { this.port = port; }

    @Override
    public String getDb() { return db; }

    @Override
    public void setDb(String db) { this.db = db; }

    @Override
    public String getUser() { return user; }

    @Override
    public void setUser(String user) { this.user = user; }

    @Override
    public String getPass() { return pass; }

    @Override
    public void setPass(String pass) { this.pass = pass; }

    @Override
    public String getServername() { return servername; }

    @Override
    public void setServername(String servername) { this.servername = servername; }

    @Override
    public String getDataTablespace() { return dataTablespace; }

    @Override
    public void setDataTablespace(String dataTablespace) { this.dataTablespace = dataTablespace; }

    @Override
    public String getIndexTablespace() { return indexTablespace; }

    @Override
    public void setIndexTablespace(String indexTablespace) { this.indexTablespace = indexTablespace; }

    public String urlJoint(String prefix, String separator) {
        List<String> params = new ArrayList<>();
        for (Object o : attributes.keySet()) {
            String key = (String) o;
            String value = attributes.getProperty(key);
            params.add(key + "=" + value);
        }
        return prefix + StringUtils.join(params, separator);
    }

    public String urlJoint() {
        return urlJoint("?", "&");
    }
}
