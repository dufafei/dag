package org.mxgraph.flow.core.cflow.plugin;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * URLClassLoader继承ClassLoader,是ClassLoader 的扩展。
 * 主要区别是ClassLoader加载classpath下面的类，而URLClassLoader加载任意路径下的类
 */
public class PluginScanner {

    List<Class<?>> classes = new ArrayList<>();

    public List<Class<?>> getClasses() {
        return classes;
    }

    /**
     * 采用ClassLoader扫描
     */
    public <A extends Annotation> void scanArchives(String packageName, Class<A> annotationClass) throws Exception {
        packageName = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(packageName);
        if(url != null) {
            // 得到协议的名称
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                // 以文件的方式扫描整个包下的文件 并添加到集合中
                addClass(filePath, packageName, annotationClass);
            }
            if ("jar".equals(protocol)) {
                // 获取jar
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                addClass(entries, packageName, annotationClass);
            }
        }
    }

    /**
     * 遍历一个目录下
     */
    private <A extends Annotation> void addClass(String filePath,
                                                 String packageName,
                                                 Class<A> annotationClass) throws Exception {
        File[] files = new File(filePath).listFiles(File::isDirectory);
        if(files != null) {
            for (File file : files) {
                String dirName = file.getName();
                File[] files1 = file.listFiles(x -> (x.isFile() && x.getName().endsWith(".class")));
                if(files1 != null) {
                    for (File file1: files1) {
                        if (file1.isFile()) {
                            String fileName = file1.getName();
                            String className = fileName.substring(0, fileName.lastIndexOf("."));
                            if (!packageName.isEmpty()) {
                                packageName = packageName.replace("/", ".");
                                className = packageName + "." + dirName + "." + className;
                            }
                            doAddClass(className, annotationClass);
                        }
                    }
                }
            }
        }
    }

    private <A extends Annotation> void addClass(Enumeration<JarEntry> entries,
                                                 String packageName,
                                                 Class<A> annotationClass) throws Exception {
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            // 如果是以/开头的
            if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
            }
            // 如果前半部分和定义的包名相同
            if (name.startsWith(packageName)) {
                int idx = name.lastIndexOf('/');
                if (idx != -1) {
                    // 如果是一个.class文件 而且不是目录
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        // 去掉后面的".class" 获取真正的类名
                        String className = name.substring(0, name.length() - 6);
                        className = className.replace("/", ".");
                        doAddClass(className, annotationClass);
                    }
                }
            }
        }
    }

    private <A extends Annotation> void doAddClass(final String className,
                                                   Class<A> annotationClass) throws Exception {
        Class<?> clazz = Class.forName(className);
        if (clazz.getAnnotation(annotationClass) != null && classes.indexOf(clazz) < 0) {
            classes.add(clazz);
        }
    }
}
