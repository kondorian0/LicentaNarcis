package com.narcis.neamtiu.licentanarcis.models;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.Constants;

import java.util.ArrayList;

public class EventData {
    public String userId, eventContent, eventType, eventDate, eventTime;
    public String eventTitle, eventDescription, eventLocation;

    int eventImageIcon;

    public EventData() {}

    //NoteEvent - ImageEvent - AudioEvent
    public EventData(String userId, String eventType, String eventDate,
                     String eventTime, String eventContent){
        this.userId = userId;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventContent = eventContent;
    }

    //LocationEvent
    public EventData(String userId, String eventType, String eventDate, String eventTime,
                     String eventTitle, String eventDescription, String eventLocation){
        this.userId = userId;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
    }

    public int getEventIcon(){
        switch (eventType){
            case Constants.NOTE_EVENT:
                eventImageIcon = R.drawable.ic_note_color;
                break;
            case Constants.LOCATION_EVENT:
                eventImageIcon = R.drawable.ic_location_color;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + eventType);
        }
        return eventImageIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTitle() {
        if(!eventType.equals(Constants.LOCATION_EVENT)){
            return eventContent;
        }
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        if(!eventType.equals(Constants.LOCATION_EVENT)){
            return " ";
        }
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}
