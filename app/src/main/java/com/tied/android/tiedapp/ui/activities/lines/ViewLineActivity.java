package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Femi on 7/30/2016.
 */
public class ViewLineActivity extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    LinearLayout back_layout;
    RelativeLayout clients_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_view);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        initComponent();
    }

    private void initComponent() {
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);
        clients_layout = (RelativeLayout) findViewById(R.id.clients_layout);
        clients_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                MyUtils.startActivity(this, MainActivity.class);
                break;
            case R.id.clients_layout:
                MyUtils.startActivity(this, LineClientList.class);
                break;

        }

    }
}
