package com.dataflow.frame.core.row;

public class ValueMeta implements ValueMetaInterface {

    private String name;

    public ValueMeta(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
