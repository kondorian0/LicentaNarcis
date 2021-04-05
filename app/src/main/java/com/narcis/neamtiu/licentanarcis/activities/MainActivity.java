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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreClass;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTimeHelper;
import com.narcis.neamtiu.licentanarcis.util.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
//import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
//import com.narcis.neamtiu.licentanarcis.util.EventDecorator;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity{
    private final int EVENT_ITEM = 1;
    private final int NOTE_ITEM = 2;
    private final int AUDIO_ITEM = 3;
    private final int IMAGE_ITEM = 4;

    private TextView mTextView;

    Map<CalendarDay, EventDecorator> mDecorators = new HashMap<CalendarDay, EventDecorator>();

    private static final String TAG = "MainActivity";

//    DatabaseHelper myDb = new DatabaseHelper(this);

    private FloatingActionButton mEventItem, mNoteItem, mAudioItem, mDrawItem;

    private MaterialCalendarView mCalendarView;

    private FirestoreClass firestoreClass;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        firestoreClass = new FirestoreClass();
//        firestoreClass.getUserData();


//        myDb.doStaf();
        SharedPreferences sharedPreferences =
                getSharedPreferences(
                        Constants.EVENTS,
                        Context.MODE_PRIVATE);

        String userName = sharedPreferences.getString(Constants.TYPE_OF_EVENT, "");

//        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.testTestView);
        mTextView.setText(userName);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if(requestCode == NOTE_ITEM)
        {
            if (resultCode == Constants.RESULT_SUCCESS)
            {
                String selectedDate = data.getStringExtra(Constants.SELECTED_DATE);
                CalendarDay selectedDay = calendarDayFromString(selectedDate);
                mDecorators.get(selectedDay);
                EventDecorator selectedDayDecorator = mDecorators.get(selectedDay);

                if (selectedDayDecorator == null)
                {
                    selectedDayDecorator = new EventDecorator(selectedDay);
                }
                else
                {
                    mCalendarView.removeDecorator(selectedDayDecorator);
                }

                selectedDayDecorator.decorateNoteDot = true;

                mCalendarView.addDecorator(selectedDayDecorator);
            }

        }
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