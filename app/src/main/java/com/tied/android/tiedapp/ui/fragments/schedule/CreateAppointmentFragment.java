package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.fragments.DatePickerFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

//import com.imanoweb.calendarview.CustomCalendarView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateAppointmentFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CreateAppointmentFragment.class
            .getSimpleName();

    TextView txt_title, txt_description, txt_date, txt_time, txt_location, txt_reminder;
    ImageView img_avatar, img_plus_date, img_plus1, img_location, img_reminder,img_close;

    private Bundle bundle;
//    private CustomCalendarView calendarView;

    private TextView txt_create_schedule;

    private FragmentInterationListener fragmentInterationListener;

    public CreateAppointmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_appointment, container, false);
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

        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_description = (TextView) view.findViewById(R.id.txt_description);
        txt_date = (TextView) view.findViewById(R.id.date);
        txt_time = (TextView) view.findViewById(R.id.time);
        txt_reminder = (TextView) view.findViewById(R.id.reminder);

        img_close = (ImageView) view.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        img_avatar = (ImageView) view.findViewById(R.id.img_avatar_schedule);

        img_plus_date = (ImageView) view.findViewById(R.id.img_plus_date);
        img_plus_date.setOnClickListener(this);


        img_plus1 = (ImageView) view.findViewById(R.id.img_plus1);
        img_plus1.setOnClickListener(this);

        img_reminder = (ImageView) view.findViewById(R.id.img_reminder);
        img_reminder.setOnClickListener(this);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.img_close:
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                break;
            case R.id.img_plus_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.img_plus1:

                break;
            case R.id.img_reminder:

                break;
            case R.id.txt_create_schedule:
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}