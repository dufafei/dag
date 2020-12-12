package org.mxgraph.flow.core.edge;

import com.mxgraph.model.mxCell;
import org.w3c.dom.Element;

public interface EdgeCoder {

    Element encodeHop(EdgeMeta edgeMeta) throws Exception;

    void decodeHop(EdgeMeta edgeMeta, mxCell cell) throws Exception;
}
