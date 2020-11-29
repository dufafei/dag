package org.mxgraph.flow.core.vertex;

public enum Vertex {

    Start("start"), // 起始点
    End("end"), // 结束点
    ;

    private String desc;

    Vertex(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
