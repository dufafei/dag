package com.dataflow.frame.core.plugin;

import com.dataflow.frame.consts.Const;
import com.dataflow.frame.core.plugin.folder.PluginFolder;
import com.dataflow.frame.core.plugin.folder.PluginFolderInterface;
import com.dataflow.frame.core.plugin.registry.PluginRegistry;
import com.dataflow.frame.core.plugin.registry.PluginScanner;
import org.apache.commons.vfs2.FileObject;
import com.dataflow.frame.core.annonation.PluginScan;
import com.dataflow.frame.core.vfs2.FileAnnotationPlugin;
import com.dataflow.frame.core.vfs2.FileCache;
import com.dataflow.frame.core.vfs2.FileVFS2;
import org.scannotation.AnnotationDB;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;

/**
 * 插件类型定义的基类
 */
public abstract class BasePluginType implements PluginTypeInterface {

    protected PluginRegistry registry = PluginRegistry.getInstance();

    protected Class<? extends java.lang.annotation.Annotation> pluginType;
    protected List<PluginFolderInterface> pluginFolders;
    protected boolean searchLibDir;
    protected String id;
    protected String name;

    private BasePluginType(Class<? extends Annotation> pluginType) {
        this.pluginType = pluginType;
        this.pluginFolders = new ArrayList<>();
    }

    public BasePluginType(Class<? extends Annotation> pluginType, String id, String name) {
        this(pluginType);
        this.id = id;
        this.name = name;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public List<PluginFolderInterface> getPluginFolders() { return this.pluginFolders; }

    /**
     * 搜索本类型下的所有插件。
     */
    public void searchPlugins() throws Exception {
        registerNatives(); // classpath搜索
        registerPluginJars(); // 指定目录下的jar搜索
    }

    private void registerNatives() throws Exception {
        PluginScan pluginScan = getClass().getAnnotation(PluginScan.class);
        if(pluginScan != null) {
            String packageName = pluginScan.basePackageName();
            PluginScanner pluginScanner = new PluginScanner();
            pluginScanner.scanArchives(packageName, pluginType);
            List<Class<?>> classes = pluginScanner.getClasses();
            for (Class<?> clazz: classes) {
                Annotation annotation = clazz.getAnnotation(pluginType);
                handlePluginAnnotation(clazz, null, annotation,
                        null, true, null);
            }
        } else {
            throw new RuntimeException("missing a annonation: PluginScan");
        }
    }

    private List<FileAnnotationPlugin> findAnnotatedClassFiles(String annotationClassName) throws Exception {
        FileCache fileCache = FileCache.getInstance();
        List<FileAnnotationPlugin> classFiles = new ArrayList<>();
        for (PluginFolderInterface pluginFolder : getPluginFolders()) {
            FileObject[] fileObjects = fileCache.getFileObjects(pluginFolder);
            if (fileObjects != null) {
                for (FileObject fileObject : fileObjects) {
                    AnnotationDB annotationDB = fileCache.getAnnotationDB(fileObject);
                    Set<String> impls = annotationDB.getAnnotationIndex().get(annotationClassName);
                    if (impls != null) {
                        for (String fil : impls) {
                            classFiles.add(
                                    new FileAnnotationPlugin(
                                            fileObject.getParent().getURL(),
                                            fileObject.getURL(),
                                            fil
                                    )
                            );
                        }
                    }
                }
            }
        }
        return classFiles;
    }

    private void registerPluginJars() throws Exception {
        List<FileAnnotationPlugin> fileAnnotationPluginList = findAnnotatedClassFiles(pluginType.getName());
        for (FileAnnotationPlugin fileAnnotationPlugin: fileAnnotationPluginList) {
            URLClassLoader urlClassLoader = createUrlClassLoader(fileAnnotationPlugin.getJarFile());
            try {
                Class<?> clazz = urlClassLoader.loadClass(fileAnnotationPlugin.getClassName());
                if (clazz == null) {
                    throw new Exception("Unable to load class: " + fileAnnotationPlugin.getClassName());
                }
                List<String> libraries = new ArrayList<>();
                Annotation annotation;
                try {
                    annotation = clazz.getAnnotation(pluginType);
                    String fileName = URLDecoder.decode(fileAnnotationPlugin.getJarFile().getFile(), "UTF-8");
                    libraries.add(fileName);
                    FileObject fileObject = FileVFS2.getFileObject(fileName);
                    // 添加lib目录
                    FileObject parentFolder = fileObject.getParent();
                    String parentFolderName = FileVFS2.getFilename(parentFolder);
                    String libFolderName;
                    if (parentFolderName.endsWith(Const.FILE_SEPARATOR + "lib")) {
                        libFolderName = parentFolderName;
                    } else {
                        libFolderName = parentFolderName + Const.FILE_SEPARATOR + "lib";
                    }
                    PluginFolder folder = new PluginFolder(libFolderName, searchLibDir);

                    FileObject[] jarFiles = folder.findJarFiles(true);
                    if (jarFiles != null) {
                        for (FileObject jarFile : jarFiles) {
                            String libFileName = FileVFS2.getFilename(jarFile);
                            // 如果插件本身在lib文件夹中，我们将忽略它
                            if (fileObject.equals(jarFile)) {
                                continue;
                            }
                            libraries.add(libFileName);
                        }
                    }
                } catch (Exception e) {
                    throw new Exception(
                            "Unexpected error loading class " + clazz.getName() + " of plugin type: " + pluginType, e
                    );
                }
                handlePluginAnnotation(
                        clazz, urlClassLoader, annotation, libraries, false, fileAnnotationPlugin.getPluginFolder()
                );
            } catch (Exception e) {
                throw new Exception(
                        "Unexpected error registering jar plugin file: " + fileAnnotationPlugin.getJarFile(), e
                );
            }
        }
    }

    private URLClassLoader createUrlClassLoader(URL jarFileUrl) throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.add(jarFileUrl);
        String libFolderName = new File(URLDecoder.decode(jarFileUrl.getFile(), "UTF-8")).getParent() + "/lib";
        if (new File(libFolderName).exists()) {
            PluginFolder pluginLibFolder = new PluginFolder(libFolderName , searchLibDir);
            FileObject[] libFiles = pluginLibFolder.findJarFiles(true);
            for (FileObject libFile : libFiles) {
                urls.add(libFile.getURL());
            }
        }
        return new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
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
        /*System.out.println(annotation);
        System.out.println(extensionOptions);*/
    }

    protected abstract String extractID(java.lang.annotation.Annotation annotation);

    protected abstract String extractName(java.lang.annotation.Annotation annotation);

    protected abstract String extractDesc(java.lang.annotation.Annotation annotation);

    protected abstract String extractCategory(java.lang.annotation.Annotation annotation);

    protected abstract String extractIcon(java.lang.annotation.Annotation annotation);
}
