package com.narcis.neamtiu.licentanarcis;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.Calendar;

import static java.util.Calendar.*;

public class EventItem extends AppCompatActivity  {

    private static final String TAG = "EventItem";

    private static AppCompatTextView mEventDateStart;
    private static AppCompatTextView mEventDateEnd;

    private static AppCompatTextView mEventTimeStart, mEventTimeEnd;

    private SwitchCompat mAllDay;
    private static Boolean isTouched = false;

    private Calendar mTime = getInstance();
    int hour = mTime.get(Calendar.HOUR_OF_DAY);
    int minute = mTime.get(Calendar.MINUTE);

    private EditText mTitle, mLocation, mDescription;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_item);

        DialogDateTime.context1 = this;

        mTitle = findViewById(R.id.event_title);
        mLocation = findViewById(R.id.event_location);
        mDescription = findViewById(R.id.event_description);

        mAllDay = findViewById(R.id.event_all_day);

        mEventDateStart = findViewById(R.id.event_start_date);
        mEventDateEnd = findViewById(R.id.event_end_date);
        mEventTimeStart = findViewById(R.id.event_start_time);
        mEventTimeEnd = findViewById(R.id.event_end_time);

        Intent incomingIntent = getIntent();
        String currentDate = incomingIntent.getStringExtra("Start Date");
        mEventDateStart.setText(currentDate);
        mEventDateEnd.setText(currentDate);

        String currentHour = incomingIntent.getStringExtra("Start Hour");

        mEventTimeStart.setText(currentHour);
        mEventTimeEnd.setText(currentHour);

        mEventDateStart.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                DialogDateTime.onDateSelectedClick();

            }
        });

        mEventDateEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogDateTime.onDateSelectedClick();

            }
        });

        mEventTimeStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogDateTime.onTimeSelectedClick();

            }
        });

        mEventTimeStart.setText(DialogDateTime.getTimeHourMinute());

        mEventTimeEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogDateTime.onTimeSelectedClick();

            }
        });


        mAllDay.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                isTouched = true;
                return false;

            }
        });

        mAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isTouched) {

                    isTouched = false;
                    if (isChecked) {

                        mEventDateStart.setVisibility(View.GONE);
                        mEventDateEnd.setVisibility(View.GONE);
                        mEventTimeStart.setVisibility(View.GONE);
                        mEventTimeEnd.setVisibility(View.GONE);

                    }
                    else {

                        mEventDateStart.setVisibility(View.VISIBLE);
                        mEventDateEnd.setVisibility(View.VISIBLE);
                        mEventTimeStart.setVisibility(View.VISIBLE);
                        mEventTimeEnd.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
    }
}
