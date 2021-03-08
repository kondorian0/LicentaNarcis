package com.narcis.neamtiu.licentanarcis.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;

public class DialogDateTimeHelper extends AppCompatActivity {

    private static DialogDateTimeHelper mInstance = null;

    private final static int RESULT_SUCCESS = 0;
    private final static String SELECTED_DATE = "SelectedDate";

    private String EVENT_TYPE;

    private EditText mTitle;
    private EditText mLocation;
    private EditText mDescription;

    private DatabaseHelper myDb;

    String date_from = "";
    String time_from = "";

    public void setEVENT_TYPE(String EVENT_TYPE) {
        this.EVENT_TYPE = EVENT_TYPE;
    }

    public void setTitle(EditText mTitle) {
        this.mTitle = mTitle;
    }
    public void setDescription(EditText mDescription) {
        this.mDescription = mDescription;
    }
    public void setLocation(EditText mLocation) {
        this.mLocation = mLocation;
    }

    private DialogDateTimeHelper(Context context) {
         myDb = new DatabaseHelper(context);
    }

    public static DialogDateTimeHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DialogDateTimeHelper(context);
        }
        return mInstance;
    }

    public void onTimePicked(int hourOfDay, int minute) {
        if(hourOfDay < 10 && minute < 10) {
            time_from = "0" + hourOfDay + ":" + "0" + minute;
        }
        else if(hourOfDay < 10 && minute >= 10) {
            time_from = "0" + hourOfDay + ":" + minute;
        }
        else if(hourOfDay >= 10 && minute < 10) {
            time_from = hourOfDay + ":" + "0" + minute;
        }
        else if(hourOfDay >= 10 && minute >= 10) {
            time_from = hourOfDay + ":" + minute;
        }
//            time_from = startTime;
        commitDataEvent();
    }

    public void onDatePicked(int year, int month, int day) {
        date_from = day + "/" + month + "/" + year;
        commitDataEvent();
    }

    void commitDataEvent() {
        if (date_from.isEmpty() || time_from.isEmpty()) {
            return;
        }
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        String location = mLocation.getText().toString();

        myDb.insertDataEvent(title,description,location);

        switch (EVENT_TYPE) {
            case "Event":
            case "Note":
            case "Image":
            case "Record":
        }

        myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);

        mTitle.getText().clear();
        mDescription.getText().clear();
        mLocation.getText().clear();

        Intent intent = new Intent();
        intent.putExtra(SELECTED_DATE, date_from);
        setResult(RESULT_SUCCESS, intent);

        date_from = "";
        time_from = "";

        finish();

        Log.i("DATASAVED", "/////////////---------------Data saved---------------/////////////");
    }
}