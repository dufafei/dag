package org.mxgraph.flow.core.cflow.plugin.vfs2;

import org.mxgraph.flow.core.cflow.plugin.PluginFolderInterface;
import org.apache.commons.vfs2.FileObject;
import org.scannotation.AnnotationDB;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileCache {

    private static FileCache cache;
    private final Map<PluginFolderInterface, FileObject[]> folderMap;
    private final Map<FileObject, AnnotationDB> annotationMap;

    private FileCache() {
        annotationMap = new HashMap<>();
        folderMap = new HashMap<>();
    }

    public static FileCache getInstance() {
        if (cache == null) {
            cache = new FileCache();
        }
        return cache;
    }

    public AnnotationDB getAnnotationDB(FileObject fileObject) throws IOException {
        AnnotationDB result = annotationMap.get(fileObject);
        if (result == null) {
            result = new AnnotationDB();
            result.scanArchives(fileObject.getURL());
            annotationMap.put(fileObject, result);
        }
        return result;
    }

    public FileObject[] getFileObjects(PluginFolderInterface pluginFolderInterface) throws Exception {
        FileObject[] result = folderMap.get(pluginFolderInterface);
        if (result == null) {
            result = pluginFolderInterface.findJarFiles();
            folderMap.put(pluginFolderInterface, result);
        }
        return result;
    }

    public void clear() {
        annotationMap.clear();
        folderMap.clear();
    }
}
