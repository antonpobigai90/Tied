package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Femi on 7/30/2016.
 */
public class LinesActivity extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_line);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);


        initComponent();
    }
    private void initComponent() {

    }
    @Override
    public void onClick(View v) {

    }
}
