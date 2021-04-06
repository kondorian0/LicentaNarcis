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
import com.narcis.neamtiu.licentanarcis.util.EventListData;

import java.util.ArrayList;


public class DayEventsActivity extends AppCompatActivity {
//    private String tableName = TABLE_TODO_EVENTS;
//    private String columnName = KEY_DATE_FROM;

    private ListView mListView;
    private FirestoreClass firestoreClass;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_events_list);


        ArrayList<EventListData> myListData = (ArrayList<EventListData>) getIntent().getSerializableExtra("userData");

//        EventListData[] myListData = new EventListData[] {
//                new EventListData("Note", "a lore ipsum", "32/12/2001", R.drawable.ic_note_color),
//                new EventListData("Audio", "facem cepsum", "21/09/2001", R.drawable.ic_audio_color),
//                new EventListData("Image", "cem ceva lore i", "12/11/2001", R.drawable.ic_image_color),
//                new EventListData("Location", "aceva losum", "65/13/2001", R.drawable.ic_location_color)
//        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        EventListAdapter adapter = new EventListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        displayEventsList();
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

//    private void displayEventsList()
//    {
//        EventListAdapter itemAdapter = new EventListAdapter(this, getItemsList());
//        mListView.setAdapter((ListAdapter) itemAdapter);
//    }

//    private ArrayList<String> getItemsList() {
//        getItemsList().add("text");
//        return getItemsList();
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
