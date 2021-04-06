package com.narcis.neamtiu.licentanarcis.util;

import java.io.Serializable;

public class EventListData implements Serializable {

    private String title, description, date;
    private int imgId;

    public EventListData(String title, String description, String date, int imgId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}
