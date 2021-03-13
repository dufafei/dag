package com.github.dufafei.dag.codec.mxgraph;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class MxTransform extends AbstractMxTransform {

    public String encode(MxBaseDagMeta mxBaseDagMeta) throws Exception {
        mxGraph graph = new mxGraph();
        graph.getModel().beginUpdate();
        try {
            mxCell parent = getDefaultParent(graph);
            mxBaseDagMeta.encode(parent);
            Map<MxBaseNodeMeta, mxCell> cells = new HashMap<>();
            for (MxBaseNodeMeta node: mxBaseDagMeta.getNodes()) {
                mxCell cell = node.encode(graph, parent);
                cells.put(node, cell);
            }
            for (MxBaseHopMeta hop: mxBaseDagMeta.getHops()) {
                mxCell source = cells.get(hop.getSource());
                mxCell target = cells.get(hop.getTarget());
                hop.encode(graph, parent, source, target);
            }
        } finally {
            graph.getModel().endUpdate();
        }
        mxCodec codec = new mxCodec();
        Node node = codec.encode(graph.getModel());
        return mxUtils.getPrettyXml(node);
    }

    public void decode(MxBaseDagMeta mxBaseDagMeta, String xml) throws Exception {
        mxGraph graph = new mxGraph();
        mxCodec code = new mxCodec();
        Document doc = mxXmlUtils.parseXml(xml);
        Element e = doc.getDocumentElement();
        code.decode(e, graph.getModel());
        mxCell parent = getDefaultParent(graph);
        mxBaseDagMeta.decode(parent);
        Object[] objects = getCells(graph, parent);
        for (Object o: objects) {
            mxCell cell = (mxCell) o;
            if (cell.isVertex()) {
                MxBaseNodeMeta node = mxBaseDagMeta.getNode();
                node.decode(cell);
                mxBaseDagMeta.addNode(node);
            }
        }
        for (Object o: objects) {
            mxCell cell = (mxCell) o;
            if (cell.isEdge()) {
                MxBaseHopMeta hop = mxBaseDagMeta.getHop();
                hop.decode(cell);
                mxCell source = (mxCell) cell.getSource();
                mxCell target = (mxCell) cell.getTarget();
                for (MxBaseNodeMeta node: mxBaseDagMeta.getNodes()) {
                    String serialNumber = String.valueOf(node.getSerialNumber());
                    if (source.getId().equals(serialNumber)) {
                        hop.setSource(node);
                    }
                    if (target.getId().equals(serialNumber)) {
                        hop.setTarget(node);
                    }
                }
                mxBaseDagMeta.addHop(hop);
            }
        }
    }
}