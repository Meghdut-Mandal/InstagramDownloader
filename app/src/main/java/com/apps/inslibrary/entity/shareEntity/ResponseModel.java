package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    private Graphql data;
    private Graphql graphql;
    private String status;

    public Graphql getGraphql() {
        return this.graphql;
    }

    public void setGraphql(Graphql graphql2) {
        this.graphql = graphql2;
    }

    public Graphql getData() {
        return this.data;
    }

    public void setData(Graphql graphql2) {
        this.data = graphql2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }
}
