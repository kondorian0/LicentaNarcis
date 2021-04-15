package com.narcis.neamtiu.licentanarcis.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.adapters.EventListAdapter;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;

import java.util.ArrayList;

public class DayEventsActivity extends AppCompatActivity {

    private FirestoreManager firestoreManager = FirestoreManager.getInstance();
    private ArrayList<EventData> allDataList = firestoreManager.getEventsListFromFirestore();
    private ArrayList<EventData> dayDataList = new ArrayList<EventData>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_events_list);

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
        } else {
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
