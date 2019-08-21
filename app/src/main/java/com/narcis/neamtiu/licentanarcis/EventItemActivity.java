package com.narcis.neamtiu.licentanarcis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.*;

import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class EventItemActivity extends AppCompatActivity  {

    class DialogDateTimeListener implements DialogDateTime.Listener {

        String date_from = "";
        String time_from = "";

        @Override
        public void onTimePicked(int hourOfDay, int minute) {

            if(hourOfDay < 10 && minute < 10){

                String startTime = "0" + hourOfDay + ":" + "0" + minute;
                time_from = startTime;

            }else if(hourOfDay < 10 && minute >= 10){

                String startTime =  "0" + hourOfDay + ":" + minute;
                time_from = startTime;

            }else if(hourOfDay >= 10 && minute < 10){

                String startTime = hourOfDay + ":" + "0" + minute;
                time_from = startTime;

            }else if(hourOfDay >= 10 && minute >= 10) {

                String startTime = hourOfDay + ":" + minute;
                time_from = startTime;

            }
//            time_from = startTime;

            commitData();
        }

        @Override
        public void onDatePicked(int year, int month, int day) {
            String startDate = day + "/" + month + "/" + year;

            date_from = startDate;

            commitData();
        }

        void commitData() {

            if (date_from.isEmpty() || time_from.isEmpty()){

                return;

            }

            String title = mTitle.getText().toString();
            String description = mDescription.getText().toString();
            String location = mLocation.getText().toString();

            String event_type = "Event";

            myDb.insertDataEvent(title,description,location);
            myDb.insertDataTodoEvent(event_type, date_from, time_from);

            mTitle.getText().clear();
            mDescription.getText().clear();
            mLocation.getText().clear();

            date_from = "";
            time_from = "";
        }
    }

    private EditText mTitle, mLocation, mDescription;
    private AppCompatButton save_event_button, delete_event_button;
    private DatabaseHelper myDb;
    private DialogDateTimeListener mDialogDateTimeListener = new DialogDateTimeListener();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_item);

        myDb = new DatabaseHelper(getApplicationContext());

        mTitle = findViewById(R.id.event_title);
        mLocation = findViewById(R.id.event_location);
        mDescription = findViewById(R.id.event_description);
        save_event_button = findViewById(R.id.save_event_button);
        delete_event_button = findViewById(R.id.delete_event_button);

        AddData();
        delete_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EventItemActivity.this);

                builder.setMessage("Do you want to delete the text?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                delete();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        DialogDateTime.registerListener(mDialogDateTimeListener);
    }

    @Override
    protected void onDestroy() {

        DialogDateTime.unregisterListener(mDialogDateTimeListener);

        super.onDestroy();
    }

    public void AddData(){

        save_event_button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                DialogDateTime.onTimeSelectedClick(EventItemActivity.this);
                DialogDateTime.onDateSelectedClick(EventItemActivity.this);

            }
        });
    }

    public void delete(){

        mTitle.getText().clear();
        mDescription.getText().clear();
        mLocation.getText().clear();

    }

}