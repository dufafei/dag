import com.dataflow.frame.consts.Const;
import com.dataflow.frame.core.plugin.folder.PluginFolder;
import com.dataflow.frame.core.plugin.folder.PluginFolderInterface;
import com.dataflow.frame.core.vfs2.FileVFS2;
import org.apache.commons.vfs2.FileObject;

public class Test {

    public static void main(String[] args) throws Exception {

        PluginFolderInterface pluginFolder = new PluginFolder("E:\\workspace\\bgmp\\flow\\flow-engine-azkaban-plugin\\target");
        FileObject[] fileObjects = pluginFolder.findJarFiles(false);
        for (FileObject fileObject: fileObjects) {
            FileObject parentFolder = fileObject.getParent();
            System.out.println(parentFolder.getURL().toString());
            /*String parentFolderName = FileVFS2.getFilename(parentFolder);
            String libFolderName;
            if (parentFolderName.endsWith(Const.FILE_SEPARATOR + "lib")) {
                libFolderName = parentFolderName;
            } else {
                libFolderName = parentFolderName + Const.FILE_SEPARATOR + "lib";
            }
            System.out.println(libFolderName);*/
        }
    }

}
