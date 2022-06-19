package com.apps.inslibrary.entity.login;

public class LoginReel {
    private String __typename;
    private long expiring_at;
    private boolean has_pride_media;


    private String id;
    private int latest_reel_media;
    private ReelUser owner;
    private ReelUser user;

    public String get__typename() {
        return this.__typename;
    }

    public void set__typename(String str) {
        this.__typename = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public long getExpiring_at() {
        return this.expiring_at;
    }

    public void setExpiring_at(long j) {
        this.expiring_at = j;
    }

    public boolean isHas_pride_media() {
        return this.has_pride_media;
    }

    public void setHas_pride_media(boolean z) {
        this.has_pride_media = z;
    }

    public int getLatest_reel_media() {
        return this.latest_reel_media;
    }

    public void setLatest_reel_media(int i) {
        this.latest_reel_media = i;
    }

    public ReelUser getUser() {
        return this.user;
    }

    public void setUser(ReelUser reelUser) {
        this.user = reelUser;
    }

    public ReelUser getOwner() {
        return this.owner;
    }

    public void setOwner(ReelUser reelUser) {
        this.owner = reelUser;
    }
}
