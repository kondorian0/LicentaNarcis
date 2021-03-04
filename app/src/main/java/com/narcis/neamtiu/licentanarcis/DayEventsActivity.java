package com.narcis.neamtiu.licentanarcis;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.narcis.neamtiu.licentanarcis.database.DatabaseHelper.KEY_DATE_FROM;
import static com.narcis.neamtiu.licentanarcis.database.DatabaseHelper.TABLE_TODO_EVENTS;


public class DayEventsActivity extends AppCompatActivity
{
    private ArrayList<String> events = new ArrayList<String>();
    private String tableName = TABLE_TODO_EVENTS;
    private String columnName = KEY_DATE_FROM;
    private SQLiteDatabase newDB;
    ListView mListView;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_events_list);

        mListView = (ListView)findViewById(R.id.list);

//        dateSelected();

//        openAndQueryDatabase();

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

//        ArrayList<String> events = new ArrayList<>();
//        events.add("Audio 1");
//        events.add("Audio 2");
//
//        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.row_item_test, events);
//        listView.setAdapter(adapter);

        EventTest event1 = new EventTest("Cumparaturi", "magazin Lidl", "21/05/2020");
        EventTest event2 = new EventTest("Examen", "geografie", "12/05/2020");

        ArrayList<EventTest> eventsList = new ArrayList<>();
        eventsList.add(event1);
        eventsList.add(event2);

        EventListAdapter adapter = new EventListAdapter(this, R.layout.row_item, eventsList, this);
        mListView.setAdapter(adapter);
    }
//    private void openAndQueryDatabase() {
//        try {
//            DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
//            newDB = dbHelper.getWritableDatabase();
////            Cursor c = newDB.rawQuery("SELECT columnName FROM " +
////                    tableName +
////                    " where columnName == dayToSearch", null);
//
//            Cursor c = newDB.rawQuery("SELECT columnName FROM " +
//                    tableName, null);
//
//            if (c != null ) {
//                if (c.moveToFirst()) {
//                    do {
//                        String firstName = c.getString(c.getColumnIndex(columnName));
//                        int age = c.getInt(c.getColumnIndex(columnName));
//                        results.add("test 1 ========= "+age+"  test 2 ========= "+ firstName);
//                    }while (c.moveToNext());
//                }
//            }
//        } catch (SQLiteException se ) {
//            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
//        } finally {
//            if (newDB != null)
//                newDB.execSQL("DELETE FROM " + tableName);
//            newDB.close();
//        }
//
//    }

}
