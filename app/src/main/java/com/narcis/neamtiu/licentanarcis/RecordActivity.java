package com.narcis.neamtiu.licentanarcis;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {

    private AppCompatButton record_button, stop_record_button, play_button, stop_play_button;
    private AppCompatButton save_record_button, delete_record_button;
    private AppCompatImageView recording, not_recording;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    private final int REQUEST_PERMISSIOON_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        record_button = findViewById(R.id.record_button);
        stop_record_button = findViewById(R.id.stop_record_button);
        play_button = findViewById(R.id.play_button);
        stop_play_button = findViewById(R.id.stop_play_button);
        save_record_button = findViewById(R.id.save_record_button);
        delete_record_button = findViewById(R.id.delete_record_button);
        recording = findViewById(R.id.recording_image);
        not_recording = findViewById(R.id.no_recording_image);



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

                    mFileName = getExternalCacheDir().getAbsolutePath();
                    mFileName += "/" + UUID.randomUUID().toString() + "_audiorecordtest.3gp";

                    try{

                        setupMediaRecorder();
                        mRecorder.prepare();
                        mRecorder.start();

                        Toast.makeText(getApplicationContext(),"Recording Started", Toast.LENGTH_LONG).show();

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

                Toast.makeText(getApplicationContext(),"Recording Stopped", Toast.LENGTH_LONG).show();

            }

        });

        play_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                play_button.setEnabled(false);
                stop_play_button.setEnabled(true);
                stop_record_button.setEnabled(false);
                record_button.setEnabled(true);

                mPlayer = new MediaPlayer();

                try {

                    mPlayer.setDataSource(mFileName);
                    mPlayer.prepare();
                    mPlayer.start();

                    Toast.makeText(getApplicationContext(),"Recording Started Playing", Toast.LENGTH_LONG).show();

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

                    Toast.makeText(getApplicationContext(),"Playing Audio Stopped", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void setupMediaRecorder() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(mFileName);

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

}