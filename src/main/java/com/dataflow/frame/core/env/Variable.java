package com.dataflow.frame.core.env;

/**
 * 变量空间，用于管理全局变量
 */
public interface Variable {

    /**
     * 使用默认值初始化变量空间，复制父变量 (使用copyVariablesFrom())
     * 在此之后，应该插入“注入的”变量(injectVariables())
     */
    void initializeVariablesFrom(Variable parent);

    /**
     * 从另一个空间复制变量，不使用默认值初始化。
     */
    void copyVariablesFrom(Variable space);

    /**
     * 从另一个变量空间共享一个变量空间。这意味着该对象应该接管所使用的空间，例如参数
     */
    void shareVariablesWith( Variable space );

    /**
     * 获取变量空间的父元素。
     */
    Variable getParentVariableSpace();

    /**
     * 设置变量空间的父元素
     */
    void setParentVariableSpace(Variable parent);

    void setVariable(String variableName, String variableValue);

    String getVariable(String variableName, String defaultValue);

    String getVariable(String variableName);

    boolean getBooleanValueOfVariable(String variableName, boolean defaultValue);

    /**
     * 获取所有的变量名
     */
    String[] listVariables();
}
