package com.tied.android.tiedapp.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String[] MONTHS_LIST = {"January", "Febuary", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    String[] WEEK_LIST = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String name_month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String month_name = MONTHS_LIST[view.getMonth()];
        GregorianCalendar gregorianCalendar = new GregorianCalendar(view.getYear(), view.getMonth(), view.getDayOfMonth() - 1);

        int dayOfWeek = gregorianCalendar.get(gregorianCalendar.DAY_OF_WEEK);
        String dayOfWeekName = WEEK_LIST[dayOfWeek];

        TextView tv1 = (TextView) getActivity().findViewById(R.id.date);
        tv1.setText("" + dayOfWeekName + " " + month_name + " " + view.getDayOfMonth() + ", " + view.getYear());

        String selected = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
        TextView tv2 = (TextView) getActivity().findViewById(R.id.date_selected);
        tv2.setText(selected);
    }
}
