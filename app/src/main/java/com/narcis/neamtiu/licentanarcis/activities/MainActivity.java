package com.narcis.neamtiu.licentanarcis.activities;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
//import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreClass;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.EventDecorator;
import com.narcis.neamtiu.licentanarcis.util.EventListData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
//import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
//import com.narcis.neamtiu.licentanarcis.util.EventDecorator;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private TextView mTextView;

//    Map<CalendarDay, EventDecorator> mDecorators = new HashMap<CalendarDay, EventDecorator>();

    private static final String TAG = "MainActivity";

    private FloatingActionButton mEventLocationItem, mNoteItem, mAudioItem, mDrawItem;

    private MaterialCalendarView mCalendarView;

    private FirestoreClass firestoreClass = new FirestoreClass();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.EVENTS, Context.MODE_PRIVATE);

        mCalendarView = findViewById(R.id.calendarView);
        mEventLocationItem = findViewById(R.id.menu_item_event);
        mNoteItem = findViewById(R.id.menu_item_note);

        firestoreClass.registerListener(new FirestoreClass.Listener() {
            @Override
            public void onUserDataAcquired(ArrayList<EventListData> list) {
                updateCalendarViewSpanDots(list);
            }
        });
        firestoreClass.getUserData();

        selectCurrentDate();

        final Intent intentDayEvent = new Intent(MainActivity.this, DayEventsActivity.class);

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String selectedDate = date.getDate().format(dateTimeFormatter);
                ArrayList<EventListData> allDataList = firestoreClass.getEventsListFromFirestore();
                Log.d(TAG, selectedDate);
                intentDayEvent.putExtra("userData", allDataList);
                intentDayEvent.putExtra(Constants.SELECTED_DATE, selectedDate);
                Log.d(TAG, String.valueOf(intentDayEvent));
                startActivity(intentDayEvent);
            }
        });

        mEventLocationItem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, EventLocationActivity.class);
                startActivityForResult(intent, Constants.EVENT_LOCATION_ITEM);
                finish();
            }
        });

        mNoteItem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent, Constants.NOTE_ITEM);
                finish();
            }
        });

        mAudioItem = findViewById(R.id.menu_item_audio);
        mAudioItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivityForResult(intent, Constants.AUDIO_ITEM);
                finish();
            }
        });

        mDrawItem = findViewById(R.id.menu_item_draw);
        mDrawItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                startActivityForResult(intent, Constants.IMAGE_ITEM);
                finish();
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

    public void updateCalendarViewSpanDots(ArrayList<EventListData> allDataList)
    {

        for (EventListData eventListData : allDataList) {

            String selectedDate = eventListData.getDate();
            CalendarDay selectedDay = calendarDayFromString(selectedDate);
            EventDecorator dayDecorator = new EventDecorator(selectedDay);
//            if (dayDecorator == null) {
//                dayDecorator = new EventDecorator(selectedDay);
//            }else {
//                mCalendarView.removeDecorator(dayDecorator);
//            }

            switch (eventListData.getType()) {
                case "Note":
                    dayDecorator.decorateNoteDot = true;
                    break;
                case "Location Event":
                    dayDecorator.decorateEventDot = true;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + eventListData.getType());
            }

            mCalendarView.addDecorator(dayDecorator);
        }
        // check if the request code is same as what is passed  here it is 2
//        if(requestCode == Constants.NOTE_ITEM)
//        {
//            if (resultCode == Constants.RESULT_SUCCESS)
//            {
//                String selectedDate = data.getStringExtra(Constants.SELECTED_DATE);
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
    }



    private void selectCurrentDate(){
        mCalendarView.setSelectedDate(CalendarDay.today());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginUserActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.selectCurrentDate();
    }
}