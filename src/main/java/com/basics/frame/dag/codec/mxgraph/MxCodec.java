package com.basics.frame.dag.codec.mxgraph;

import com.mxgraph.model.mxCell;
import org.w3c.dom.Element;

public interface MxCodec {

    void encodeElement(Element e) throws Exception;

    void decodeCell(mxCell cell) throws Exception;
}
