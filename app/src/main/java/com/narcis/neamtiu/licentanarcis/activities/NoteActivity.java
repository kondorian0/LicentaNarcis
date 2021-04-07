package com.narcis.neamtiu.licentanarcis.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTimeHelper;

public class NoteActivity extends AppCompatActivity
        implements DialogDateTime.Listener {

    private String EVENT_TYPE = "Note";

    private DialogDateTimeHelper mDateTimeHelper;

    private AppCompatButton save_note_button, delete_note_button;
    private EditText mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        save_note_button = findViewById(R.id.save_note_button);
        delete_note_button = findViewById(R.id.delete_event_button);
        mNote = findViewById(R.id.editText);

        mDateTimeHelper = DialogDateTimeHelper.getInstance(getApplicationContext());
        mDateTimeHelper.setEVENT_TYPE(EVENT_TYPE);
        mDateTimeHelper.setNote(mNote);

        DialogDateTime.registerListener(this);

        save_note_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                DialogDateTime.onTimeSelectedClick(NoteActivity.this);
                DialogDateTime.onDateSelectedClick(NoteActivity.this);
            }
        });

        delete_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);

                builder.setMessage("Do you want to delete the text?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                delete();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NoteActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DialogDateTime.unregisterListener(this);
        super.onDestroy();
    }

    public void delete() {
        mNote.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTimePicked(int hourOfDay, int minute) {
        mDateTimeHelper.onTimePicked(hourOfDay, minute);
    }

    @Override
    public void onDatePicked(int year, int month, int day) {
        mDateTimeHelper.onDatePicked(year, month, day);
    }
}
