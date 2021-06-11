package com.narcis.neamtiu.licentanarcis.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.LinkedList;
import java.util.List;

public class DialogDateTime extends AppCompatActivity {
    static LocalTime mTime = LocalTime.now();
    static LocalDate mDate = LocalDate.now();

    static int mDateYear =  mDate.getYear();
    static int mDateMonthValue = mDate.getMonthValue()-1;
    static int mDateDayOfMonth = mDate.getDayOfMonth();

    static int mTimeHour = mTime.getHour();
    static int mTimeMinute =  mTime.getMinute();

    public interface Listener {
        void onTimePicked(int hourOfDay, int minute);
        void onDatePicked(int year, int month, int day);
    }

    static private final List<Listener> mListeners = new LinkedList<>();

    public static void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    public static void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }

    public static void showDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callback) {
        DatePickerDialog dialog = new DatePickerDialog(context, callback, mDateYear, mDateMonthValue, mDateDayOfMonth);
        dialog.show();
    }

    public static void showTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener callback) {
        TimePickerDialog dialog = new TimePickerDialog(context, 0, callback, mTimeHour, mTimeMinute, true);
        dialog.show();
    }

    public static void onDateSelectedClick(Context context) {
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                for (Listener l : mListeners) {
                    l.onDatePicked(year, month, dayOfMonth);
                }
            }
        };
        showDatePickerDialog(context, mDateSetListener);
    }


    public static void onTimeSelectedClick(Context context) {
        TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                for (Listener l : mListeners) {
                    l.onTimePicked(hourOfDay, minute);
                }
            }
        };
        showTimePickerDialog(context, mTimeSetListener);
    }
}