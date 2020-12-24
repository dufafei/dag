package com.dataflow.frame.core.annonation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginScan {

    String basePackageName();
}
