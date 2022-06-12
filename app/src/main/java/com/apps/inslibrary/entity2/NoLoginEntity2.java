package com.apps.inslibrary.entity2;

import java.util.List;

public class NoLoginEntity2 {
    private List<NoLoginRes2> medias;
    private NoLoginUser2 user;

    public NoLoginUser2 getUser() {
        return this.user;
    }

    public void setUser(NoLoginUser2 noLoginUser2) {
        this.user = noLoginUser2;
    }

    public List<NoLoginRes2> getMedias() {
        return this.medias;
    }

    public void setMedias(List<NoLoginRes2> list) {
        this.medias = list;
    }
}
