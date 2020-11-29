package org.mxgraph.flow.core;

public interface GraphCode {

    /**
     * 将xml转为meta
     */
    AbstractMeta decode(String xml) throws Exception;

    /**
     * 将meta转为xml
     */
    String encode(AbstractMeta meta) throws Exception;
}
