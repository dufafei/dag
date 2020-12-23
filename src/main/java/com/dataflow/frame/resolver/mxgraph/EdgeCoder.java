package com.dataflow.frame.resolver.mxgraph;

import com.dataflow.frame.meta.EdgeMeta;
import com.mxgraph.model.mxCell;
import org.w3c.dom.Element;

public interface EdgeCoder {

    Element encodeHop(EdgeMeta edgeMeta) throws Exception;

    void decodeHop(EdgeMeta edgeMeta, mxCell cell) throws Exception;
}
