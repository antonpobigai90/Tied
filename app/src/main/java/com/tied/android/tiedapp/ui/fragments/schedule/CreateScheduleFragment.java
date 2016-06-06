package com.tied.android.tiedapp.ui.fragments.schedule;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateScheduleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CreateScheduleFragment.class
            .getSimpleName();

    private Bundle bundle;
    // Reference to our image view we will use
    public ImageView img_user_picture;

    public CreateScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initComponent(View view) {

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            if (user.getAvatar_uri() != null) {
                Uri myUri = Uri.parse(user.getAvatar_uri());
                img_user_picture.setImageURI(myUri);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}