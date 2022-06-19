package com.apps.inslibrary.entity.userStories;

public class StoriesUser {
    private int follow_friction_type;
    private String full_name;
    private boolean is_private;
    private boolean is_verified;

    private long pk;
    private String profile_pic_id;
    private String profile_pic_url;
    private String username;

    public long getPk() {
        return this.pk;
    }

    public void setPk(long j) {
        this.pk = j;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String str) {
        this.full_name = str;
    }

    public boolean isIs_private() {
        return this.is_private;
    }

    public void setIs_private(boolean z) {
        this.is_private = z;
    }

    public String getProfile_pic_url() {
        return this.profile_pic_url;
    }

    public void setProfile_pic_url(String str) {
        this.profile_pic_url = str;
    }

    public String getProfile_pic_id() {
        return this.profile_pic_id;
    }

    public void setProfile_pic_id(String str) {
        this.profile_pic_id = str;
    }

    public boolean isIs_verified() {
        return this.is_verified;
    }

    public void setIs_verified(boolean z) {
        this.is_verified = z;
    }

    public int getFollow_friction_type() {
        return this.follow_friction_type;
    }

    public void setFollow_friction_type(int i) {
        this.follow_friction_type = i;
    }
}
