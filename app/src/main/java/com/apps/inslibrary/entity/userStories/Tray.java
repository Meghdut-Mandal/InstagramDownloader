package com.apps.inslibrary.entity.userStories;

public class Tray {
    private CoverMedia cover_media;
    private String created_at;

    private String id;
    private long latest_reel_media;
    private int media_count;
    private int prefetch_count;
    private String title;
    private StoriesUser user;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public long getLatest_reel_media() {
        return this.latest_reel_media;
    }

    public void setLatest_reel_media(long j) {
        this.latest_reel_media = j;
    }

    public CoverMedia getCover_media() {
        return this.cover_media;
    }

    public void setCover_media(CoverMedia coverMedia) {
        this.cover_media = coverMedia;
    }

    public StoriesUser getUser() {
        return this.user;
    }

    public void setUser(StoriesUser storiesUser) {
        this.user = storiesUser;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String str) {
        this.created_at = str;
    }

    public int getPrefetch_count() {
        return this.prefetch_count;
    }

    public void setPrefetch_count(int i) {
        this.prefetch_count = i;
    }

    public int getMedia_count() {
        return this.media_count;
    }

    public void setMedia_count(int i) {
        this.media_count = i;
    }
}
