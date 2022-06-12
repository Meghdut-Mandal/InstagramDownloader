package com.apps.inslibrary.entity.userStories;

import java.util.List;

public class ReelsStoriesMedia {
    private CoverMedia cover_media;

    /* renamed from: id */
    private String f193id;
    private List<StoriesItems> items;
    private long latest_reel_media;
    private int media_count;
    private int prefetch_count;
    private String title;
    private StoriesUser user;

    public String getId() {
        return this.f193id;
    }

    public void setId(String str) {
        this.f193id = str;
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

    public List<StoriesItems> getItems() {
        return this.items;
    }

    public void setItems(List<StoriesItems> list) {
        this.items = list;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
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
