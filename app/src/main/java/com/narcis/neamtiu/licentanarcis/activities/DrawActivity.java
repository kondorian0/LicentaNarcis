package com.narcis.neamtiu.licentanarcis.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTimeHelper;
import com.narcis.neamtiu.licentanarcis.util.PaintFileHelper;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity
        implements DialogDateTime.Listener {

    private String EVENT_TYPE = "Image";

    private DialogDateTimeHelper mDateTimeHelper;

    private PaintFileHelper paintHelper;
    private int defaultColor;
    private int STORAGE_PERMISSION_CODE = 1;
    private Button change_color_button, redo_button, undo_button, clear_button, save_button;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        mDateTimeHelper = DialogDateTimeHelper.getInstance(getApplicationContext());
        mDateTimeHelper.setEVENT_TYPE(EVENT_TYPE);
//        mDateTimeHelper.setmImagePath(mImagePath);

        paintHelper = findViewById(R.id.paintView);
        change_color_button = findViewById(R.id.change_color_button);
        redo_button = findViewById(R.id.redo_button);
        undo_button = findViewById(R.id.undo_button);
        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);

        WindowMetrics displayMetrics = getWindowManager().getCurrentWindowMetrics();

        paintHelper.initialise(displayMetrics);

        DialogDateTime.registerListener(this);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(DrawActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                }

                mDateTimeHelper.setmImagePath(paintHelper.saveImage());

                DialogDateTime.onTimeSelectedClick(DrawActivity.this);
                DialogDateTime.onDateSelectedClick(DrawActivity.this);
                paintHelper.clear();
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
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
        DialogDateTime.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTimePicked(int hourOfDay, int minute) {
        mDateTimeHelper.onTimePicked(hourOfDay, minute);
    }

    @Override
    public void onDatePicked(int year, int month, int day) {
        mDateTimeHelper.onDatePicked(year, month, day);
    }
}
