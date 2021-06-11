package com.narcis.neamtiu.licentanarcis.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.EventHelper;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {
    private AppCompatButton record_button, stop_record_button, play_button, stop_play_button;
    private AppCompatButton save_record_button, delete_record_button;
    private AppCompatImageView recording, not_recording;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    private boolean isRecording = false;

    private static final String LOG_TAG = "AudioRecording";

    private final int REQUEST_PERMISSIOON_CODE = 1;

    private String mCurrentSelectedTime = new String();
    private String mCurrentSelectedDate = new String();

    private DatePickerDialog mDateDialog = null;
    private TimePickerDialog mTimeDialog = null;

    private String filename = "audio_"+ System.currentTimeMillis() + ".mp3";
    private String mimeType = "audio/mp3";
    private String directory = Environment.DIRECTORY_MUSIC + "/CalendarNarcis";
    private Uri audioUri = null;
    private ParcelFileDescriptor file = null;
    private ContentResolver resolver;

    void setupFileData() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Audio.Media.MIME_TYPE, mimeType);
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, directory);

        resolver = getContentResolver();
        audioUri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

        try {
            file = resolver.openFileDescriptor(audioUri, "w");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void commitData() {
        FirestoreManager.getInstance().uploadFileToCloudStorage(filename, audioUri,
                new FirestoreManager.OnImageUploadListener() {
                    @Override
                    public void onFileUpload(String audioUrl) {
                        final String userId = FirestoreManager.getInstance().getCurrentUserID();
                        EventData recordEvent = new EventData(userId, Constants.RECORD_EVENT,
                                mCurrentSelectedDate, mCurrentSelectedTime, filename, audioUrl);
                        FirestoreManager.getInstance().registerDataEvent(recordEvent);
                    }
                });
    }

    private void setupMediaRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOutputFile(file.getFileDescriptor());
    }

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

        recording.setVisibility(View.INVISIBLE);
        not_recording.setVisibility(View.VISIBLE);

        if(!checkPermissionFromDevice()) {
            requestPermission();
        }

        mDateDialog = new DatePickerDialog(RecordActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mCurrentSelectedDate = EventHelper.formatDatePicked(year, month, day);
                mTimeDialog.show();
            }
        }, LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth());

        mTimeDialog = new TimePickerDialog(RecordActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCurrentSelectedTime = EventHelper.formatTimePicked(hourOfDay, minute);
                commitData();
            }
        }, LocalTime.now().getHour(), LocalTime.now().getMinute(), true);

        save_record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    isRecording = false;
                }
                mDateDialog.show();
            }
        });

        record_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {
                if(checkPermissionFromDevice()) {
                    record_button.setEnabled(false);
                    stop_record_button.setEnabled(true);
                    play_button.setEnabled(false);
                    stop_play_button.setEnabled(false);
                    not_recording.setVisibility(View.INVISIBLE);
                    recording.setVisibility(View.VISIBLE);

                    setupFileData();
                    setupMediaRecorder();

                    try {
                        mRecorder.prepare();
                        mRecorder.start();

                        isRecording = true;

                        Toast.makeText(getApplicationContext(),"Recording", Toast.LENGTH_LONG).show();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
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
                if (file.getFileDescriptor()==null) {
                    Toast.makeText(getApplicationContext(),"No Record", Toast.LENGTH_LONG).show();
                    return;
                }

                play_button.setEnabled(false);
                stop_play_button.setEnabled(true);
                stop_record_button.setEnabled(false);
                record_button.setEnabled(true);

                mPlayer = new MediaPlayer();

                try {
                    mPlayer.setDataSource(file.getFileDescriptor());
                    mPlayer.prepare();
                    mPlayer.start();

                    Toast.makeText(getApplicationContext(),"Playing", Toast.LENGTH_LONG).show();
                }catch (IOException e) {
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

                if(mPlayer != null) {
                    mPlayer.release();
                    mPlayer = null;
                }
            }
        });

        delete_record_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);

                builder.setMessage("Do you want to delete the recorded audio?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(file.getFileDescriptor().valid()) {
                                    resolver.delete(audioUri, null);
                                    Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIOON_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return  write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, REQUEST_PERMISSIOON_CODE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RecordActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}