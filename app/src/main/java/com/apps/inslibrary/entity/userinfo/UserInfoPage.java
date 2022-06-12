package com.apps.inslibrary.entity.userinfo;

public class UserInfoPage {
    private String status;
    private GraphqlUser user;

    public GraphqlUser getUser() {
        return this.user;
    }

    public void setUser(GraphqlUser graphqlUser) {
        this.user = graphqlUser;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }
}
