package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;

public class PageInfo implements Serializable {
    private String end_cursor;
    private boolean has_next_page;

    public boolean isHas_next_page() {
        return this.has_next_page;
    }

    public void setHas_next_page(boolean z) {
        this.has_next_page = z;
    }

    public String getEnd_cursor() {
        return this.end_cursor;
    }

    public void setEnd_cursor(String str) {
        this.end_cursor = str;
    }
}
