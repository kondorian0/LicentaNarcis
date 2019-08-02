package com.narcis.neamtiu.licentanarcis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.*;

public class EventItemActivity extends AppCompatActivity  {

    private EditText mTitle, mLocation, mDescription;
    private AppCompatButton save_event_button;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_item);

        mTitle = findViewById(R.id.event_title);
        mLocation = findViewById(R.id.event_location);
        mDescription = findViewById(R.id.event_description);
        save_event_button = findViewById(R.id.save_event_button);

    }
}
