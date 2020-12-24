package com.dataflow.frame.core.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义类加载器
 */
public class DataFlowLoader extends URLClassLoader {

    private String name;

    public DataFlowLoader(URL[] url, ClassLoader classLoader) {
        super(url, classLoader);
    }

    public DataFlowLoader(URL[] url, ClassLoader classLoader, String name) {
        this(url, classLoader);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected Class<?> loadClassFromThisLoader(String arg0, boolean arg1) throws ClassNotFoundException {
        Class<?> clz = null;
        if ((clz = findLoadedClass(arg0)) != null) {
            if (arg1) {
                resolveClass(clz);
            }
            return clz;
        }
        if ((clz = findClass(arg0)) != null) {
            if (arg1) {
                resolveClass( clz );
            }
            return clz;
        }
        return null;
    }

    protected Class<?> loadClassFromParent(String arg0, boolean arg1) throws ClassNotFoundException {
        Class<?> clz;
        if ((clz = getParent().loadClass(arg0)) != null) {
            if (arg1) {
                //
                resolveClass( clz );
            }
            return clz;
        }
        throw new ClassNotFoundException("Could not find :" + arg0);
    }

    @Override
    protected synchronized Class<?> loadClass(String arg0, boolean arg1) throws ClassNotFoundException {
        try {
            return loadClassFromThisLoader(arg0, arg1);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        return loadClassFromParent(arg0, arg1);
    }

    /**
     * 这个方法被设计用来清除windows中的类加载器文件锁。
     */
    public void closeClassLoader() {

    }
}
