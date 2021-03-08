package com.narcis.neamtiu.licentanarcis;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.narcis.neamtiu.licentanarcis.activities.DayEventsActivity;
import com.narcis.neamtiu.licentanarcis.activities.DrawActivity;
import com.narcis.neamtiu.licentanarcis.activities.EventActivity;
import com.narcis.neamtiu.licentanarcis.activities.NoteActivity;
import com.narcis.neamtiu.licentanarcis.activities.RecordActivity;
import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
//import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
//import com.narcis.neamtiu.licentanarcis.util.EventDecorator;

import org.threeten.bp.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity{
    private final int EVENT_ITEM = 1;
    private final int NOTE_ITEM = 2;
    private final int AUDIO_ITEM = 3;
    private final int IMAGE_ITEM = 4;

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;

//    Map<CalendarDay, EventDecorator> mDecorators = new HashMap<CalendarDay, EventDecorator>();

    private static final String TAG = "MainActivity";

    DatabaseHelper myDb;

    private FloatingActionButton mEventItem, mNoteItem, mAudioItem, mDrawItem;

    public MaterialCalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        mCalendarView = findViewById(R.id.calendarView);
        mEventItem = findViewById(R.id.menu_item_event);
        mNoteItem = findViewById(R.id.menu_item_note);

        selectCurrentDate();

        final Intent intentDayEvent = new Intent(MainActivity.this, DayEventsActivity.class);

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String selectedDate = date.getDate().format(dateTimeFormatter);
                Log.d(TAG, selectedDate);
                intentDayEvent.putExtra("selectedDay", selectedDate);
                Log.d(TAG, String.valueOf(intentDayEvent));
                startActivity(intentDayEvent);
            }
        });

        mEventItem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivityForResult(intent, EVENT_ITEM);
            }
        });

        mNoteItem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent, NOTE_ITEM);

                // Cumva il dai mai departe
            }
        });

        mAudioItem = findViewById(R.id.menu_item_audio);
        mAudioItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivityForResult(intent, AUDIO_ITEM);
            }
        });

        mDrawItem = findViewById(R.id.menu_item_draw);
        mDrawItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                startActivityForResult(intent, IMAGE_ITEM);
            }
        });
    }

    private static CalendarDay calendarDayFromString(String dayAsString)
    {
        String[] dayMonthYear = dayAsString.split("/");

        // TODO: Validate & improve
        String day = dayMonthYear[0];
        String month = dayMonthYear[1];
        String year = dayMonthYear[2];

        CalendarDay calendarDay = CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        return calendarDay;
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // check if the request code is same as what is passed  here it is 2
//        if(requestCode == NOTE_ITEM)
//        {
//            if (resultCode == NoteActivity.RESULT_SUCCESS)
//            {
//                String selectedDate = data.getStringExtra(NoteActivity.SELECTED_DATE);
//                CalendarDay selectedDay = calendarDayFromString(selectedDate);
//                mDecorators.get(selectedDay);
//                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);
//
//                if (selectedDayDecorator == null)
//                {
//                    selectedDayDecorator = new EventDecorator(selectedDay);
//                }
//                else
//                {
//                    mCalendarView.removeDecorator(selectedDayDecorator);
//                }
//
//                selectedDayDecorator.decorateNoteDot = true;
//
//                mCalendarView.addDecorator(selectedDayDecorator);
//            }
//
//        }
//        else if (requestCode == EVENT_ITEM)
//        {
//            if (resultCode == EventActivity.RESULT_SUCCESS)
//            {
//                String selectedDate = data.getStringExtra(EventActivity.SELECTED_DATE);
//                CalendarDay selectedDay = calendarDayFromString(selectedDate);
//                mDecorators.get(selectedDay);
//                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);
//
//                if (selectedDayDecorator == null)
//                {
//                    selectedDayDecorator = new EventDecorator(selectedDay);
//                }
//                else
//                {
//                    mCalendarView.removeDecorator(selectedDayDecorator);
//                }
//
//                selectedDayDecorator.decorateEventDot = true;
//
//                mCalendarView.addDecorator(selectedDayDecorator);
//            }
//        }
//        else if (requestCode == AUDIO_ITEM)
//        {
//            if (resultCode == RecordActivity.RESULT_SUCCESS)
//            {
//                String selectedDate = data.getStringExtra(RecordActivity.SELECTED_DATE);
//                CalendarDay selectedDay = calendarDayFromString(selectedDate);
//                mDecorators.get(selectedDay);
//                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);
//
//                if (selectedDayDecorator == null)
//                {
//                    selectedDayDecorator = new EventDecorator(selectedDay);
//                }
//                else
//                {
//                    mCalendarView.removeDecorator(selectedDayDecorator);
//                }
//
//                selectedDayDecorator.decorateAudioDot = true;
//
//                mCalendarView.addDecorator(selectedDayDecorator);
//            }
//
//        }
//        else if (requestCode == IMAGE_ITEM)
//        {
//            if (resultCode == DrawActivity.RESULT_SUCCESS)
//            {
//                String selectedDate = data.getStringExtra(DrawActivity.SELECTED_DATE);
//                CalendarDay selectedDay = calendarDayFromString(selectedDate);
//                mDecorators.get(selectedDay);
//                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);
//
//                if (selectedDayDecorator == null)
//                {
//                    selectedDayDecorator = new EventDecorator(selectedDay);
//                }
//                else
//                {
//                    mCalendarView.removeDecorator(selectedDayDecorator);
//                }
//
//                selectedDayDecorator.decorateImageDot = true;
//
//                mCalendarView.addDecorator(selectedDayDecorator);
//            }
//        }
//    }

    private void selectCurrentDate(){
        mCalendarView.setSelectedDate(CalendarDay.today());
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.selectCurrentDate();
    }
}