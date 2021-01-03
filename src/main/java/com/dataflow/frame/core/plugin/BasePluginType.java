package com.dataflow.frame.core.plugin;

import com.dataflow.frame.core.loader.DataFlowLoader;
import com.dataflow.frame.core.vfs2.FileVFS2;
import com.dataflow.frame.core.vfs2.Folder;
import com.dataflow.frame.core.vfs2.FolderInterface;
import org.apache.commons.vfs2.FileObject;
import org.scannotation.AnnotationDB;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;

public abstract class BasePluginType implements PluginTypeInterface {

    private String id;
    protected String name;
    private Class<? extends java.lang.annotation.Annotation> pluginType;
    private List<FolderInterface> pluginFolders;

    public BasePluginType(String id, String name, Class<? extends Annotation> pluginType) {
        this.id = id;
        this.name = name;
        this.pluginType = pluginType;
        this.pluginFolders = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Class<? extends Annotation> getPluginType() { return pluginType; }
    public List<FolderInterface> getPluginFolders() { return pluginFolders; }

    public void searchPlugins() throws Exception {
        registerNatives(); // 本地加载
        registerJars(); // 从jar包中加载
    }

    public void registerNatives() throws Exception {}

    private void registerJars() throws Exception {
        for (FolderInterface pluginFolder: getPluginFolders()) {
            FileObject[] fileObjectList = pluginFolder.findJarFiles(false);
            if(fileObjectList != null) {
                for (FileObject fileObject: fileObjectList) {
                    List<FileObject> fileObjects = getFileObjects(fileObject);
                    List<URL> urls = getFileUrls(fileObjects);
                    List<String> libraries = getFileNames(fileObjects);
                    AnnotationDB annotationDB = new AnnotationDB();
                    DataFlowLoader urlClassLoader = null;
                    try {
                        annotationDB.scanArchives(fileObject.getURL());
                        Set<String> impls = annotationDB.getAnnotationIndex().get(pluginType.getName());
                        if(impls != null) {
                            for (String imp: impls) {
                                ClassLoader classLoader = getClass().getClassLoader();
                                urlClassLoader = new DataFlowLoader(urls.toArray(new URL[0]), classLoader);
                                Class<?> clazz = urlClassLoader.loadClass(imp);
                                handlePluginAnnotation(clazz, false, libraries, urlClassLoader);
                            }
                        }
                    } catch (Exception e) {
                        throw new Exception("Unable to read jar file: " + fileObject.getURL().toString(), e);
                    } /*finally {
                        // 使用到union组件时候会异常关闭 排查中
                        if (urlClassLoader != null) {
                            urlClassLoader.closeClassLoader();
                        }
                    }*/
                }
            }
        }
    }

    private List<FileObject> getFileObjects(FileObject fileObject) throws Exception {
        List<FileObject> fileObjects = new ArrayList<>();
        fileObjects.add(fileObject);
        String path = fileObject.getURL().getFile();
        path = URLDecoder.decode(path, "UTF-8");
        File file = new File(path);
        String libFolderPath = file.getParent() + "/lib";
        if (new File(libFolderPath).exists()) {
            Folder libFolder = new Folder(libFolderPath);
            FileObject[] jarFiles = libFolder.findJarFiles(true);
            fileObjects.addAll(Arrays.asList(jarFiles));
        }
        return fileObjects;
    }

    private List<String> getFileNames(List<FileObject> fileObjects) {
        List<String> fileNames = new ArrayList<>();
        for (FileObject fileObject: fileObjects) {
            fileNames.add(FileVFS2.getFilename(fileObject));
        }
        return fileNames;
    }

    private List<URL> getFileUrls(List<FileObject> fileObjects) throws Exception {
        List<URL> urls = new ArrayList<>();
        for (FileObject fileObject: fileObjects) {
            urls.add(fileObject.getURL());
        }
        return urls;
    }

    @Override
    public void handlePluginAnnotation(Class<?> clazz,
                                       boolean isNative,
                                       List<String> libraries,
                                       URLClassLoader urlClassLoader) throws Exception {
        Annotation annotation = clazz.getAnnotation(pluginType);
        String id = extractID(annotation);
        String name = extractName(annotation);
        String desc = extractDesc(annotation);
        String icon = extractIcon(annotation);
        String category = extractCategory(annotation);
        Map<String, Object> extensionOptions = new HashMap<>();
        addExtraMessages(annotation, extensionOptions);
        PluginInterface plugin = new Plugin(
                id, name, desc,
                icon, category,
                clazz.getName(), isNative,
                libraries, urlClassLoader,
                extensionOptions
        );
        PluginRegistry.getInstance().registerPlugin(getClass(), plugin);
    }

    protected abstract String extractID(java.lang.annotation.Annotation annotation);

    protected abstract String extractName(java.lang.annotation.Annotation annotation);

    protected abstract String extractDesc(java.lang.annotation.Annotation annotation);

    protected abstract String extractIcon(java.lang.annotation.Annotation annotation);

    protected abstract String extractCategory(java.lang.annotation.Annotation annotation);

    protected void addExtraMessages(Annotation annotation, Map<String, Object> extensionOptions) {}
}
