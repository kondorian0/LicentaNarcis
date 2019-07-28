package com.narcis.neamtiu.licentanarcis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private AppCompatButton save_note_button, delete_note_button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        save_note_button = findViewById(R.id.save_note_button);
        delete_note_button = findViewById(R.id.delete_note_button);
        editText = findViewById(R.id.editText);

        save_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //save to list items respective day

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

    public void delete(){

        editText.getText().clear();

        Toast.makeText(getApplicationContext(),"Text deleted", Toast.LENGTH_LONG).show();

    }

}
