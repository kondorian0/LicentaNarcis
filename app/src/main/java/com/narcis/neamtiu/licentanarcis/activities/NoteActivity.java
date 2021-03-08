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
import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;

public class NoteActivity extends AppCompatActivity
{
    private String EVENT_TYPE = "Note";

    private AppCompatButton save_note_button, delete_note_button;
    private EditText mNote;
    private DatabaseHelper myDb;
    private DialogDateTimeListener mDialogDateTimeListener = new DialogDateTimeListener();

//    class DialogDateTimeListener implements DialogDateTime.Listener
//    {
//        String date_from = "";
//        String time_from = "";
//
//        @Override
//        public void onTimePicked(int hourOfDay, int minute)
//        {
//            if(hourOfDay < 10 && minute < 10)
//            {
//                time_from = "0" + hourOfDay + ":" + "0" + minute;
//            }
//            else if(hourOfDay < 10 && minute >= 10)
//            {
//                time_from = "0" + hourOfDay + ":" + minute;
//            }
//            else if(hourOfDay >= 10 && minute < 10)
//            {
//                time_from = hourOfDay + ":" + "0" + minute;
//            }
//            else if(hourOfDay >= 10 && minute >= 10)
//            {
//                time_from = hourOfDay + ":" + minute;
//            }
//
////            time_from = startTime;
//            commitData();
//        }
//
//        @Override
//        public void onDatePicked(int year, int month, int day)
//        {
//            date_from = day + "/" + month + "/" + year;
//            commitData();
//        }
//
//        void commitData()
//        {
//            if (date_from.isEmpty() || time_from.isEmpty())
//            {
//                return;
//            }
//
//            String note = mNote.getText().toString();
//
//            myDb.insertDataNote(note);
//            myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);
//
//            mNote.getText().clear();
//
//            Intent intent = new Intent();
//            intent.putExtra(SELECTED_DATE, date_from);
//            setResult(RESULT_SUCCESS, intent);
//
//            date_from = "";
//            time_from = "";
//
//            finish();
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        myDb = new DatabaseHelper(getApplicationContext());

        save_note_button = findViewById(R.id.save_note_button);
        delete_note_button = findViewById(R.id.delete_event_button);
        mNote = findViewById(R.id.editText);

        AddData();

        delete_note_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);

                builder.setMessage("Do you want to delete the text?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                // FIRE ZE MISSILES!
                                delete();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        DialogDateTime.registerListener(mDialogDateTimeListener);
    }

    @Override
    protected void onDestroy()
    {
        DialogDateTime.unregisterListener(mDialogDateTimeListener);
        super.onDestroy();
    }

    public void AddData()
    {
        save_note_button.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                DialogDateTime.onTimeSelectedClick(NoteActivity.this);
//                DialogDateTime.onDateSelectedClick(NoteActivity.this);
            }
        });
    }

    public void delete()
    {
        mNote.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
