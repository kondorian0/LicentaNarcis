package com.narcis.neamtiu.licentanarcis;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.narcis.neamtiu.licentanarcis.database.DatabaseHelper;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTime;
import com.narcis.neamtiu.licentanarcis.util.Draw;
import com.narcis.neamtiu.licentanarcis.util.PaintFileHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity
{
    public final static int RESULT_SUCCESS = 0;
    public final static String SELECTED_DATE = "SelectedDate";
    public final static String EVENT_TYPE = "Image";

    class DialogDateTimeListener implements DialogDateTime.Listener
    {
        String date_from = "";
        String time_from = "";

        @Override
        public void onTimePicked(int hourOfDay, int minute)
        {
            if(hourOfDay < 10 && minute < 10)
            {
                time_from = "0" + hourOfDay + ":" + "0" + minute;
            }
            else if(hourOfDay < 10 && minute >= 10)
            {
                time_from = "0" + hourOfDay + ":" + minute;
            }
            else if(hourOfDay >= 10 && minute < 10)
            {
                time_from = hourOfDay + ":" + "0" + minute;
            }
            else if(hourOfDay >= 10 && minute >= 10)
            {
                time_from = hourOfDay + ":" + minute;
            }
//            time_from = startTime;
            commitData();
        }

        @Override
        public void onDatePicked(int year, int month, int day)
        {
            date_from = day + "/" + month + "/" + year;
            commitData();
        }

        void commitData()
        {
            if (date_from.isEmpty() || time_from.isEmpty())
            {
                return;
            }

            mPath = paintHelper.saveImage();

            myDb.insertDataImage(mPath);
            myDb.insertDataTodoEvent(EVENT_TYPE, date_from, time_from);

            Intent intent = new Intent();
            intent.putExtra(SELECTED_DATE, date_from);
            setResult(RESULT_SUCCESS, intent);

            date_from = "";
            time_from = "";

            finish();
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        myDb = new DatabaseHelper(getApplicationContext());

        paintHelper = findViewById(R.id.paintView);
        change_color_button = findViewById(R.id.change_color_button);
        redo_button = findViewById(R.id.redo_button);
        undo_button = findViewById(R.id.undo_button);
        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);
        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView textView = findViewById(R.id.current_pen_size);

        WindowMetrics displayMetrics = getWindowManager().getCurrentWindowMetrics();

        paintHelper.initialise(displayMetrics);

        DialogDateTime.registerListener(mDialogDateTimeListener);
        String penSize = "Pen size: " + seekBar.getProgress();
        textView.setText(penSize);

        change_color_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openColourPicker();
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                paintHelper.clear();
            }
        });

        undo_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                paintHelper.undo();
            }
        });

        redo_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                paintHelper.redo();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(ContextCompat.checkSelfPermission(DrawActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestStoragePermission();
                }

                DialogDateTime.onTimeSelectedClick(DrawActivity.this);
                DialogDateTime.onDateSelectedClick(DrawActivity.this);

                paintHelper.clear();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                paintHelper.setStrokeWidth(seekBar.getProgress());
                String penSize = "Pen size: " + seekBar.getProgress();
                textView.setText(penSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        DialogDateTime.unregisterListener(mDialogDateTimeListener);
        super.onDestroy();
    }


    private void requestStoragePermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Needed to save image")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            ActivityCompat.requestPermissions(DrawActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Access granted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Acces denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openColourPicker()
    {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener()
        {
            @Override
            public void onCancel(AmbilWarnaDialog dialog)
            {
                Toast.makeText(DrawActivity.this, "Unavailable", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color)
            {
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
