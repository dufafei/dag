package com.dataflow.frame.core.database;

import com.dataflow.frame.core.plugin.BasePluginType;
import java.lang.annotation.Annotation;

public class DatasourcePluginType extends BasePluginType {

    private static DatasourcePluginType datasourcePluginType;

    private DatasourcePluginType() {
        super("datasource", "datasource", Datasource.class);
    }

    public static DatasourcePluginType getInstance() {
        if (datasourcePluginType == null) {
            datasourcePluginType = new DatasourcePluginType();
        }
        return datasourcePluginType;
    }

    @Override
    protected String extractID(Annotation annotation) {
        return ((Datasource) annotation).type();
    }

    @Override
    protected String extractName(Annotation annotation) {
        return null;
    }

    @Override
    protected String extractDesc(Annotation annotation) {
        return ((Datasource) annotation).typeDescription();
    }

    @Override
    protected String extractIcon(Annotation annotation) {
        return null;
    }

    @Override
    protected String extractCategory(Annotation annotation) {
        return null;
    }
}
