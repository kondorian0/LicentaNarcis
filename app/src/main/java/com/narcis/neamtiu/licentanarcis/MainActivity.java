package com.narcis.neamtiu.licentanarcis;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DatabaseHelper myDb;

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

        myDb = new DatabaseHelper(this);

        mCalendarView = findViewById(R.id.calendarView);

        DialogDateTime.widget = mCalendarView;


        this.selectTodayDateCalendar();
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

                Intent intent = new Intent(MainActivity.this, DayEventActivity.class);
                startActivity(intent);
            }
        });

        mEventItem = findViewById(R.id.menu_item_event);
        mEventItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EventItemActivity.class);
                startActivity(intent);
            }
        });

        mNoteItem = findViewById(R.id.menu_item_note);
        mNoteItem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent eventsIntent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(eventsIntent);

            }
        });

        mAudioItem = findViewById(R.id.menu_item_audio);
        mAudioItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent eventsIntent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(eventsIntent);

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