package com.dataflow.frame.core.check;

public class CheckResult implements CheckResultInterface {

    private int type;
    private String text;
    private CheckResultSourceInterface sourceMeta;

    public CheckResult(int type, String text, CheckResultSourceInterface sourceMeta) {
        this.type = type;
        this.text = text;
        this.sourceMeta = sourceMeta;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(String value) {

    }
}
