package com.narcis.neamtiu.licentanarcis.util;

public class DataFirebase {

    private String id, typeofevent, dateEvent, timeEvent,
            title, description, location, note;
    int path;

    public DataFirebase(){

    }

    //Constructor for normal Event
    public DataFirebase(String dateEvent, String timeEvent, String title, String description, String location){
        this.dateEvent = dateEvent;
        this.timeEvent = timeEvent;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    //Constructor for Note
    public DataFirebase(String dateEvent, String timeEvent, String note){
        this.dateEvent = dateEvent;
        this.timeEvent = timeEvent;
        this.note = note;
    }

    //Constructor for Audio or Image
    public DataFirebase(String dateEvent, String timeEvent, int path){
        this.dateEvent = dateEvent;
        this.timeEvent = timeEvent;
        this.path = path;
    }

    public class ContentDataEvent {

        public ContentDataEvent(){

        }

        public ContentDataEvent(String typeofEvent, DataFirebase data){

        }

    }

}
