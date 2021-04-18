package com.narcis.neamtiu.licentanarcis.util;

public class EventHelper {
    private static String timeEvent = "";
    private static String dateEvent = "";

    static public String formatTimePicked(int hourOfDay, int minute) {
        if(hourOfDay < 10 && minute < 10) {
            timeEvent = "0" + hourOfDay + ":" + "0" + minute;
        } else if(hourOfDay < 10 && minute >= 10) {
            timeEvent = "0" + hourOfDay + ":" + minute;
        } else if(hourOfDay >= 10 && minute < 10) {
            timeEvent = hourOfDay + ":" + "0" + minute;
        } else if(hourOfDay >= 10 && minute >= 10) {
            timeEvent = hourOfDay + ":" + minute;
        }
        return timeEvent;
    }

    static public String formatDatePicked(int year, int month, int day) {
        int valueOfMonth = month+1;  //TODO: Improve
        if(day < 10 && month < 10) {
            dateEvent = "0" + day + "/" + "0" + valueOfMonth + "/" + year;
        } else if(day < 10 && month >= 10) {
            dateEvent = "0" + day + "/" + valueOfMonth + "/" + year;
        } else if(day >= 10 && month < 10) {
            dateEvent = day + "/" + "0" + valueOfMonth + "/" + year;
        } else if(day >= 10 && month >= 10) {
            dateEvent = day + "/" + valueOfMonth + "/" + year;
        }
        return dateEvent;
    }
}