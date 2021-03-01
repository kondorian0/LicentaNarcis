package com.narcis.neamtiu.licentanarcis;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private final int EVENT_ITEM = 1;
    private final int NOTE_ITEM = 2;
    private final int AUDIO_ITEM = 3;
    private final int IMAGE_ITEM = 4;

    Map<CalendarDay, EventDecorator> mDecorators = new HashMap<CalendarDay, EventDecorator>();

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
                SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/YYYY");

                CalendarDay mDaySelected = mCalendarView.getSelectedDate();
                Intent intent = new Intent(MainActivity.this, DayEventActivity.class);
                Date date = mDaySelected.getDate();

                String dateString = sdfr.format(date);
                Log.d(TAG, dateString);
                intent.putExtra("selectedDay", dateString);
                Log.d(TAG, String.valueOf(intent));
                startActivity(intent);
            }
        });

        mEventItem = findViewById(R.id.menu_item_event);
        mEventItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivityForResult(intent, EVENT_ITEM);
            }
        });

        mNoteItem = findViewById(R.id.menu_item_note);
        mNoteItem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent, NOTE_ITEM);

                // Cumva il dai mai departe

            }
        });

        mAudioItem = findViewById(R.id.menu_item_audio);
        mAudioItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivityForResult(intent, AUDIO_ITEM);

            }
        });

        mDrawItem = findViewById(R.id.menu_item_draw);
        mDrawItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                startActivityForResult(intent, IMAGE_ITEM);

            }
        });

    }

    private static CalendarDay calendarDayFromString(String dayAsString) {

        String[] dayMonthYear = dayAsString.split("/");

        // TODO: Validate & improve
        String day = dayMonthYear[0];
        String month = dayMonthYear[1];
        String year = dayMonthYear[2];

        CalendarDay calendarDay = CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        return calendarDay;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if(requestCode == NOTE_ITEM) {

            if (resultCode == NoteActivity.RESULT_SUCCESS) {

                String selectedDate = data.getStringExtra(NoteActivity.SELECTED_DATE);
                CalendarDay selectedDay = calendarDayFromString(selectedDate);
                mDecorators.get(selectedDay);
                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);

                if (selectedDayDecorator == null) {

                    selectedDayDecorator = new EventDecorator(selectedDay);

                } else {

                    mCalendarView.removeDecorator(selectedDayDecorator);

                }

                selectedDayDecorator.decorateNoteDot = true;

                mCalendarView.addDecorator(selectedDayDecorator);

            }

        } else if (requestCode == EVENT_ITEM){

            if (resultCode == EventActivity.RESULT_SUCCESS) {

                String selectedDate = data.getStringExtra(EventActivity.SELECTED_DATE);
                CalendarDay selectedDay = calendarDayFromString(selectedDate);
                mDecorators.get(selectedDay);
                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);

                if (selectedDayDecorator == null) {

                    selectedDayDecorator = new EventDecorator(selectedDay);

                } else {

                    mCalendarView.removeDecorator(selectedDayDecorator);

                }

                selectedDayDecorator.decorateEventDot = true;

                mCalendarView.addDecorator(selectedDayDecorator);

            }

        } else if (requestCode == AUDIO_ITEM){

            if (resultCode == RecordActivity.RESULT_SUCCESS) {

                String selectedDate = data.getStringExtra(RecordActivity.SELECTED_DATE);
                CalendarDay selectedDay = calendarDayFromString(selectedDate);
                mDecorators.get(selectedDay);
                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);

                if (selectedDayDecorator == null) {

                    selectedDayDecorator = new EventDecorator(selectedDay);

                } else {

                    mCalendarView.removeDecorator(selectedDayDecorator);

                }

                selectedDayDecorator.decorateAudioDot = true;

                mCalendarView.addDecorator(selectedDayDecorator);

            }

        } else if (requestCode == IMAGE_ITEM){

            if (resultCode == DrawActivity.RESULT_SUCCESS) {

                String selectedDate = data.getStringExtra(DrawActivity.SELECTED_DATE);
                CalendarDay selectedDay = calendarDayFromString(selectedDate);
                mDecorators.get(selectedDay);
                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);

                if (selectedDayDecorator == null) {

                    selectedDayDecorator = new EventDecorator(selectedDay);

                } else {

                    mCalendarView.removeDecorator(selectedDayDecorator);

                }

                selectedDayDecorator.decorateImageDot = true;

                mCalendarView.addDecorator(selectedDayDecorator);

            }
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        this.selectTodayDateCalendar();

    }
}