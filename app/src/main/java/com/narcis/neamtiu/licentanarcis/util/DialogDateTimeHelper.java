package com.narcis.neamtiu.licentanarcis.util;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;

import static com.narcis.neamtiu.licentanarcis.EventActivity.EVENT_TYPE;

public class DialogDateTimeHelper extends AppCompatActivity{

    private final static int RESULT_SUCCESS = 0;
    private final static String SELECTED_DATE = "SelectedDate";

    private EditText mTitle = findViewById(R.id.event_title);
    private EditText mLocation = findViewById(R.id.event_location);
    private EditText mDescription = findViewById(R.id.event_description);

    private DatabaseHelper myDb = new DatabaseHelper(getApplicationContext());

    String date_from = "";
    String time_from = "";

    public void onTimePicked(int hourOfDay, int minute)
    {
        if(hourOfDay < 10 && minute < 10)
        {
            time_from = "0" + hourOfDay + ":" + "0" + minute;
        }
        else if(hourOfDay < 10 && minute >= 10)
        {
            time_from = "0" + hourOfDay + ":" + minute;
        }
        else if(hourOfDay >= 10 && minute < 10)
        {
            time_from = hourOfDay + ":" + "0" + minute;
        }
        else if(hourOfDay >= 10 && minute >= 10)
        {
            time_from = hourOfDay + ":" + minute;
        }
//            time_from = startTime;
        commitDataEvent();
    }

    public void onDatePicked(int year, int month, int day)
    {
        date_from = day + "/" + month + "/" + year;
        commitDataEvent();
    }

    void commitDataEvent()
    {
        if (date_from.isEmpty() || time_from.isEmpty())
        {
            return;
        }
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        String location = mLocation.getText().toString();

        myDb.insertDataEvent(title,description,location);
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
    }
}