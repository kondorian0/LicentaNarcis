package com.narcis.neamtiu.licentanarcis.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.adapters.EventListAdapter;

import java.util.ArrayList;


public class DayEventsActivity extends AppCompatActivity
{
    private ArrayList<String> events = new ArrayList<String>();
//    private String tableName = TABLE_TODO_EVENTS;
//    private String columnName = KEY_DATE_FROM;
    private SQLiteDatabase newDB;
    ListView mListView;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_events_list);

        mListView = (ListView)findViewById(R.id.list);

        displayEventsList();
    }

    private String dateSelected()
    {
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            String value = extras.getString("selectedDay");
            return value;
        }
        else
        {
            return null;
        }
    };

    private void displayEventsList()
    {
        EventListAdapter itemAdapter = new EventListAdapter(this, getItemsList());
        mListView.setAdapter((ListAdapter) itemAdapter);
    }

    private ArrayList<String> getItemsList() {
        getItemsList().add("text");
        return getItemsList();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
