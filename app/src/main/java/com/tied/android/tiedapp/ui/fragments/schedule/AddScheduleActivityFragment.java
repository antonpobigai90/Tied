package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.client.ClientActivity;
import com.tied.android.tiedapp.ui.activities.client.SelectClientActivity;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddScheduleActivityFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AddScheduleActivityFragment.class
            .getSimpleName();

    private Bundle bundle;
    TextView txt_done;
    RelativeLayout sale_layout, schedule_layout, client_layout, line_layout, employee_layout, goal_layout;

    private User user;

    private FragmentIterationListener fragmentIterationListener;

    public AddScheduleActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_add_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view){

        bundle = getArguments();

        txt_done = (TextView) view.findViewById(R.id.txt_done);
        txt_done.setOnClickListener(this);

        sale_layout = (RelativeLayout) view.findViewById(R.id.sale_layout);
        sale_layout.setOnClickListener(this);

        schedule_layout = (RelativeLayout) view.findViewById(R.id.schedule_layout);
        schedule_layout.setOnClickListener(this);

        client_layout = (RelativeLayout) view.findViewById(R.id.client_layout);
        client_layout.setOnClickListener(this);

        line_layout = (RelativeLayout) view.findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);

        employee_layout = (RelativeLayout) view.findViewById(R.id.employee_layout);
        employee_layout.setOnClickListener(this);

        goal_layout = (RelativeLayout) view.findViewById(R.id.goal_layout);
        goal_layout.setOnClickListener(this);



    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            fragmentIterationListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentIterationListener != null) {
            fragmentIterationListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txt_done:
                nextAction(Constants.CreateSchedule, bundle);
                break;
            case R.id.sale_layout:

                break;
            case R.id.schedule_layout:
                intent = new Intent(getActivity(), SelectClientActivity.class);
                startActivity(intent);
                break;
            case R.id.client_layout:
                intent = new Intent(getActivity(), ClientActivity.class);
                startActivity(intent);
                break;
            case R.id.employee_layout:

                break;
            case R.id.line_layout:
//                intent = new Intent(getActivity(), AddLinesActivity.class);
//                startActivity(intent);
                break;
            case R.id.goal_layout:

                break;
        }
    }
}