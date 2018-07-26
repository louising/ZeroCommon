package com.zero.core.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "querys")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryConfiguration {

    @XmlElement(name = "query")
    private List<QueryConfig> queryList;

    public QueryConfiguration() {
    }

    public QueryConfiguration(List<QueryConfig> querys) {
        super();
        this.queryList = querys;
    }

    public List<QueryConfig> getQueryList() {
        return queryList;
    }

    public void setQueryList(List<QueryConfig> querys) {
        this.queryList = querys;
    }

    @Override
    public String toString() {
        return "QueryConfiguration [querys=" + queryList + "]";
    }
}
