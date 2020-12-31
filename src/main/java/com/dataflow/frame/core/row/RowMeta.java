package com.dataflow.frame.core.row;

import java.util.List;

public class RowMeta implements RowMetaInterface {

    private List<ValueMetaInterface> valueMetaList;

    @Override
    public List<ValueMetaInterface> getValueMetaList() {
        return valueMetaList;
    }

    @Override
    public ValueMetaInterface getValueMeta(int index) {
        return valueMetaList.get(index);
    }

    @Override
    public void setValueMeta(int index, ValueMetaInterface valueMeta) {
        valueMetaList.set(index, valueMeta);
    }

    @Override
    public void addValueMeta(ValueMetaInterface meta) {
        valueMetaList.add(meta);
    }

    @Override
    public void addRowMeta(RowMetaInterface rowMeta) {
        // todo 需要确认排序情况
        valueMetaList.addAll(rowMeta.getValueMetaList());
    }

    @Override
    public ValueMetaInterface searchValueMeta(String name) {
        for (ValueMetaInterface valueMetaInterface: getValueMetaList()) {
            if(valueMetaInterface.getName().equals(name)) {
                return valueMetaInterface;
            }
        }
        return null;
    }
}
