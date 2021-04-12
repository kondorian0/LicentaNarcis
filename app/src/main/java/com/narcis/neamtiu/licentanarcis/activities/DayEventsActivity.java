package com.narcis.neamtiu.licentanarcis.activities;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.adapters.EventListAdapter;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreClass;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.EventListData;

import java.util.ArrayList;


public class DayEventsActivity extends AppCompatActivity {
//    private String tableName = TABLE_TODO_EVENTS;
//    private String columnName = KEY_DATE_FROM;

    private ListView mListView;
    private FirestoreClass firestoreClass;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_events_list);


        ArrayList<EventData> allDataList = firestoreClass.getEventsListFromFirestore();

        ArrayList<EventData> dayDataList = new ArrayList<EventData>();

        for(int i=0; i<allDataList.size(); i++){
            String eventDate = allDataList.get(i).getEventDate();
            String dateSelected = dateSelected();

            if(dateSelected.equals(eventDate)){
                dayDataList.add(allDataList.get(i));
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        EventListAdapter adapter = new EventListAdapter(dayDataList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private String dateSelected() {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String value = extras.getString(Constants.SELECTED_DATE);
            return value;
        }else {
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
