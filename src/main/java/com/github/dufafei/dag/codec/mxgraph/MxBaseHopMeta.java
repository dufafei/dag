package com.github.dufafei.dag.codec.mxgraph;

import com.github.dufafei.dag.meta.BaseHopMeta;
import com.github.dufafei.dag.utils.JavaUtils;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class MxBaseHopMeta extends BaseHopMeta<MxBaseNodeMeta> implements MxCodec {

    public final static String HOP_TAG = "Hop";
    public final static String HOP_ELEMENT_CHECK = "check";

    public void encode(mxGraph graph, mxCell parent, mxCell source, mxCell target) throws Exception {
        Document doc = mxDomUtils.createDocument();
        Element e = doc.createElement(HOP_TAG);
        boolean enabled = getEnabled() == null ? true : getEnabled();
        e.setAttribute(HOP_ELEMENT_CHECK, Boolean.toString(enabled));
        encodeElement(e);
        graph.insertEdge(parent, null, e, source, target, null);
    }

    public void decode(mxCell cell) throws Exception {
        String enabled = JavaUtils.getDefault(cell.getAttribute(HOP_ELEMENT_CHECK), "true");
        setEnabled(Boolean.parseBoolean(enabled));
        decodeCell(cell);
    }
}
