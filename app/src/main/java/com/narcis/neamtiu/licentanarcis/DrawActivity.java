package com.narcis.neamtiu.licentanarcis;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.Draw;
import com.narcis.neamtiu.licentanarcis.util.PaintFileHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity {

    class DialogDateTimeListener implements DialogDateTime.Listener {

        String date_from = "";
        String time_from = "";

        @Override
        public void onTimePicked(int hourOfDay, int minute) {

            if(hourOfDay < 10 && minute < 10){

                String startTime = "0" + hourOfDay + ":" + "0" + minute;
                time_from = startTime;

            }else if(hourOfDay < 10 && minute >= 10){

                String startTime =  "0" + hourOfDay + ":" + minute;
                time_from = startTime;

            }else if(hourOfDay >= 10 && minute < 10){

                String startTime = hourOfDay + ":" + "0" + minute;
                time_from = startTime;

            }else if(hourOfDay >= 10 && minute >= 10) {

                String startTime = hourOfDay + ":" + minute;
                time_from = startTime;

            }
//            time_from = startTime;

            commitData();
        }

        @Override
        public void onDatePicked(int year, int month, int day) {
            String startDate = day + "/" + month + "/" + year;

            date_from = startDate;

            commitData();
        }

        void commitData() {

            if (date_from.isEmpty() || time_from.isEmpty()){

                return;

            }

            String event_type = "Note";

            mPath = paintHelper.saveImage();

            myDb.insertDataImage(mPath);
            myDb.insertDataTodoEvent(event_type, date_from, time_from);

            date_from = "";
            time_from = "";

        }
    }

    private PaintFileHelper paintHelper;
    private int defaultColor;
    private int STORAGE_PERMISSION_CODE = 1;
    private Button change_color_button, redo_button, undo_button, clear_button, save_button;
    private String mPath;

    private DialogDateTimeListener mDialogDateTimeListener = new DialogDateTimeListener();

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        myDb = new DatabaseHelper(getApplicationContext());

        paintHelper = findViewById(R.id.paintView);
        change_color_button = findViewById(R.id.change_color_button);
        redo_button = findViewById(R.id.redo_button);
        undo_button = findViewById(R.id.undo_button);
        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView textView = findViewById(R.id.current_pen_size);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        paintHelper.initialise(displayMetrics);

        DialogDateTime.registerListener(mDialogDateTimeListener);

        textView.setText("Pen size: " + seekBar.getProgress());

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

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(DrawActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    requestStoragePermission();

                }

                DialogDateTime.onTimeSelectedClick(DrawActivity.this);
                DialogDateTime.onDateSelectedClick(DrawActivity.this);

                paintHelper.clear();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                paintHelper.setStrokeWidth(seekBar.getProgress());
                textView.setText("Pen size: " + seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {

        DialogDateTime.unregisterListener(mDialogDateTimeListener);

        super.onDestroy();
    }


    private void requestStoragePermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

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

        }else{

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Access granted", Toast.LENGTH_LONG).show();

            }else{

                Toast.makeText(this, "Acces denied", Toast.LENGTH_LONG).show();

            }

        }

    }

    private void openColourPicker(){

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
    protected void onStop() {
        super.onStop();
    }
}
