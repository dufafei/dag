package org.mxgraph.flow.core.vertex;

import com.mxgraph.model.mxCell;
import org.w3c.dom.Element;

public interface VertexCoder {

    Element encodeVertex(VertexMeta vertexMeta) throws Exception;

    void decodeVertex(VertexMeta vertexMeta, mxCell cell) throws Exception;
}
