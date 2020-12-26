package com.dataflow.frame.core.resolver;

import com.dataflow.frame.core.meta.FlowMeta;
import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class MxCoder implements XmlCoder {

    @Override
    public FlowMeta decode(String xml) throws Exception {
        mxGraph graph = new mxGraph();
        mxCodec code = new mxCodec();
        Document doc = mxXmlUtils.parseXml(xml);
        Element e = doc.getDocumentElement();
        code.decode(e, graph.getModel());
        return decode(graph);
    }

    protected abstract FlowMeta decode(mxGraph graph) throws Exception;

    @Override
    public String encode(FlowMeta meta) throws Exception {
        mxGraph graph = new mxGraph();
        graph.getModel().beginUpdate();
        try {
            encode(meta, graph);
        } finally {
            graph.getModel().endUpdate();
        }
        mxCodec codec = new mxCodec();
        Node node = codec.encode(graph.getModel());
        return mxUtils.getPrettyXml(node);
    }

    protected abstract void encode(FlowMeta meta, mxGraph graph) throws Exception;
}
