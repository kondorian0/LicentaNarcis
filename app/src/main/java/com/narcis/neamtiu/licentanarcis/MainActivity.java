package com.narcis.neamtiu.licentanarcis;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FloatingActionButton mEventItem, mNoteItem, mAudioItem, mDrawItem;

    public MaterialCalendarView mCalendarView;
    public Calendar calendar = Calendar.getInstance();


    public void selectTodayDateCalendar() {
        mCalendarView.setSelectedDate(calendar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendarView = findViewById(R.id.calendarView);

        DialogDateTime.widget = mCalendarView;


        this.selectTodayDateCalendar();
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                Intent intent = new Intent(MainActivity.this, DayEvent.class);
                startActivity(intent);
            }
        });

        mEventItem = findViewById(R.id.menu_item_event);
        mEventItem.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                LocalDateTime ldt = LocalDateTime.now();

                DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd MMM. yyyy", Locale.ENGLISH);
                String currentDate = dateTime.format(ldt);

                DateTimeFormatter hourTime = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
                String currentHour = hourTime.format(ldt);


                Intent intent = new Intent(MainActivity.this, EventItem.class);
                intent.putExtra("Start Date",currentDate);
                intent.putExtra("Start Hour", currentHour);
                startActivity(intent);
            }
        });

        mNoteItem = findViewById(R.id.menu_item_note);
        mNoteItem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent eventsIntent = new Intent(MainActivity.this, EventItem.class);
                startActivity(eventsIntent);

            }
        });

        mAudioItem = findViewById(R.id.menu_item_audio);
        mAudioItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent eventsIntent = new Intent(MainActivity.this, RecordActivity.class);
//                startActivity(eventsIntent);

            }
        });

        mDrawItem = findViewById(R.id.menu_item_draw);
        mDrawItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent eventsIntent = new Intent(MainActivity.this, DrawActivity.class);
                startActivity(eventsIntent);

            }
        });

    }

    @Override
    protected void onStop() {

        super.onStop();
        this.selectTodayDateCalendar();

    }
}