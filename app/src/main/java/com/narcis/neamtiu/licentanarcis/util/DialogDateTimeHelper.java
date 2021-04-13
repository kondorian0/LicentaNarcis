package com.narcis.neamtiu.licentanarcis.util;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.narcis.neamtiu.licentanarcis.firestore.FirestoreClass;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.models.MyDotSpan;

//////////////////// SINGLETON CLASS ////////////////////

public class DialogDateTimeHelper extends AppCompatActivity {

    private static DialogDateTimeHelper mInstance = null;

    private String EVENT_TYPE;

    private EditText mTitle;
    private EditText mDescription;
    private EditText mNote;

    private TextView mLocation;

    private String mImagePath;
    private String mRecordPath;

    private FirestoreClass firestoreClass = FirestoreClass.getInstance();
    private String userID = firestoreClass.getCurrentUserID();

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

    ////////////////////Note///////////////////////
    public void setNote(EditText mNote) {
        this.mNote = mNote;
    }

    ////////////////////DRAW///////////////////////
    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    ////////////////////RECORD/////////////////////
    public void setmRecordPath(String mRecordPath) {
        this.mRecordPath = mRecordPath;
    }

    private DialogDateTimeHelper(Context context) {
    }

    public static DialogDateTimeHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DialogDateTimeHelper(context);
        }
        return mInstance;
    }

    public void onTimePicked(int hourOfDay, int minute) {
        if(hourOfDay < 10 && minute < 10) {
            timeEvent = "0" + hourOfDay + ":" + "0" + minute;
        } else if(hourOfDay < 10 && minute >= 10) {
            timeEvent = "0" + hourOfDay + ":" + minute;
        } else if(hourOfDay >= 10 && minute < 10) {
            timeEvent = hourOfDay + ":" + "0" + minute;
        } else if(hourOfDay >= 10 && minute >= 10) {
            timeEvent = hourOfDay + ":" + minute;
        }
        commitDataEvent();
    }

    public void onDatePicked(int year, int month, int day) {
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
    }

    void commitDataEvent() {
        if (dateEvent.isEmpty() || timeEvent.isEmpty()) {
            return;
        }

        switch (EVENT_TYPE) {
            case Constants.LOCATION_EVENT:
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                String location = mLocation.getText().toString();

                EventData locationEvent = new EventData(userID,Constants.LOCATION_EVENT,
                        dateEvent, timeEvent, title, description, location);

                firestoreClass.registerDataEvent(locationEvent);

                mTitle.getText().clear();
                mDescription.getText().clear();

                break;

            case Constants.NOTE_EVENT:
                String note = mNote.getText().toString();

                EventData noteEvent = new EventData(userID,Constants.NOTE_EVENT,
                        dateEvent, timeEvent, note);

                firestoreClass.registerDataEvent(noteEvent);

                mNote.getText().clear();

                break;
            case "Image":
//                myDb.insertDataImage(mImagePath);
//                myDb.insertDataTodoEvent(EVENT_TYPE, dateEvent, timeEvent);

//                mImagePath = new PaintFileHelper().saveImage();

                EventData imageEvent = new EventData(userID,"Image",
                        dateEvent, timeEvent, mImagePath);

                firestoreClass.registerDataEvent(imageEvent);

                break;

            case "Record":
//                myDb.insertDataAudio(mRecordPath);
//                myDb.insertDataTodoEvent(EVENT_TYPE, dateEvent, timeEvent);

                break;
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.SELECTED_DATE, dateEvent);
        setResult(Constants.RESULT_SUCCESS, intent);

        dateEvent = "";
        timeEvent = "";
    }
}