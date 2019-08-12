package com.narcis.neamtiu.licentanarcis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;

public class NoteActivity extends AppCompatActivity {

    private AppCompatButton save_note_button, delete_note_button;
    private EditText mNote;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        myDb = new DatabaseHelper(getApplicationContext());

        save_note_button = findViewById(R.id.save_note_button);
        delete_note_button = findViewById(R.id.delete_note_button);
        mNote = findViewById(R.id.editText);

        AddData();


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

    public void AddData(){
        save_note_button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                DialogDateTime.onDateSelectedClick(NoteActivity.this);

                String note = mNote.getText().toString();

                String date_from = DialogDateTime.getStartDate();
//
//                //tests
                String event_type = "Note";
                String date_to = "yy/yy/yyyy";
                String time_from = "zz:zz:zz";
                String time_to = "zz:zz:zz";
//
                myDb.insertDataNote(note);
                myDb.insertDataTodoEvent(event_type, date_from, date_to, time_from, time_to);
//
                mNote.getText().clear();

            }
        });
    }

    public void delete(){

        mNote.getText().clear();

        Toast.makeText(getApplicationContext(),"Text deleted", Toast.LENGTH_LONG).show();

    }

}
