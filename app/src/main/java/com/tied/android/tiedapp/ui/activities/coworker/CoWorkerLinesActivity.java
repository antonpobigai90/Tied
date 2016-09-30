package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.ClientLinesAdapter;
import com.tied.android.tiedapp.ui.adapters.MapClientListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

public class CoWorkerLinesActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = CoWorkerLinesActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    RelativeLayout top_layout;
    TextView txt_title;

    ListView line_listview;
    ClientLinesAdapter linesAdapter;

    ArrayList<Line> lineDataModels = new ArrayList<Line>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_worker_line);

        bundle = getIntent().getExtras();
        int page_index = bundle.getInt(Constants.SHOW_LINE);
        int filter_index = bundle.getInt(Constants.SHOW_FILTER);

        user = MyUtils.getUserFromBundle(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            if (page_index == 2 || filter_index == 0) {
                window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.gradient));
            }
        }

        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        if (page_index == 2 || filter_index == 0) {
            top_layout.setBackgroundResource(R.drawable.background);
        } else {
            top_layout.setBackgroundResource(R.drawable.background_gradient);
        }

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        txt_title = (TextView) findViewById(R.id.txt_title);
        line_listview = (ListView) findViewById(R.id.lines_listview);

        String title = (page_index == 0 || page_index == 2) ? "Lines" : "Filter Line";
        txt_title.setText(title);

        for (int i = 0 ; i < 10; i++) {
            Line lineDataModel = new Line();

            lineDataModel.setName("Creative Co-op");
            lineDataModel.setSales("$35,000");

            lineDataModels.add(lineDataModel);
        }

        linesAdapter = new ClientLinesAdapter( lineDataModels, this);
        line_listview.setAdapter(linesAdapter);
        linesAdapter.notifyDataSetChanged();

        if (page_index == 1) {
            line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Line line = lineDataModels.get(position);

                    if (line.isCheck_status()) {
                        line.setCheck_status(false);
                    } else {
                        line.setCheck_status(true);
                    }

                    linesAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
        }
    }
}
