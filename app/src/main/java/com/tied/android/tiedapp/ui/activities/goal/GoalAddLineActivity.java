package com.tied.android.tiedapp.ui.activities.goal;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.ClientLinesAdapter;

import java.util.ArrayList;

public class GoalAddLineActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = GoalAddLineActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    ImageView img_close;
    TextView txt_title, selected_count;
    LinearLayout add_layout;
    RelativeLayout top_layout;

    ListView line_listview;
    ClientLinesAdapter linesAdapter;

    ArrayList<Line> lineDataModels = new ArrayList<Line>();

    int total_selected_cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_worker_line);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = getIntent().getExtras();

        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        top_layout.setBackgroundResource(R.drawable.background);

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Select Lines to Add to Goal");

        add_layout = (LinearLayout) findViewById(R.id.add_layout);
        selected_count = (TextView) findViewById(R.id.selected_count);

        line_listview = (ListView) findViewById(R.id.lines_listview);

        for (int i = 0 ; i < 10; i++) {
            Line lineDataModel = new Line();

            lineDataModel.setName("Creative Co-op");
            lineDataModel.setSales("$35,000");

            lineDataModels.add(lineDataModel);
        }

        linesAdapter = new ClientLinesAdapter(3, lineDataModels, this);
        line_listview.setAdapter(linesAdapter);
        linesAdapter.notifyDataSetChanged();

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Line line = lineDataModels.get(position);
                if (line.isCheck_status()) {
                    line.setCheck_status(false);
                    total_selected_cnt--;
                } else {
                    line.setCheck_status(true);
                    total_selected_cnt++;
                }

                linesAdapter.notifyDataSetChanged();

                if (total_selected_cnt > 0) {
                    add_layout.setVisibility(View.VISIBLE);
                    selected_count.setText(String.format("Add %d Lines to Goal?", total_selected_cnt));
                } else {
                    add_layout.setVisibility(View.GONE);
                }
            }
        });
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                onBackPressed();
                break;
        }
    }
}
