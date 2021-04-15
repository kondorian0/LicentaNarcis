package com.narcis.neamtiu.licentanarcis.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.models.MyDotSpan;

public class EventHelper {
    private String EVENT_TYPE;

    private EditText mTitle;
    private EditText mDescription;

    private TextView mLocation;

    private String mImagePath;
    private Uri mRecordPath;


    public  String dateEvent = "";
    private String timeEvent = "";

    private MyDotSpan dotSpan = new MyDotSpan();

    public void setEVENT_TYPE(String EVENT_TYPE) {
        this.EVENT_TYPE = EVENT_TYPE;
    }

    /////////////////////Event/////////////////////
    public void setTitle(EditText mTitle) {
        this.mTitle = mTitle;
    }
    public void setDescription(EditText mDescription) {
        this.mDescription = mDescription;
    }
    public void setLocation(TextView mLocation) {
        this.mLocation = mLocation;
    }

    ////////////////////DRAW///////////////////////
    public void setmImageURL(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    ////////////////////RECORD/////////////////////
    public void setmRecordPath(Uri mRecordPath) {
        this.mRecordPath = mRecordPath;
    }

    private EventHelper(Context context) {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void commitDataEvent() {
        if (dateEvent.isEmpty() || timeEvent.isEmpty()) {
            return;
        }
        switch (EVENT_TYPE) {
            case Constants.LOCATION_EVENT:
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                String location = mLocation.getText().toString();
//
//                EventData locationEvent = new EventData(userID, Constants.LOCATION_EVENT,
//                        dateEvent, timeEvent, title, description, location);

//                firestoreManager.registerDataEvent(locationEvent);

                mTitle.getText().clear();
                mDescription.getText().clear();
                break;
            case Constants.DRAW_EVENT:
//
//                paintFileHelper.saveImage(); //save image in storage (device and cloud)
//
//                EventData drawEvent = new EventData(userID, Constants.DRAW_EVENT,
//                        dateEvent, timeEvent, paintFileHelper.getImageURL());
//                firestoreClass.registerDataEvent(drawEvent);
//
//                paintFileHelper.clear();
                break;
            case Constants.RECORD_EVENT:
//                myDb.insertDataAudio(mRecordPath);
//                myDb.insertDataTodoEvent(EVENT_TYPE, dateEvent, timeEvent);

//                EventData recordEvent = new EventData(userID, Constants.RECORD_EVENT,
//                        dateEvent, timeEvent, mRecordPath);
//
//                firestoreClass.registerDataEvent(recordEvent);
                break;
        }

//        Intent intent = new Intent();
//        intent.putExtra(Constants.SELECTED_DATE, dateEvent);
//        setResult(Constants.RESULT_SUCCESS, intent);

        dateEvent = "";
        timeEvent = "";
    }

    static public String formatTimePicked(int hourOfDay, int minute) {
       String timeEvent = new String();
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
        String dateEvent = new String();
        int valueOfMonth = month+1;  //Ugly hack TODO: Improve
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