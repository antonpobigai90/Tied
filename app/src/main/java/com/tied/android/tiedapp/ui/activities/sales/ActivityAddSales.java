package com.tied.android.tiedapp.ui.activities.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_sales);

        bundle = getIntent().getExtras();
        //user = MyUtils.getUserFromBundle(bundle);
       // fragment=AddSalesFragment.newInstance(bundle);


        initComponent();
    }
    private void initComponent() {
      /*  FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        ft.commit();*/

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
        //GeneralSelectObjectActivity.setType(GeneralSelectObjectActivity.SELECT_CLIENT_TYPE, false);
       // MyUtils.startRequestActivity(this, GeneralSelectObjectActivity.class, Constants.SELECT_CLIENT);
        MyUtils.initiateClientSelector(this, null, false, 12000);
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