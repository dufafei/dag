package com.dataflow.frame.core.vfs2;

import com.dataflow.frame.core.env.Variable;
import org.apache.commons.vfs2.FileSystemOptions;
import java.io.IOException;

public class FileSystemConfigFactory {

    private FileSystemConfigFactory() {}


    public static FileSystemConfigBuilder getConfigBuilder() {
        return null;
    }

    public static FileSystemOptions buildFsOptions(Variable varSpace,
                                                   FileSystemOptions sourceOptions,
                                                   String vfsFilename,
                                                   String scheme) throws IOException {
        if (varSpace == null || vfsFilename == null) {
            return null;
        }
        FileSystemConfigBuilder configBuilder = getConfigBuilder();
        FileSystemOptions fsOptions = (sourceOptions == null) ? new FileSystemOptions() : sourceOptions;
        // 变量列表
        String[] varList = varSpace.listVariables();
        for (String var : varList) {
            if (var.startsWith("vfs.")) {
                String param = configBuilder.parseParameterName(var, scheme);
                if ( param != null ) {
                    configBuilder.setParameter(fsOptions, param, varSpace.getVariable(var), var, vfsFilename);
                } else {
                    throw new IOException( "FileSystemConfig could not parse parameter: " + var );
                }
            }
        }
        return fsOptions;
    }
}
