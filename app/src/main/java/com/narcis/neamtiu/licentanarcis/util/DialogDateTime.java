package com.narcis.neamtiu.licentanarcis.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.narcis.neamtiu.licentanarcis.NoteActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DialogDateTime extends AppCompatActivity {

    public static MaterialCalendarView widget;

    public static Calendar mTime= Calendar.getInstance();

    public static int hour1 = mTime.get(Calendar.HOUR_OF_DAY);
    public static int minute1 =  mTime.get(Calendar.MINUTE);

    public static String timeHourMinute;
    public static String startDate;

    public static void showDatePickerDialog(Context context, CalendarDay day,
                                            DatePickerDialog.OnDateSetListener callback) {

        if (day == null) {

            day = CalendarDay.today();

        }
        DatePickerDialog dialog = new DatePickerDialog(

                context, callback, day.getYear(), day.getMonth(), day.getDay()

        );

        dialog.show();

    }

    public static void onDateSelectedClick(final Context context) {

        showDatePickerDialog(context, widget.getSelectedDate(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                startDate = dayOfMonth+"/"+monthOfYear+"/"+year;

                Toast.makeText(context,dayOfMonth+"/"+monthOfYear+"/"+year, Toast.LENGTH_LONG).show();
//                widget.setSelectedDate(CalendarDay.from(year, monthOfYear, dayOfMonth));

            }
        });
    }

    public static void showTimePickerDialog(Context context, Calendar calendar, TimePickerDialog.OnTimeSetListener callback){

        TimePickerDialog dialog = new TimePickerDialog(
                context, 0, callback, hour1, minute1, true

        );

        dialog.show();


    }
    public static void onTimeSelectedClick(final Context context){
        showTimePickerDialog(context, mTime, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                hour1 = hourOfDay;
                minute1 = minute;
                Toast.makeText(context,hourOfDay+" : "+minute, Toast.LENGTH_LONG).show();

                timeHourMinute = hourOfDay+" : "+minute;

            }
        });
    }

    public static String getTimeHourMinute(){

        return timeHourMinute;

    }

    public static String getStartDate(){

        return startDate;

    }
}