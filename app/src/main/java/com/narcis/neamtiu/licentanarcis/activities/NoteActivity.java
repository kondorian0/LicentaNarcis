package com.narcis.neamtiu.licentanarcis.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.EventHelper;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class NoteActivity extends AppCompatActivity {
    private AppCompatButton save_note_button, delete_note_button;
    private EditText mNote;

    private String mCurrentSelectedTime = new String();
    private String mCurrentSelectedDate = new String();

    private DatePickerDialog mDateDialog = null;
    private TimePickerDialog mTimeDialog = null;

    void commitData() {
        String note = mNote.getText().toString();

        final String userId = FirestoreManager.getInstance().getCurrentUserID();

        EventData noteEvent = new EventData(userId, Constants.NOTE_EVENT, mCurrentSelectedDate, mCurrentSelectedTime, note);

        FirestoreManager.getInstance().registerDataEvent(noteEvent);

        mNote.getText().clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        save_note_button = findViewById(R.id.save_note_button);
        delete_note_button = findViewById(R.id.delete_event_button);
        mNote = findViewById(R.id.editText);

        mDateDialog = new DatePickerDialog(NoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mCurrentSelectedDate = EventHelper.formatDatePicked(year, month, day);
                mTimeDialog.show();
            }
        }, LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth());

        mTimeDialog = new TimePickerDialog(NoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCurrentSelectedTime = EventHelper.formatTimePicked(hourOfDay, minute);
                commitData();
            }
        }, LocalTime.now().getHour(), LocalTime.now().getMinute(), true);

        save_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateDialog.show();
            }
        });

        delete_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);

                builder.setMessage("Do you want to delete the text?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
        super.onDestroy();
    }

    public void delete() {
        mNote.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
