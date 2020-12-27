package com.dataflow.frame.core.database;

import java.lang.annotation.*;

@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Datasource {

    String type();
    String typeDescription();
}
