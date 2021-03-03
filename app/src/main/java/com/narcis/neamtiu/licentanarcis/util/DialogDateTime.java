package com.narcis.neamtiu.licentanarcis.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DialogDateTime extends AppCompatActivity {

    public static MaterialCalendarView widget;

    public static Calendar mTime= Calendar.getInstance();

    public static int hour1 = mTime.get(Calendar.HOUR_OF_DAY);
    public static int minute1 =  mTime.get(Calendar.MINUTE);


    public interface Listener {

        void onTimePicked(int hourOfDay, int minute);
        void onDatePicked(int year, int month, int day);

    }

    static private List<Listener> mListeners = new LinkedList();

    static public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    static public void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }

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

        showDatePickerDialog(context, widget.getSelectedDate(), new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                for (Listener l : mListeners) {
                    l.onDatePicked(i, i1, i2);
                }
            }
        });
    }

    public static void showTimePickerDialog(final Context context, Calendar calendar, TimePickerDialog.OnTimeSetListener callback){

        TimePickerDialog dialog = new TimePickerDialog(

                context, 0, callback, hour1, minute1, true

        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {



            }
        });

        dialog.show();


    }

    public static void onTimeSelectedClick(final Context context){

        showTimePickerDialog(context, mTime, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                for (Listener l : mListeners) {
                    l.onTimePicked(hourOfDay, minute);
                }
            }
        });
    }
}