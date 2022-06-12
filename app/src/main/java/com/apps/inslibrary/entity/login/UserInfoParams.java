package com.apps.inslibrary.entity.login;

public class UserInfoParams {
    private boolean include_chaining = false;
    private boolean include_highlight_reels = false;
    private boolean include_logged_out_extras = false;
    private boolean include_reel = true;
    private boolean include_related_profiles = false;
    private boolean include_suggested_users = false;
    private String user_id;

    public UserInfoParams() {
    }

    public UserInfoParams(String str) {
        this.user_id = str;
    }

    public String toString() {
        return "{user_id:" + this.user_id + ", include_chaining:" + this.include_chaining + ", include_reel:" + this.include_reel + ", include_suggested_users:" + this.include_suggested_users + ", include_logged_out_extras:" + this.include_logged_out_extras + ", include_highlight_reels:" + this.include_highlight_reels + ", include_related_profiles:" + this.include_related_profiles + "}";
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String str) {
        this.user_id = str;
    }

    public boolean isInclude_chaining() {
        return this.include_chaining;
    }

    public boolean isInclude_reel() {
        return this.include_reel;
    }

    public boolean isInclude_suggested_users() {
        return this.include_suggested_users;
    }

    public boolean isInclude_logged_out_extras() {
        return this.include_logged_out_extras;
    }

    public boolean isInclude_highlight_reels() {
        return this.include_highlight_reels;
    }

    public boolean isInclude_related_profiles() {
        return this.include_related_profiles;
    }
}
