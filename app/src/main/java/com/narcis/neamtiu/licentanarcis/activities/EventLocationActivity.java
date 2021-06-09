package com.narcis.neamtiu.licentanarcis.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.EventHelper;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class EventLocationActivity extends AppCompatActivity {

    private EditText mTitle, mDescription;
    private TextView mLocation;

    private AppCompatButton add_location_button, save_event_button, delete_event_button;

    private String mCurrentSelectedTime = new String();
    private String mCurrentSelectedDate = new String();

    private DatePickerDialog mDateDialog = null;
    private TimePickerDialog mTimeDialog = null;

    void commitData() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        String location = mLocation.getText().toString();

        final String userId = FirestoreManager.getInstance().getCurrentUserID();
        EventData locationEvent = new EventData(userId, Constants.LOCATION_EVENT,
                mCurrentSelectedDate, mCurrentSelectedTime, title, description, location);

        FirestoreManager.getInstance().registerDataEvent(locationEvent);

        clearEditTexts();
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);

        mTitle = findViewById(R.id.event_title);
        mDescription = findViewById(R.id.event_description);
        mLocation = findViewById(R.id.event_location);
        add_location_button = findViewById(R.id.add_location_button);
        save_event_button = findViewById(R.id.save_event_button);
        delete_event_button = findViewById(R.id.delete_event_button);

        mDateDialog = new DatePickerDialog(EventLocationActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mCurrentSelectedDate = EventHelper.formatDatePicked(year, month, day);
                mTimeDialog.show();
            }
        }, LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth());

        mTimeDialog = new TimePickerDialog(EventLocationActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCurrentSelectedTime = EventHelper.formatTimePicked(hourOfDay, minute);
                commitData();
            }
        }, LocalTime.now().getHour(), LocalTime.now().getMinute(), true);

        save_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateDialog.show();
            }
        });

        delete_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EventLocationActivity.this);

                builder.setMessage("Do you want to delete the text?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clearEditTexts();
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
        super.onDestroy();
    }

    public void clearEditTexts() {
        mTitle.getText().clear();
        mDescription.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}