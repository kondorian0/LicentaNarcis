package com.narcis.neamtiu.licentanarcis.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.narcis.neamtiu.licentanarcis.activities.LoginUserActivity;
import com.narcis.neamtiu.licentanarcis.activities.NoteActivity;
import com.narcis.neamtiu.licentanarcis.activities.RegisterUserActivity;
import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreClass;
import com.narcis.neamtiu.licentanarcis.models.EventContent;
import com.narcis.neamtiu.licentanarcis.models.User;

import java.util.ArrayList;
import java.util.HashMap;

//////////////////// SINGLETON CLASS ////////////////////

public class DialogDateTimeHelper extends AppCompatActivity {

    private static DialogDateTimeHelper mInstance = null;

    private final static int RESULT_SUCCESS = 0;
    private final static String SELECTED_DATE = "SelectedDate";

    private String EVENT_TYPE;

    private EditText mTitle;
    private EditText mDescription;
    private EditText mNote;

    private String userID;

    private TextView mLocation;

    private String mImagePath;
    private String mRecordPath;

    private DatabaseHelper myDb;

    private FirebaseAuth mAuth;
    private Task<Void> reference;

    private String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();


    String date_from = "";
    String time_from = "";

    public void setEVENT_TYPE(String EVENT_TYPE) {
        this.EVENT_TYPE = EVENT_TYPE;
    }

    /////////////////////Event/////////////////////
    public void setTitle(EditText mTitle) {
        this.mTitle = mTitle;
    }
    public void setDescription(EditText mDescription) {
        this.mDescription = mDescription;
    }
    public void setLocation(TextView mLocation) {
        this.mLocation = mLocation;
    }

    ////////////////////Note///////////////////////
    public void setNote(EditText mNote) {
        this.mNote = mNote;
    }

    ////////////////////DRAW///////////////////////
    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    ////////////////////RECORD/////////////////////
    public void setmRecordPath(String mRecordPath) {
        this.mRecordPath = mRecordPath;
    }

    private DialogDateTimeHelper(Context context) {
         myDb = new DatabaseHelper(context);
    }

    public static DialogDateTimeHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DialogDateTimeHelper(context);
        }
        return mInstance;
    }

    public void onTimePicked(int hourOfDay, int minute) {
        if(hourOfDay < 10 && minute < 10) {
            time_from = "0" + hourOfDay + ":" + "0" + minute;
        }
        else if(hourOfDay < 10 && minute >= 10) {
            time_from = "0" + hourOfDay + ":" + minute;
        }
        else if(hourOfDay >= 10 && minute < 10) {
            time_from = hourOfDay + ":" + "0" + minute;
        }
        else if(hourOfDay >= 10 && minute >= 10) {
            time_from = hourOfDay + ":" + minute;
        }
//            time_from = startTime;
        commitDataEvent();
    }

    public void onDatePicked(int year, int month, int day) {
        date_from = day + "/" + month + "/" + year;
        commitDataEvent();
    }

    void commitDataEvent() {
        if (date_from.isEmpty() || time_from.isEmpty()) {
            return;
        }

        switch (EVENT_TYPE) {
            case "Event":
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                String location = mLocation.getText().toString();

                mAuth = FirebaseAuth.getInstance();
//                mRef.child("Name").child("Somehitng");

                mTitle.getText().clear();
                mDescription.getText().clear();
                break;
            case "Note":
                String note = mNote.getText().toString();
                ArrayList<HashMap<String, String>> contentArray = new ArrayList<HashMap<String, String>>();

                EventContent eventContent = new EventContent(
                        "Note",
                        "21/12/2010",
                        "10:23",
                        contentArray);
                new FirestoreClass().setEventContent(DialogDateTimeHelper.this, eventContent);


//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userUID).child("events");
//                DatabaseReference refContent = ref.child("content");
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        ArrayList<String> youNameArray = new ArrayList<>();
//
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                            String data = snapshot.getKey();
//                            youNameArray.add(data);
//                        }
//
//                        Log.v("asdf", "First data : " + youNameArray.get(0));
//                        Log.v("asdsf", "Second data : " + youNameArray.get(1));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                myDb.insertDataNote(note);
                myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);

                mNote.getText().clear();
                break;
            case "Image":
                myDb.insertDataImage(mImagePath);
                myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);
                break;
            case "Record":
                myDb.insertDataAudio(mRecordPath);
                myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);
                break;
        }

        Intent intent = new Intent();
        intent.putExtra(SELECTED_DATE, date_from);
        setResult(RESULT_SUCCESS, intent);

        myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);

        date_from = "";
        time_from = "";

        finish();

        Log.i("DATASAVED", "/////////////---------------Data saved---------------/////////////");
    }


    public void sendNoteData() {
        Toast.makeText(DialogDateTimeHelper.this, "Data sent succesfully to Firebase", Toast.LENGTH_SHORT).show();
    }
}