package com.github.dufafei.dag.codec.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public abstract class AbstractMxTransform {

    protected mxCell getDefaultParent(mxGraph graph) { return (mxCell) graph.getDefaultParent(); }

    protected Object[] getCells(mxGraph graph, mxCell parent) { return graph.getChildCells(parent); }
}
