package com.dataflow.frame.core.datasource;

import com.dataflow.frame.core.datasource.impl.*;
import com.dataflow.frame.core.plugin.BasePluginType;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class DatasourcePluginType extends BasePluginType<DatasourceInterface> {

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
        return ((Datasource) annotation).id();
    }

    @Override
    protected String extractName(Annotation annotation) { return null; }

    @Override
    protected String extractDesc(Annotation annotation) {
        return ((Datasource) annotation).description();
    }

    @Override
    protected String extractIcon(Annotation annotation) {
        return null;
    }

    @Override
    protected String extractCategory(Annotation annotation) { return ((Datasource) annotation).category(); }

    @Override
    public List<Class<?>> registerNatives() throws Exception {
        List<Class<?>> list = new ArrayList<>();
        list.add(MysqlDatasource.class);
        // list.add(ClickHouseDatasource.class);
        // list.add(DmDatasource.class);
        // list.add(HiveDatasource.class);
        // list.add(OracleDatasource.class);
        // list.add(PostgreSqlDatasource.class);
        return list;
    }
}
