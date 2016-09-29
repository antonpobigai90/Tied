package com.tied.android.tiedapp.ui.activities.sales;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.GeneralSelectObjectActivity;
import com.tied.android.tiedapp.ui.fragments.sales.AddSalesFragment;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivityAddSales extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    // AddSalesFragment fragment;

    LinearLayout parent;
    int page_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_sales);

        bundle = getIntent().getExtras();
        page_index = bundle.getInt(Constants.SHOW_SALE);

        initComponent();
    }
    private void initComponent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            if (page_index == 0) {
                window.setStatusBarColor(getResources().getColor(R.color.green_color1));
            } else if (page_index == 1){
                window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.gradient));
            }
        }

        parent = (LinearLayout) findViewById(R.id.parent);
        if (page_index == 0) {
            parent.setBackgroundResource(R.drawable.background_green);
        } else if (page_index == 1) {
            parent.setBackgroundResource(R.drawable.background);
        } else {
            parent.setBackgroundResource(R.drawable.background_gradient);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_client:
                selectClient();
                break;

            case R.id.select_date:

                break;
            case R.id.select_line:

                break;
        }
    }

    private void selectClient() {
        MyUtils.startRequestActivity(this, GeneralSelectObjectActivity.class, Constants.SELECT_CLIENT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.write(requestCode+" "+resultCode);
        if(requestCode==Constants.SELECT_CLIENT && resultCode==RESULT_OK) {
            Client client = (Client)((ArrayList)data.getSerializableExtra("selected")).get(0);

            Logger.write(client.toString());
        }
    }
    public void goBack(View v) {
        onBackPressed();
    }
}