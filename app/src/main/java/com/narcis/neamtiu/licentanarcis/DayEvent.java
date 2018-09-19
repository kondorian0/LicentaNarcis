package com.narcis.neamtiu.licentanarcis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class DayEvent extends AppCompatActivity {

    private MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_preview_activity);

    }
}
