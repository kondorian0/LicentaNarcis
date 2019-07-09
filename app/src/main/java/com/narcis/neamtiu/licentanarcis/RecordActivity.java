package com.narcis.neamtiu.licentanarcis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class RecordActivity extends AppCompatActivity {

    Button record_button, stop_record_button, play_button, stop_play_button, save_record_button, delete_record_button;
    ImageView recording, not_recording;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private String pathSave = "";

    private final int REQUEST_PERMISSIOON_CODE = 1000;


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

                    pathSave = Environment.getExternalStorageDirectory()
                            .getAbsolutePath()+ "/"
                            + UUID.randomUUID().toString()+ "_audio_record.3gp";

                    setupMediaRecorder();

                    try{

                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    }catch (IOException e) {

                        e.printStackTrace();

                    }

                    play_button.setEnabled(false);
                    stop_play_button.setEnabled(false);

                    }else{

                        requestPermission();

                    }
                }

            });

            stop_record_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    mediaRecorder.stop();

                    play_button.setEnabled(true);
                    stop_play_button.setEnabled(false);

                    record_button.setEnabled(true);
                    stop_record_button.setEnabled(false);

                }

            });

            play_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    stop_play_button.setEnabled(true);
                    stop_record_button.setEnabled(false);
                    record_button.setEnabled(false);

                    mediaPlayer = new MediaPlayer();

                    try {

                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                    mediaPlayer.start();

                }

            });

            stop_play_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    stop_record_button.setEnabled(false);
                    record_button.setEnabled(true);
                    stop_play_button.setEnabled(false);
                    play_button.setEnabled(true);

                    if(mediaPlayer != null){

                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();

                    }

                }

            });

    }

    private void setupMediaRecorder() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(RecordActivity.this, new String[]{

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

        }, REQUEST_PERMISSIOON_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case REQUEST_PERMISSIOON_CODE:{

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

            }break;

        }

    }

    private boolean checkPermissionFromDevice(){

        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return  write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

}
