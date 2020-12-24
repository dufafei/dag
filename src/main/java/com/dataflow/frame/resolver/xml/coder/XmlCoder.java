package com.dataflow.frame.resolver.xml.coder;

import com.dataflow.frame.meta.GraphMeta;

public interface XmlCoder {

    /**
     * 将xml转为meta
     */
    GraphMeta decode(String xml) throws Exception;

    /**
     * 将meta转为xml
     */
    String encode(GraphMeta meta) throws Exception;
}
