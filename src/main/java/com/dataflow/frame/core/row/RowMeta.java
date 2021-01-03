package com.dataflow.frame.core.row;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class RowMeta implements RowMetaInterface {

    private List<ValueMetaInterface> valueMetaList;

    public RowMeta() { valueMetaList = new ArrayList<>(); }

    @Override
    public List<ValueMetaInterface> getValueMetaList() { return valueMetaList; }

    @Override
    public int size() { return valueMetaList.size(); }

    @Override
    public ValueMetaInterface getValueMeta(int index) {
        return valueMetaList.get(index);
    }

    @Override
    public void setValueMeta(int index, ValueMetaInterface valueMeta) { valueMetaList.set(index, valueMeta); }

    @Override
    public void addValueMeta(ValueMetaInterface meta) { valueMetaList.add(meta); }

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

    @Override
    public String toString() {
        List<String> names = new ArrayList<>();
        for (ValueMetaInterface valueMeta: getValueMetaList()) {
            names.add(valueMeta.getName());
        }
        return StringUtils.join(names, ",");
    }
}
