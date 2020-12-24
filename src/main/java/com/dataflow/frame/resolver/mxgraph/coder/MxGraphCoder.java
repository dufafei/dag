package com.dataflow.frame.resolver.mxgraph.coder;

import com.dataflow.frame.meta.FlowMeta;
import com.dataflow.frame.resolver.xml.coder.XmlCoder;
import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 基于前端mxGraph解析(xml格式)
 */
public abstract class MxGraphCoder implements XmlCoder {

    @Override
    public FlowMeta decode(String xml) throws Exception {
        mxGraph graph = new mxGraph();
        mxCodec code = new mxCodec();
        Document doc = mxXmlUtils.parseXml(xml);
        Element e = doc.getDocumentElement();
        code.decode(e, graph.getModel());
        return decode(graph);
    }

    public abstract FlowMeta decode(mxGraph graph) throws Exception;

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

    public abstract void encode(FlowMeta meta, mxGraph graph) throws Exception;
}
