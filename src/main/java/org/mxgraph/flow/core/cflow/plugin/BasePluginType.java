package org.mxgraph.flow.core.cflow.plugin;

import org.mxgraph.flow.core.cflow.Const;
import org.apache.commons.vfs2.FileObject;
import org.mxgraph.flow.core.cflow.plugin.vfs2.FileAnnotationPlugin;
import org.mxgraph.flow.core.cflow.plugin.vfs2.FileCache;
import org.mxgraph.flow.core.cflow.plugin.vfs2.VFS2;
import org.mxgraph.flow.core.util.XMLHandler;
import org.scannotation.AnnotationDB;
import org.w3c.dom.Node;
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

    protected String id;
    protected String name;
    protected List<PluginFolderInterface> pluginFolders;
    protected PluginRegistry registry;
    protected boolean searchLibDir;
    Class<? extends java.lang.annotation.Annotation> pluginType;

    private BasePluginType(Class<? extends Annotation> pluginType) {
        this.pluginType = pluginType;
        this.pluginFolders = new ArrayList<>();
        this.registry = PluginRegistry.getInstance();
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
        // 本地搜索
        this.registerNatives();
        // jar搜索
        this.registerPluginJars();
    }

    /**
     * 加载源码下的组件
     */
    protected abstract void registerNatives() throws Exception;

    protected void registerPluginFromXmlResource(Node pluginNode) {
        try {
            String id = XMLHandler.getTagAttribute(pluginNode, "id");
            String name = XMLHandler.getTagValue(pluginNode, "name");
            String desc = XMLHandler.getTagValue(pluginNode, "description");
            String category = XMLHandler.getTagValue(pluginNode, "category");
            String icon = XMLHandler.getTagValue(pluginNode, "icon");
            String className = XMLHandler.getTagValue(pluginNode, "class_name");
            Map<String, String> extensionOptions = new HashMap<>();
            addExtraClasses(pluginNode, extensionOptions);
            PluginInterface pluginInterface = new Plugin(
                    this.getClass(), id, name, desc, category,
                    null, icon, className,
                    null, true, extensionOptions);
            this.registry.registerPlugin(this.getClass(), pluginInterface);
        } catch (Throwable throwable) {
            throw new RuntimeException("获取插件失败", throwable);
        }
    }

    protected void addExtraClasses(Node pluginNode, Map<String, String> extensionOptions) {
        System.out.println(pluginNode);
        System.out.println(extensionOptions);
    }

    protected void registerPluginJars() throws Exception {
        List<FileAnnotationPlugin> fileAnnotationPluginList = findAnnotatedClassFiles(pluginType.getName());
        for (FileAnnotationPlugin fileAnnotationPlugin: fileAnnotationPluginList) {
            URLClassLoader urlClassLoader = createUrlClassLoader(
                    fileAnnotationPlugin.getJarFile(), getClass().getClassLoader()
            );
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
                    FileObject fileObject = VFS2.getFileObject(fileName);
                    FileObject parentFolder = fileObject.getParent();
                    String parentFolderName = VFS2.getFilename(parentFolder);
                    String libFolderName;
                    if (parentFolderName.endsWith(Const.FILE_SEPARATOR + "lib")) {
                        libFolderName = parentFolderName;
                    } else {
                        libFolderName = parentFolderName + Const.FILE_SEPARATOR + "lib";
                    }
                    PluginFolder folder = new PluginFolder(
                            libFolderName, false, false, searchLibDir);
                    FileObject[] jarFiles = folder.findJarFiles(true);
                    if (jarFiles != null) {
                        for (FileObject jarFile : jarFiles) {
                            String libFileName = VFS2.getFilename(jarFile);
                            // 如果插件本身在lib文件夹中，我们将忽略它
                            if (fileObject.equals(jarFile)) {
                                continue;
                            }
                            libraries.add(libFileName);
                        }
                    }
                } catch (Exception e) {
                    throw new Exception(
                            "Unexpected error loading class " + clazz.getName() + " of plugin type: " + pluginType, e);
                }
                handlePluginAnnotation(clazz, urlClassLoader, annotation, libraries,
                        false, fileAnnotationPlugin.getPluginFolder());
            } catch (Exception e) {
                throw new Exception(
                        "Unexpected error registering jar plugin file: " + fileAnnotationPlugin.getJarFile(), e);
            } /*finally {
                ((CFlowURLClassLoader) urlClassLoader).closeClassLoader();
            }*/
        }
    }

    protected List<FileAnnotationPlugin> findAnnotatedClassFiles(String annotationClassName) throws Exception {
        FileCache fileCache = FileCache.getInstance();
        List<FileAnnotationPlugin> classFiles = new ArrayList<>();
        for (PluginFolderInterface pluginFolder : getPluginFolders()) {
            if (pluginFolder.isPluginAnnotationsFolder()) {
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
                                        fileObject.getURL(), fil)
                                );
                            }
                        }
                    }
                }
            }
        }
        return classFiles;
    }

    protected URLClassLoader createUrlClassLoader(URL jarFileUrl, ClassLoader classLoader) throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.add(jarFileUrl);
        String libFolderName = new File(URLDecoder.decode(jarFileUrl.getFile(), "UTF-8")).getParent() + "/lib";
        if (new File(libFolderName).exists()) {
            PluginFolder pluginLibFolder = new PluginFolder(
                    libFolderName, false, true, searchLibDir);
            FileObject[] libFiles = pluginLibFolder.findJarFiles(true);
            for (FileObject libFile : libFiles) {
                urls.add(libFile.getURL());
            }
        }
        return new URLClassLoader(urls.toArray(new URL[0]),
                Thread.currentThread().getContextClassLoader());
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
        PluginInterface plugin = new Plugin(
                this.getClass(), id, name, desc, category,
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
