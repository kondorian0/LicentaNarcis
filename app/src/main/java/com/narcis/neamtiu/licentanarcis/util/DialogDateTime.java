package com.narcis.neamtiu.licentanarcis.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.LinkedList;
import java.util.List;

public class DialogDateTime extends AppCompatActivity
{
    public static MaterialCalendarView widget;

    public static LocalTime mTime = LocalTime.now();
    public static LocalDate mDate = LocalDate.now();

    public static int hour1 = mTime.getHour();
    public static int minute1 =  mTime.getMinute();

    public interface Listener
    {
        void onTimePicked(int hourOfDay, int minute);
        void onDatePicked(int year, int month, int day);
    }

    static private List<Listener> mListeners = new LinkedList<>();

    static public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    static public void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }

    public static void showDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callback)
    {
        int startYear =  mDate.getYear();
        int startMonth = mDate.getMonthValue();
        int startDay = mDate.getDayOfMonth();

        DatePickerDialog dialog = new DatePickerDialog(context, callback, startYear, startMonth, startDay);

        dialog.show();
    }

    public static void onDateSelectedClick(Context context)
    {
        showDatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
            {
                for (Listener l : mListeners) {
                    l.onDatePicked(i, i1, i2);
                }
            }
        });
    }

    public static void showTimePickerDialog(final Context context, LocalTime calendar, TimePickerDialog.OnTimeSetListener callback)
    {
        TimePickerDialog dialog = new TimePickerDialog(context, 0, callback, hour1, minute1, true);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                Toast.makeText(context, "Data saved", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    public static void onTimeSelectedClick(final Context context)
    {
        showTimePickerDialog(context, mTime, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                for (Listener l : mListeners)
                {
                    l.onTimePicked(hourOfDay, minute);
                }
            }
        });
    }
}