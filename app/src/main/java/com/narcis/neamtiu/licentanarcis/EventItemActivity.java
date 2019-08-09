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

    public EventItemActivity(){
    }

    public EventItemActivity(String title, String description, String location){

//        this.mTitle.getText().toString() = title;
    }

    public void AddData(){
        save_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mTitle.getText().toString();
                String description = mTitle.getText().toString();
                String location = mTitle.getText().toString();
                String event_type = "Normal Event";

                boolean isInserted = myDb.insertDataEvent(title,description,location, event_type);

                if(isInserted == true) {

                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();

                }else {

                    Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_LONG).show();

                }



            }
        });
    }

}
