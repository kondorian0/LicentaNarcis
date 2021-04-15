package com.narcis.neamtiu.licentanarcis.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.EventHelper;

public class EventLocationActivity extends AppCompatActivity
        implements DialogDateTime.Listener {

    private String EVENT_TYPE = Constants.LOCATION_EVENT;
    private EventHelper mDateTimeHelper;
    private EditText mTitle, mDescription;
    private TextView mLocation;

    private AppCompatButton add_location_button, save_event_button, delete_event_button;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);

        mTitle = findViewById(R.id.event_title);
        mDescription = findViewById(R.id.event_description);
        mLocation = findViewById(R.id.event_location);
        add_location_button = findViewById(R.id.add_location_button);
        save_event_button = findViewById(R.id.save_event_button);
        delete_event_button = findViewById(R.id.delete_event_button);
//
//        mDateTimeHelper = EventHelper.getInstance(getApplicationContext());
//        mDateTimeHelper.setEVENT_TYPE(EVENT_TYPE);
//        mDateTimeHelper.setTitle(mTitle);
//        mDateTimeHelper.setDescription(mDescription);
//        mDateTimeHelper.setLocation(mLocation);

        DialogDateTime.registerListener(this);

        save_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDateTime.onTimeSelectedClick(EventLocationActivity.this);
                DialogDateTime.onDateSelectedClick(EventLocationActivity.this);
            }
        });

        delete_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EventLocationActivity.this);

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
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EventLocationActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DialogDateTime.unregisterListener(this);
        super.onDestroy();
    }

    public void delete(){
        mTitle.getText().clear();
        mDescription.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTimePicked(int hourOfDay, int minute) {
//        mDateTimeHelper.onTimePicked(hourOfDay, minute);
    }

    @Override
    public void onDatePicked(int year, int month, int day) {
//        mDateTimeHelper.onDatePicked(year, month, day);
    }
}