package com.dataflow.frame.resolver.mxgraph.coder;

import com.mxgraph.model.mxCell;
import org.w3c.dom.Element;

public interface VertexCoder {

    void encodeMx(Element element) throws Exception;
    void decodeMx(mxCell cell) throws Exception;
}
