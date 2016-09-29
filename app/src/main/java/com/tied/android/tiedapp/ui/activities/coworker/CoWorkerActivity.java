package com.tied.android.tiedapp.ui.activities.coworker;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;

import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.adapters.CoWorkerGAdapter;
import com.tied.android.tiedapp.ui.adapters.CoWorkerHAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
public class CoWorkerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    public static final String TAG = CoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private CoWorkerHAdapter horizontalAdapter;
    private RecyclerView horizontalView;
    LinearLayoutManager horizontalManager;

    private CoWorkerGAdapter gridAdapter;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }

        //set horizontal LinearLayout as layout manager to creating horizontal list view
        horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalView = (RecyclerView) findViewById(R.id.horizontal_list);
        horizontalView.setLayoutManager(horizontalManager);

        horizontalAdapter = new CoWorkerHAdapter(MainApplication.clientsList, this);
        horizontalView.setAdapter(horizontalAdapter);

        if (MainApplication.clientsList.size() == 0){
            MyUtils.initClient(this, user, horizontalAdapter);
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new CoWorkerGAdapter(MainApplication.clientsList, this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);


        if (MainApplication.clientsList.size() == 0){
            MyUtils.initClient(this, user, gridAdapter);
        }

//        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
//        layoutParams.height = 800;
//        gridView.setLayoutParams(layoutParams);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
        Client client = (Client) MainApplication.clientsList.get(position);
        bundle.putSerializable(Constants.CLIENT_DATA, client);
        MyUtils.startActivity(this, ViewCoWorkerActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
            case R.id.add:
                MyUtils.startActivity(this, InviteCoWorkerActivity.class, bundle);
                break;
        }
    }
}
