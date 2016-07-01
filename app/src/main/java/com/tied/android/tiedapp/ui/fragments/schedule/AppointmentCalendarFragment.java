package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

//import com.imanoweb.calendarview.CalendarListener;
//import com.imanoweb.calendarview.CustomCalendarView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AppointmentCalendarFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AppointmentCalendarFragment.class
            .getSimpleName();

    private Bundle bundle;
//    private CustomCalendarView calendarView;

    private TextView txt_create_schedule;

    private FragmentInterationListener fragmentInterationListener;

    public AppointmentCalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_choose_date, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterationListener) {
            fragmentInterationListener = (FragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentInterationListener != null) {
            fragmentInterationListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void initComponent(View view) {
        txt_create_schedule = (TextView) view.findViewById(R.id.txt_create_schedule);
        txt_create_schedule.setOnClickListener(this);

//        //Initialize CustomCalendarView from layout
//        calendarView = (CustomCalendarView) view.findViewById(R.id.calendar_view);
//
//        //Initialize calendar with date
//        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
//
//        //Show Monday as first date of week
//        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
//
//        //Show/hide overflow days of a month
//        calendarView.setShowOverflowDate(false);
//
//        //call refreshCalendar to update calendar the view
//        calendarView.refreshCalendar(currentCalendar);
//
//        //Handling custom calendar events
//        calendarView.setCalendarListener(new CalendarListener() {
//            @Override
//            public void onDateSelected(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onMonthChanged(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
//                Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_create_schedule:

                break;
        }
    }
}