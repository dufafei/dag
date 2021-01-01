package com.dataflow.frame.core.row;

import java.util.List;

public interface RowMetaInterface {

    List<ValueMetaInterface> getValueMetaList();

    ValueMetaInterface getValueMeta(int index);

    void setValueMeta(int index, ValueMetaInterface valueMeta);

    void addValueMeta( ValueMetaInterface meta );

    void removeValueMeta(String name);

    void addRowMeta(RowMetaInterface rowMeta);

    ValueMetaInterface searchValueMeta(String name);
}
