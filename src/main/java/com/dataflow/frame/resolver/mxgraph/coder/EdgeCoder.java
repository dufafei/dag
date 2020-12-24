package com.dataflow.frame.resolver.mxgraph.coder;

import com.dataflow.frame.meta.EdgeMeta;
import com.mxgraph.model.mxCell;
import org.w3c.dom.Element;

public interface EdgeCoder {

    Element encodeEdge(EdgeMeta edgeMeta) throws Exception;

    void decodeEdge(EdgeMeta edgeMeta, mxCell cell) throws Exception;
}
