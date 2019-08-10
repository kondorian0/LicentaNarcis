package com.narcis.neamtiu.licentanarcis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.*;

import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;

public class EventItemActivity extends AppCompatActivity  {

    private EditText mTitle, mLocation, mDescription;
    private AppCompatButton save_event_button;
    DatabaseHelper myDb;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_item);

        myDb = new DatabaseHelper(getApplicationContext());

        mTitle = findViewById(R.id.event_title);
        mLocation = findViewById(R.id.event_location);
        mDescription = findViewById(R.id.event_description);
        save_event_button = findViewById(R.id.save_event_button);

        AddData();

    }
    public void AddData(){
        save_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                String location = mLocation.getText().toString();

                //tests
                String event_type = "Event";
                String date_from = "xx/xx/xxxx";
                String date_to = "xx/xx/xxxx";
                String time_from = "xx:xx:xx";
                String time_to = "xx:xx:xx";

                myDb.insertDataEvent(title, description, location);
                myDb.insertDataTodoEvent(event_type, date_from, date_to, time_from, time_to);

                mTitle.getText().clear();
                mDescription.getText().clear();
                mLocation.getText().clear();

            }
        });
    }

}
