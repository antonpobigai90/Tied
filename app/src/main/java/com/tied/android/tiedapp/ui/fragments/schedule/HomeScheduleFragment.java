package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeScheduleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = HomeScheduleFragment.class
            .getSimpleName();

    private Bundle bundle;

    private ImageView img_user_picture;


    private TextView btn_got, date, greeting;
    private User user;

    private FragmentInterationListener fragmentInterationListener;

    public HomeScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_landing, container, false);
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
            fragmentInterationListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view) {

        bundle = getArguments();

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        btn_got = (TextView) view.findViewById(R.id.btn_got);
        greeting = (TextView) view.findViewById(R.id.greeting);
        date = (TextView) view.findViewById(R.id.date);

        btn_got.setOnClickListener(this);
        date.setOnClickListener(this);
        img_user_picture.setOnClickListener(this);

        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER);
        user = gson.fromJson(user_json, User.class);

        if (user.getAvatar_uri() != null) {
            Uri myUri = Uri.parse(user.getAvatar_uri());
            img_user_picture.setImageURI(myUri);
        }else{
            Picasso.with(getActivity()).
                    load(Constants.GET_AVATAR_ENDPOINT+"avatar_"+user.getId()+".jpg")
                    .resize(35,35)
                    .into(img_user_picture);
        }

        Calendar cal = Calendar.getInstance();

        String greet = getTimeOfTheDay() +", "+user.getLast_name();
        greeting.setText(greet);

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        String date_string = format.format(Date.parse(cal.getTime().toString()));
        date.setText(date_string);
    }

    private String getTimeOfTheDay(){
        String time_of_the_day = "";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            time_of_the_day = "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            time_of_the_day =  "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            time_of_the_day =  "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            time_of_the_day = "Good Night";
        }
        return time_of_the_day;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_got:
                nextAction(Constants.Schedules, bundle);
                break;
        }
    }
}