package com.dataflow.frame.core.env;

import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VariableSpace implements Variable {

    private Map<String, String> properties; // 存储变量
    private Variable parent; // 父变量
    private Map<String, String> injection; // 注入的变量
    private boolean initialized; // 是否注入

    public VariableSpace() {
        properties = new ConcurrentHashMap<>();
        parent = null;
        injection = null;
        initialized = false;
    }

    @Override
    public void initializeVariablesFrom(Variable parent) { }

    @Override
    public void copyVariablesFrom(Variable space) {
        if (space != null && this != space) {
            String[] variableNames = space.listVariables();
            for (String variableName : variableNames) {
                properties.put(variableName, space.getVariable(variableName));
            }
        }
    }

    @Override
    public void shareVariablesWith(Variable space) { }

    @Override
    public Variable getParentVariableSpace() {
        return parent;
    }

    @Override
    public void setParentVariableSpace(Variable parent) {
        this.parent = parent;
    }

    @Override
    public void setVariable(String variableName, String variableValue) {
        if (variableValue != null) {
            properties.put(variableName, variableValue);
        } else {
            properties.remove(variableName);
        }
    }

    @Override
    public String getVariable(String variableName, String defaultValue) {
        String var = properties.get(variableName);
        if ( var == null ) {
            return defaultValue;
        }
        return var;
    }

    @Override
    public String getVariable(String variableName) {
        return properties.get(variableName);
    }

    @Override
    public boolean getBooleanValueOfVariable(String variableName, boolean defaultValue) {
        String value = getVariable(variableName);
        if (StringUtils.isNotEmpty(value)) {
            if(value.equals("true")) return true;
            else if(value.equals("false")) return false;
            else return defaultValue;
        }
        return defaultValue;
    }

    @Override
    public String[] listVariables() {
        Set<String> keySet = properties.keySet();
        return keySet.toArray(new String[0]);
    }
}
