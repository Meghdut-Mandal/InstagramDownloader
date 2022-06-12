package com.apps.inslibrary.entity.stroiesEntity;

import com.apps.inslibrary.entity.shareEntity.ShortcodeMediaOwner;
import java.io.Serializable;
import java.util.List;

public class ReelsMedia implements Serializable {

    /* renamed from: id */
    private long f188id;
    private List<ReelsMediaItems> items;
    private int media_count;
    private List<String> media_ids;
    private int prefetch_count;
    private ShortcodeMediaOwner user;

    public long getId() {
        return this.f188id;
    }

    public void setId(long j) {
        this.f188id = j;
    }

    public ShortcodeMediaOwner getUser() {
        return this.user;
    }

    public void setUser(ShortcodeMediaOwner shortcodeMediaOwner) {
        this.user = shortcodeMediaOwner;
    }

    public List<ReelsMediaItems> getItems() {
        return this.items;
    }

    public void setItems(List<ReelsMediaItems> list) {
        this.items = list;
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

    public List<String> getMedia_ids() {
        return this.media_ids;
    }

    public void setMedia_ids(List<String> list) {
        this.media_ids = list;
    }
}
