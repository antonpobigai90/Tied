package com.tied.android.tiedapp.ui.activities.sales;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.SalePrintListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivitySalesFilter extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;

    private TextView txt_reset, txt_cancel, txt_default, txt_highest, txt_lowest, txt_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_filter);

        bundle = getIntent().getExtras();

        initComponent();
    }
    private void initComponent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green_color1));
        }

        txt_reset = (TextView) findViewById(R.id.txt_reset);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        txt_default = (TextView) findViewById(R.id.txt_default);
        txt_highest = (TextView) findViewById(R.id.txt_highest);
        txt_lowest = (TextView) findViewById(R.id.txt_lowest);
        txt_filter = (TextView) findViewById(R.id.txt_filter);

        txt_reset.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);
        txt_default.setOnClickListener(this);
        txt_highest.setOnClickListener(this);
        txt_filter.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_cancel:
                finish();
                break;
            case R.id.txt_reset:
                break;
            case R.id.txt_default:
                break;
            case R.id.txt_highest:
                break;
            case R.id.txt_lowest:
                break;
            case R.id.txt_filter:
                break;
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
}