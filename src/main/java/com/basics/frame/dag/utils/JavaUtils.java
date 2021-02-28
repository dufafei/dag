package com.basics.frame.dag.utils;

public class JavaUtils {

    private JavaUtils() {}

    public static String getDefault(String value, String defaultValue) {
        if(value == null) {
            return defaultValue;
        } else if(value.trim().equals("")) {
            return defaultValue;
        } else {
            return value;
        }
    }
}
