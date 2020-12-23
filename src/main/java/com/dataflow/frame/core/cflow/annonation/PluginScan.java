package com.dataflow.frame.core.cflow.annonation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginScan {

    String basePackageName();
}
