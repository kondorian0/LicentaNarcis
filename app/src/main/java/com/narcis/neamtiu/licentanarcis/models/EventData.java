package com.narcis.neamtiu.licentanarcis.models;

import java.util.ArrayList;

public class EventData {

    public String userId, eventContent, eventType, eventDate, eventTime;
    public String eventTitle, eventDescription, eventLocation;

    public EventData() {}

    //NoteEvent - ImageEvent - AudioEvent
    public EventData(String userId, String typeOfEvent, String eventDate,
                     String eventTime, String contentNote){
        this.userId = userId;
        this.eventType = typeOfEvent;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventContent = contentNote;
    }

    //LocationEvent
    public EventData(String userId, String typeOfEvent, String eventDate, String eventTime,
                     String title, String description, String location){
        this.userId = userId;
        this.eventType = typeOfEvent;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventTitle = title;
        this.eventDescription = description;
        this.eventLocation = location;
    }
}
