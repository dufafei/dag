package com.dataflow.frame.core.datasource;

/**
 *
 * 数据源接口
 */
public interface DatasourceInterface {

    /**
     * 数据源的IP地址（多个逗号隔开）
     */
    String getHost();

    void setHost(String host);

    /**
     * 数据源的端口
     */
    Integer getPort();

    void setPort(Integer port);

    /**
     * 数据源的默认端口
     */
    Integer getDefaultPort();
}
