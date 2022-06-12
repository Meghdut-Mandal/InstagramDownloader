package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;
import java.util.List;

public class ResponseModel2 implements Serializable {
    List<Items> items;

    public List<Items> getItems() {
        return this.items;
    }

    public void setItems(List<Items> list) {
        this.items = list;
    }
}
