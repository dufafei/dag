package com.dataflow.frame.resolver.mxgraph;

import com.dataflow.frame.meta.FlowMeta;

public interface GraphCode {

    /**
     * 将xml转为meta
     */
    FlowMeta decode(String xml) throws Exception;

    /**
     * 将meta转为xml
     */
    String encode(FlowMeta meta) throws Exception;
}
