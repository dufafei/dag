package com.dataflow.frame.resolver.mxgraph.coder;

import com.dataflow.frame.core.meta.VertexMeta;
import com.mxgraph.model.mxCell;
import org.w3c.dom.Element;

public interface VertexCoder {

    Element encodeVertex(VertexMeta vertexMeta) throws Exception;

    void decodeVertex(VertexMeta vertexMeta, mxCell cell) throws Exception;
}
