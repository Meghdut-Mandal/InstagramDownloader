package com.apps.inslibrary.entity.userStories;

import java.util.List;

public class UserStoriesResult {
    private boolean show_empty_state;
    private String status;
    private List<Tray> tray;

    public List<Tray> getTray() {
        return this.tray;
    }

    public void setTray(List<Tray> list) {
        this.tray = list;
    }

    public boolean isShow_empty_state() {
        return this.show_empty_state;
    }

    public void setShow_empty_state(boolean z) {
        this.show_empty_state = z;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }
}
