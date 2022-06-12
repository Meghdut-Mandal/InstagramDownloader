package com.apps.inslibrary.entity.stories2;

import com.apps.inslibrary.entity.userStories.StoriesUser;
import java.util.List;

public class ReelsMediaStories2 {

    /* renamed from: id */
    private long f185id;
    private List<Stories2Item> items;
    private StoriesUser user;

    public long getId() {
        return this.f185id;
    }

    public void setId(long j) {
        this.f185id = j;
    }

    public List<Stories2Item> getItems() {
        return this.items;
    }

    public void setItems(List<Stories2Item> list) {
        this.items = list;
    }

    public StoriesUser getUser() {
        return this.user;
    }

    public void setUser(StoriesUser storiesUser) {
        this.user = storiesUser;
    }
}
