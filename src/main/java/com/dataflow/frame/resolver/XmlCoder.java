package com.dataflow.frame.resolver;

import com.dataflow.frame.meta.FlowMeta;

public interface XmlCoder {

    FlowMeta decode(String xml) throws Exception;

    String encode(FlowMeta meta) throws Exception;
}
