package com.tied.android.tiedapp.ui.activities.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class NotificationProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public FragmentIterationListener mListener;

    private Bundle bundle;
    private ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_notifications);

        initComponent();
    }

    public void initComponent() {
        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                finish();
                break;
        }
    }
}
