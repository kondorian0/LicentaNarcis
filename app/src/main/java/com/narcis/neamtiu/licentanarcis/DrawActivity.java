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
import com.narcis.neamtiu.licentanarcis.util.PaintFileHelper;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity {

    private PaintFileHelper paintHelper;
    private int defaultColor;
    private int STORAGE_PERMISSION_CODE = 1;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button change_color_button, redo_button, undo_button, clear_button, save_button;

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

                String path = paintHelper.saveImage();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();

                String event_type = "Image";
                String date_from = "yy/yy/yy";
                String date_to = "yy/yy/yyyy";
                String time_from = "zz:zz:zz";
                String time_to = "zz:zz:zz";

                myDb.insertDataImage(path);
                myDb.insertDataTodoEvent(event_type, date_from, date_to, time_from, time_to);

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


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//
//            case R.id.clear_button:
//                paintView.clear();
//                return true;
//            case R.id.undo_button:
//                paintView.undo();
//                return true;
//            case R.id.redo_button:
//                paintView.redo();
//                return true;
//            case R.id.save_button:
//
//                if(ContextCompat.checkSelfPermission(DrawActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
//                    requestStoragePermission();
//
//                }
//                paintView.saveImage();
//                return true;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }

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
}
