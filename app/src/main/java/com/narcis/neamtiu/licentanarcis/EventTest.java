package com.narcis.neamtiu.licentanarcis;

public class EventTest {

    private String title;
    private String details;
    private String date;

    public EventTest(String title, String details, String date) {
        this.title = title;
        this.details = details;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
