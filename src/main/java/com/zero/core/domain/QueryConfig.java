package com.zero.core.domain;

public class QueryConfig {
    private String queryName;
    private String dataSource;
    private String sql;

    public QueryConfig() {
    }

    public QueryConfig(String queryName, String dataSourceJndiName, String sql) {
        super();
        this.queryName = queryName;
        this.dataSource = dataSourceJndiName;
        this.sql = sql;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSourceJndiName) {
        this.dataSource = dataSourceJndiName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "QueryConfig [queryName=" + queryName + ", dataSource=" + dataSource + ", sql=" + sql + "]";
    }

}
