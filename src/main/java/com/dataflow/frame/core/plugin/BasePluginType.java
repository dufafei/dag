package com.dataflow.frame.core.plugin;

import com.dataflow.frame.consts.Const;
import com.dataflow.frame.core.loader.DataFlowLoader;
import com.dataflow.frame.core.plugin.folder.PluginFolder;
import com.dataflow.frame.core.plugin.folder.PluginFolderInterface;
import com.dataflow.frame.core.plugin.registry.PluginRegistry;
import org.apache.commons.vfs2.FileObject;
import com.dataflow.frame.core.vfs2.FileVFS2;
import org.scannotation.AnnotationDB;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;

public abstract class BasePluginType implements PluginTypeInterface {

    protected PluginRegistry registry = PluginRegistry.getInstance();
    protected String id;
    protected String name;
    protected Class<? extends java.lang.annotation.Annotation> pluginType;
    protected List<PluginFolderInterface> pluginFolders;

    public BasePluginType(String id, String name, Class<? extends Annotation> pluginType) {
        this.id = id;
        this.name = name;
        this.pluginType = pluginType;
        this.pluginFolders = new ArrayList<>();
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public Class<? extends Annotation> getPluginType() { return pluginType; }
    public List<PluginFolderInterface> getPluginFolders() { return this.pluginFolders; }

    public void searchPlugins() throws Exception {
        for (PluginFolderInterface pluginFolder : getPluginFolders()) {
            FileObject[] fileObjectList = pluginFolder.findJarFiles(false);
            if(fileObjectList != null) {
                for (FileObject fileObject : fileObjectList) {

                    List<FileObject> fileObjects = getFileObjects(fileObject);
                    List<String> fileNames = getFileNames(fileObjects);
                    List<URL> fileUrls = getFileUrls(fileObjects);
                    URL folderUrl = fileObject.getParent().getURL();
                    URL fileUrl = fileObject.getURL();

                    AnnotationDB annotationDB = new AnnotationDB();
                    annotationDB.scanArchives(fileUrl);
                    Set<String> impls = annotationDB.getAnnotationIndex().get(pluginType.getName());

                    for (String imp : impls) {
                        try {
                            ClassLoader classLoader = getClass().getClassLoader();
                            URLClassLoader urlClassLoader = new DataFlowLoader(fileUrls.toArray(new URL[0]), classLoader);
                            Class<?> clazz = urlClassLoader.loadClass(imp);

                            if (clazz == null) {
                                throw new Exception("Unable to load class: " + imp);
                            }

                            Annotation annotation = clazz.getAnnotation(pluginType);
                            handlePluginAnnotation(
                                    clazz, urlClassLoader, annotation, fileNames, false, folderUrl
                            );
                        } catch (Exception e) {
                            throw new Exception(
                                    "Unexpected error registering jar plugin file: " + fileUrl, e
                            );
                        }
                    }
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
        String libFolder = file.getParent() + "/lib";
        if (new File(libFolder).exists()) {
            PluginFolder pluginLibFolder = new PluginFolder(libFolder);
            FileObject[] jarFiles = pluginLibFolder.findJarFiles(true);
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
                                       URLClassLoader urlClassLoader,
                                       Annotation annotation,
                                       List<String> libraries,
                                       boolean nativePlugin,
                                       URL pluginFolder) throws Exception {
        String id = extractID(annotation);
        String name = extractName(annotation);
        String desc = extractDesc(annotation);
        String category = extractCategory(annotation);
        String icon = extractIcon(annotation);
        Map<String, String> extensionOptions = new HashMap<>();
        addExtraClasses(annotation, extensionOptions);
        PluginInterface plugin = new Plugin(id, name, desc, category,
                urlClassLoader, icon, clazz.getName(),
                libraries, nativePlugin, extensionOptions);
        registry.registerPlugin(this.getClass(), plugin);
    }

    protected void addExtraClasses(Annotation annotation, Map<String, String> extensionOptions) {
        System.out.println(annotation);
        System.out.println(extensionOptions);
    }

    protected abstract String extractID(java.lang.annotation.Annotation annotation);

    protected abstract String extractName(java.lang.annotation.Annotation annotation);

    protected abstract String extractDesc(java.lang.annotation.Annotation annotation);

    protected abstract String extractCategory(java.lang.annotation.Annotation annotation);

    protected abstract String extractIcon(java.lang.annotation.Annotation annotation);
}
