package com.dataflow.frame.core.check;

/**
 * 步骤检查接口
 */
public interface CheckResultInterface {

    static int TYPE_RESULT_NONE = 0;

    static int TYPE_RESULT_OK = 1;

    static int TYPE_RESULT_COMMENT = 2;

    static int TYPE_RESULT_WARNING = 3;

    static int TYPE_RESULT_ERROR = 4;

    /**
     * @return The text of the check result.
     */
    String getText();

    /**
     * Sets the text for the check-result
     */
    void setText(String value);
}
