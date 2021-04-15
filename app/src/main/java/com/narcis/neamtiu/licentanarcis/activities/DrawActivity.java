package com.narcis.neamtiu.licentanarcis.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.EventHelper;
import com.narcis.neamtiu.licentanarcis.util.PaintFileHelper;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity {

    private String EVENT_TYPE = Constants.DRAW_EVENT;

    private PaintFileHelper paintHelper;
    private int defaultColor;
    private int STORAGE_PERMISSION_CODE = 1;
    private Button change_color_button, redo_button, undo_button, clear_button, save_button;

    String mCurrentSelectedTime = new String();
    String mCurrentSelectedDate = new String();

    DatePickerDialog mDateDialog = null;
    TimePickerDialog mTimeDialog = null;

    void commitData() {

        final String filename = "image_"+ System.currentTimeMillis() + ".jpg";
        final String mimeType = "image/jpeg";
        final String directory = Environment.DIRECTORY_PICTURES + "/CalendarNarcis";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        values.put(MediaStore.Images.Media.RELATIVE_PATH, directory);

        ContentResolver resolver = getContentResolver();
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        paintHelper.compressImage(fileOutputStream);

        FirestoreManager.getInstance().uploadImageToCloudStorage(paintHelper, filename, imageUri,
                new FirestoreManager.OnImageUploadListener() {
                    @Override
                    public void onImageUploaded(String imageUrl) {
                        final String userId = FirestoreManager.getInstance().getCurrentUserID();
                        EventData drawEvent = new EventData(userId, Constants.DRAW_EVENT,
                                mCurrentSelectedDate, mCurrentSelectedTime, imageUrl);
                        FirestoreManager.getInstance().registerDataEvent(drawEvent);
                    }
                });

        paintHelper.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        paintHelper = findViewById(R.id.paintView);
        change_color_button = findViewById(R.id.change_color_button);
        redo_button = findViewById(R.id.redo_button);
        undo_button = findViewById(R.id.undo_button);
        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);

        WindowMetrics displayMetrics = getWindowManager().getCurrentWindowMetrics();

        paintHelper.initialise(displayMetrics);

        mDateDialog = new DatePickerDialog(DrawActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mCurrentSelectedDate = EventHelper.formatDatePicked(year, month, day);
                mTimeDialog.show();
            }
        }, LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());

        mTimeDialog = new TimePickerDialog(DrawActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCurrentSelectedTime = EventHelper.formatTimePicked(hourOfDay, minute);
                commitData();
            }
        }, LocalTime.now().getHour(), LocalTime.now().getMinute(), true);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(DrawActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                }
                mDateDialog.show();
            }
        });

        change_color_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColourPicker();
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintHelper.clear();
            }
        });

        undo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintHelper.undo();
            }
        });

        redo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintHelper.redo();
            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Needed to save image")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(DrawActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Access granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Acces denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openColourPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(DrawActivity.this, "Unavailable", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                paintHelper.setColor(color);
            }
        });

        ambilWarnaDialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DrawActivity.this, MainActivity.class));
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
