package com.dataflow.frame.consts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Const {


    /**
     * 操作系统上的文件系统文件分隔符
     */
    public static String FILE_SEPARATOR = System.getProperty("file.separator");

    public static String replace(String string, String repl, String with) {
        if (string != null && repl != null && with != null) {
            return string.replaceAll(Pattern.quote(repl), Matcher.quoteReplacement(with));
        } else {
            return null;
        }
    }

    /**
     *
     */
    public static boolean classIsOrExtends(Class<?> clazz, Class<?> superClass) {
        if (clazz.equals(Object.class)) {
            return false;
        }
        return clazz.equals(superClass) || classIsOrExtends(clazz.getSuperclass(), superClass);
    }
}
