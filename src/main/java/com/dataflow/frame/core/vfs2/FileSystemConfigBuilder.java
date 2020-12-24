package com.dataflow.frame.core.vfs2;

import org.apache.commons.vfs2.FileSystemOptions;
import java.io.IOException;

public interface FileSystemConfigBuilder {

    /**
     * 抽取FileSystemOptions参数名
     */
    public String parseParameterName(String parameter, String scheme);

    /**
     * 公开设置参数的通用方法
     */
    public void setParameter(FileSystemOptions opts,
                             String name,
                             String value,
                             String fullParameterName,
                             String vfsUrl) throws IOException;

}
