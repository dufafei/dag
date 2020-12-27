package com.dataflow.frame.core.datasource;

import java.lang.annotation.*;

@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Datasource {

    String type();
    String typeDescription();
}
