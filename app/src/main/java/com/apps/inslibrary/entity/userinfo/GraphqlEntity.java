package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;

public class GraphqlEntity implements Serializable {
    private GraphqlUser user;

    public GraphqlUser getUser() {
        return this.user;
    }

    public void setUser(GraphqlUser graphqlUser) {
        this.user = graphqlUser;
    }
}
