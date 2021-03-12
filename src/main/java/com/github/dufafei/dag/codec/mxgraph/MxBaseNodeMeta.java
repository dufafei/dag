package com.github.dufafei.dag.codec.mxgraph;

import com.github.dufafei.dag.meta.BaseNodeMeta;
import com.github.dufafei.dag.utils.JavaUtils;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class MxBaseNodeMeta extends BaseNodeMeta implements MxCodec {

    public final static String NODE_TAG = "Node";
    public final static double NODE_WIDTH_SIZE = 37.0;
    public final static double NODE_HEIGHT_SIZE = 37.0;
    public final static String NODE_ELEMENT_LABEL = "label";
    public final static String NODE_ELEMENT_NAME = "name";
    public final static String NODE_ELEMENT_CHECK = "check";

    public mxCell encode(mxGraph graph, mxCell parent) throws Exception {
        Document doc = mxDomUtils.createDocument();
        Element e = doc.createElement(NODE_TAG);
        e.setAttribute(NODE_ELEMENT_LABEL, getId());
        e.setAttribute(NODE_ELEMENT_NAME, getName());
        boolean enabled = getEnabled() == null ? true : getEnabled();
        e.setAttribute(NODE_ELEMENT_CHECK, Boolean.toString(enabled));
        encodeElement(e);
        return (mxCell) graph.insertVertex(
                parent,
                null, e, 1, 1,
                NODE_WIDTH_SIZE, NODE_HEIGHT_SIZE, null
        );
    }

    public void decode(mxCell cell) throws Exception {
        setId(cell.getAttribute(NODE_ELEMENT_LABEL));
        setName(cell.getAttribute(NODE_ELEMENT_NAME));
        setSerialNumber(Integer.valueOf(cell.getId()));
        String enabled = JavaUtils.getDefault(cell.getAttribute(NODE_ELEMENT_CHECK), "true");
        setEnabled(Boolean.parseBoolean(enabled));
        decodeCell(cell);
    }
}
