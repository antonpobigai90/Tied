package com.tied.android.tiedapp.ui.activities.sales;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ClientSaleDataModel;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.SaleClientDetailsListAdapter;
import com.tied.android.tiedapp.ui.adapters.SaleLineDetailsListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivitySalesClientSaleDetails extends FragmentActivity implements  View.OnClickListener{

    public static final String TAG = ActivitySalesClientSaleDetails.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private ImageView img_back, img_filter, img_plus;

    private ListView client_sales_listview;
    private SaleClientDetailsListAdapter client_sale_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_client_sale_details);

        bundle = getIntent().getExtras();

        initComponent();
    }
    private void initComponent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green_color1));
        }

        img_back = (ImageView) findViewById(R.id.img_back);
        img_filter = (ImageView) findViewById(R.id.img_filter);
        img_plus = (ImageView) findViewById(R.id.img_plus);

        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);

        client_sales_listview = (ListView) findViewById(R.id.client_sales_listview);

        ArrayList<ClientSaleDataModel> clientSaleDataModels = new ArrayList<>();

        for (int i = 0 ; i < 7 ; i++) {
            ClientSaleDataModel clientSaleDataModel = new ClientSaleDataModel();

            clientSaleDataModel.setPrice("+ 230 USD");
            clientSaleDataModel.setDate("19 February 2016");
            clientSaleDataModel.setSummary("This is a note added by the user");

            clientSaleDataModels.add(clientSaleDataModel);
        }

        client_sale_adapter = new SaleClientDetailsListAdapter(clientSaleDataModels, this);
        client_sales_listview.setAdapter(client_sale_adapter);
        client_sale_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_filter:
                MyUtils.startActivity(this, ActivitySalesFilter.class);
                break;
            case R.id.img_plus:
                MyUtils.startActivity(this, ActivityAddSales.class);
                break;
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
}