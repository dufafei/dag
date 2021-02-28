package com.basics.frame.dag.codec.mxgraph;

import com.basics.frame.dag.meta.BaseDagMeta;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxDomUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class MxBaseDagMeta extends BaseDagMeta<MxBaseNodeMeta, MxBaseHopMeta> implements MxCodec {

    public final static String ROOT_TAG = "Info";

    public void encode(mxCell parent) throws Exception {
        Document doc = mxDomUtils.createDocument();
        Element e = doc.createElement(ROOT_TAG);
        encodeElement(e);
        parent.setValue(e);
    }

    public void decode(mxCell parent) throws Exception {
        decodeCell(parent);
    }
}
