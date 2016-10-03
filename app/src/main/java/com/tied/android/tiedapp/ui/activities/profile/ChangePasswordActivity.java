package com.tied.android.tiedapp.ui.activities.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

/**
 * Created by hitendra on 9/9/2016.
 */
public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_close;
    TextView txt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.txt_save:
                break;
        }
    }
}
