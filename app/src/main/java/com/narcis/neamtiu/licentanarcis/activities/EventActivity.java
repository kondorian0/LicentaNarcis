package com.narcis.neamtiu.licentanarcis.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTimeHelper;

public class EventActivity extends AppCompatActivity
        implements DialogDateTime.Listener {

    private String EVENT_TYPE = "Event";
    private DialogDateTimeHelper mDateTimeHelper;
    private EditText mTitle, mLocation, mDescription;
    private AppCompatButton save_event_button, delete_event_button;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_item);

        mTitle = findViewById(R.id.event_title);
        mLocation = findViewById(R.id.event_location);
        mDescription = findViewById(R.id.event_description);
        save_event_button = findViewById(R.id.save_event_button);
        delete_event_button = findViewById(R.id.delete_event_button);

        mDateTimeHelper = DialogDateTimeHelper.getInstance(getApplicationContext());
        mDateTimeHelper.setEVENT_TYPE(EVENT_TYPE);
        mDateTimeHelper.setTitle(mLocation);
        mDateTimeHelper.setDescription(mDescription);
        mDateTimeHelper.setLocation(mLocation);

        DialogDateTime.registerListener(this);

        save_event_button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DialogDateTime.onTimeSelectedClick(EventActivity.this);
                DialogDateTime.onDateSelectedClick(EventActivity.this);
            }
        });

        delete_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);

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
    protected void onDestroy() {
        DialogDateTime.unregisterListener(this);
        super.onDestroy();
    }

    public void delete(){
        mTitle.getText().clear();
        mDescription.getText().clear();
        mLocation.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTimePicked(int hourOfDay, int minute) {
        mDateTimeHelper.onTimePicked(hourOfDay, minute);
    }

    @Override
    public void onDatePicked(int year, int month, int day) {
        mDateTimeHelper.onDatePicked(year, month, day);
    }
}