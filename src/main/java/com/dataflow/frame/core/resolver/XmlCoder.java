package com.dataflow.frame.core.resolver;

import com.dataflow.frame.core.meta.FlowMeta;

public interface XmlCoder {

    FlowMeta decode(String xml) throws Exception;

    String encode(FlowMeta meta) throws Exception;
}
