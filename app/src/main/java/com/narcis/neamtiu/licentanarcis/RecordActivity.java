package com.narcis.neamtiu.licentanarcis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.AudioFileHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {

    public final static int RESULT_SUCCESS = 0;
    public final static String SELECTED_DATE = "SelectedDate";
    public final static String EVENT_TYPE = "Audio";

    class DialogDateTimeListener implements DialogDateTime.Listener {

        String date_from = "";
        String time_from = "";

        @Override
        public void onTimePicked(int hourOfDay, int minute) {

            if(hourOfDay < 10 && minute < 10){

                time_from = "0" + hourOfDay + ":" + "0" + minute;

            }else if(hourOfDay < 10 && minute >= 10){

                time_from = "0" + hourOfDay + ":" + minute;

            }else if(hourOfDay >= 10 && minute < 10){

                time_from = hourOfDay + ":" + "0" + minute;

            }else if(hourOfDay >= 10 && minute >= 10) {

                time_from = hourOfDay + ":" + minute;

            }
//            time_from = startTime;

            commitData();
        }

        @Override
        public void onDatePicked(int year, int month, int day) {

            date_from = day + "/" + month + "/" + year;

            commitData();
        }

        void commitData() {

            if (date_from.isEmpty() || time_from.isEmpty()){

                return;

            }

            myDb.insertDataAudio(path);
            myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);

            Intent intent = new Intent();
            intent.putExtra(SELECTED_DATE, date_from);
            setResult(RESULT_SUCCESS, intent);

            date_from = "";
            time_from = "";

            finish();
        }
    }

    private AppCompatButton record_button, stop_record_button, play_button, stop_play_button;
    private AppCompatButton save_record_button, delete_record_button;
    private AppCompatImageView recording, not_recording;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    private boolean isRecording = false;

    private static final String LOG_TAG = "AudioRecording";
    //    private static String mFileName = null;
    private final int REQUEST_PERMISSIOON_CODE = 1;

    private String path;

    private DatabaseHelper myDb;

    private DialogDateTimeListener mDialogDateTimeListener = new DialogDateTimeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        myDb = new DatabaseHelper(getApplicationContext());

        record_button = findViewById(R.id.record_button);
        stop_record_button = findViewById(R.id.stop_record_button);
        play_button = findViewById(R.id.play_button);
        stop_play_button = findViewById(R.id.stop_play_button);
        save_record_button = findViewById(R.id.save_record_button);
        delete_record_button = findViewById(R.id.delete_record_button);
        recording = findViewById(R.id.recording_image);
        not_recording = findViewById(R.id.no_recording_image);

        recording.setVisibility(View.INVISIBLE);
        not_recording.setVisibility(View.VISIBLE);

        AddData();

        DialogDateTime.registerListener(mDialogDateTimeListener);

        //Request RunTime permission
        if(!checkPermissionFromDevice()){

            requestPermission();

        }

        record_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(checkPermissionFromDevice()){

                    record_button.setEnabled(false);
                    stop_record_button.setEnabled(true);
                    play_button.setEnabled(false);
                    stop_play_button.setEnabled(false);
                    not_recording.setVisibility(View.INVISIBLE);
                    recording.setVisibility(View.VISIBLE);

                    path = AudioFileHelper.saveAudio();

                    try{

                        setupMediaRecorder();
                        mRecorder.prepare();
                        mRecorder.start();

                        isRecording = true;

                        Toast.makeText(getApplicationContext(),"Recording", Toast.LENGTH_LONG).show();

                    }catch (IOException e) {

                        e.printStackTrace();

                    }

                }else{

                    requestPermission();

                }
            }

        });

        stop_record_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                stop_record_button.setEnabled(false);
                play_button.setEnabled(true);
                stop_play_button.setEnabled(true);
                record_button.setEnabled(true);

                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;

                isRecording = false;

                recording.setVisibility(View.INVISIBLE);
                not_recording.setVisibility(View.VISIBLE);

            }

        });

        play_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (path==null){

                    Toast.makeText(getApplicationContext(),"No Record", Toast.LENGTH_LONG).show();
                    return;

                }

                play_button.setEnabled(false);
                stop_play_button.setEnabled(true);
                stop_record_button.setEnabled(false);
                record_button.setEnabled(true);

                mPlayer = new MediaPlayer();

                try {

                    mPlayer.setDataSource(path);
                    mPlayer.prepare();
                    mPlayer.start();

                    Toast.makeText(getApplicationContext(),"Playing", Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Log.e(LOG_TAG, "prepare() failed");

                }
            }

        });

        stop_play_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                stop_record_button.setEnabled(false);
                record_button.setEnabled(true);
                stop_play_button.setEnabled(false);
                play_button.setEnabled(true);

                if(mPlayer != null){

                    mPlayer.release();
                    mPlayer = null;

                }
            }
        });

        delete_record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final File forDelete = new File(path);

                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);

                builder.setMessage("Do you want to delete the recorded audio?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                if(forDelete.exists()) {

                                    forDelete.delete();

                                }
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

    private void setupMediaRecorder() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(path);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case REQUEST_PERMISSIOON_CODE:{

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();

            }break;

        }

    }

    private boolean checkPermissionFromDevice(){

        int write_external_storage_result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return  write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{

                WRITE_EXTERNAL_STORAGE,
                RECORD_AUDIO

        }, REQUEST_PERMISSIOON_CODE);

    }

    public void AddData(){
        save_record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRecording){

                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;

                    isRecording = false;

                }

                DialogDateTime.onTimeSelectedClick(RecordActivity.this);
                DialogDateTime.onDateSelectedClick(RecordActivity.this);

            }
        });
    }

    @Override
    protected void onDestroy() {

        DialogDateTime.unregisterListener(mDialogDateTimeListener);

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}