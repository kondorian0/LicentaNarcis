package com.narcis.neamtiu.licentanarcis.models;

import java.util.ArrayList;

public class EventContent {


    public String contentNote, typeOfEvent, eventDate, eventTime;
    public ArrayList content;

    public EventContent(){

    }

    public EventContent(String typeOfEvent,String eventDate,String eventTime, ArrayList content){
        this.typeOfEvent = typeOfEvent;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.content = content;
    }

    public EventContent(String contentNote){
        this.contentNote = contentNote;
    }
}
