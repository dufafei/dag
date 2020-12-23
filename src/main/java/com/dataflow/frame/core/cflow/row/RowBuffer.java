package com.dataflow.frame.core.cflow.row;

import java.util.ArrayList;
import java.util.List;

public class RowBuffer {

    private RowMetaInterface rowMeta;
    private List<Object[]> buffer;

    public RowBuffer(RowMetaInterface rowMeta, List<Object[]> buffer) {
        this.rowMeta = rowMeta;
        this.buffer = buffer;
    }

    public RowBuffer(RowMetaInterface rowMeta) {
        this(rowMeta, new ArrayList<>());
    }

    public RowMetaInterface getRowMeta() {
        return this.rowMeta;
    }

    public void setRowMeta(RowMetaInterface rowMeta) {
        this.rowMeta = rowMeta;
    }

    public List<Object[]> getBuffer() {
        return this.buffer;
    }

    public void setBuffer(List<Object[]> buffer) {
        this.buffer = buffer;
    }
}
