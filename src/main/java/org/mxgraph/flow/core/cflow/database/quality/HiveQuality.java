package org.mxgraph.flow.core.cflow.database.quality;

public class HiveQuality {

    //数据质量
    /*@Override
    public String getEnumerationValueCheckSql(String filter) {
        if(StringUtils.isBlank(filter)){
            return String.format("select count(*) from %s.${table} where ${field} not in (${list}) or ${field} is null",
                    getDb());
        } else {
            return String.format("select count(*) from %s.${table} where (${filter}) and (${field} not in (${list}) or ${field} is null)",
                    getDb());
        }
    }*/
}
