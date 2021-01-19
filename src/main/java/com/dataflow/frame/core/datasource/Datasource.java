package com.dataflow.frame.core.datasource;

import java.lang.annotation.*;

/**
 * 数据源类型
 * 1 数据库
 * 2 ftp
 * 3 sftp
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Datasource {

    String id();
    String category();
    String description();
}
