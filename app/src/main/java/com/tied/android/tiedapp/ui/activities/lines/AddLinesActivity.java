package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Femi on 7/30/2016.
 */
public class AddLinesActivity extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    TextView btn_add;
    LinearLayout back_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_add);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        initComponent();
    }

    private void initComponent() {
        btn_add = (TextView) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                MyUtils.startActivity(this, ViewLineActivity.class);
                break;
            case R.id.back_layout:
                MyUtils.startActivity(this, MainActivity.class);
                break;

        }
    }
}
