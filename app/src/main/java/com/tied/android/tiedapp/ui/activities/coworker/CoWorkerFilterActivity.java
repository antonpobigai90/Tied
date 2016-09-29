package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.CoWorkerGAdapter;
import com.tied.android.tiedapp.ui.adapters.CoWorkerHAdapter;
import com.tied.android.tiedapp.util.MyUtils;

public class CoWorkerFilterActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = CoWorkerFilterActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    TextView txt_apply, txt_clear;
    RelativeLayout territory_layout, line_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_worker_filter);

        bundle = new Bundle();
        user = MyUtils.getUserFromBundle(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        txt_apply = (TextView) findViewById(R.id.txt_apply);
        txt_apply.setOnClickListener(this);

        txt_clear = (TextView) findViewById(R.id.txt_clear);
        txt_clear.setOnClickListener(this);

        territory_layout = (RelativeLayout) findViewById(R.id.territory_layout);
        territory_layout.setOnClickListener(this);

        line_layout = (RelativeLayout) findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
            case R.id.txt_apply:

                break;
            case R.id.txt_clear:

                break;
            case R.id.territory_layout:
                bundle.putInt(Constants.SHOW_TERRITORY, 1);
                MyUtils.startActivity(this, CoWorkerTerritoriesActivity.class, bundle);
                break;
            case R.id.line_layout:
                bundle.putInt(Constants.SHOW_LINE, 1);
                MyUtils.startActivity(this, CoWorkerLinesActivity.class, bundle);
                break;
        }
    }
}
