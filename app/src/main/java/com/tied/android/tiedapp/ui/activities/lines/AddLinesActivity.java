package com.tied.android.tiedapp.ui.activities.lines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tied.android.tiedapp.R;

/**
 * Created by Daniel on 5/3/2016.
 */
public class AddLinesActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_line);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txt_done:
                finish();
                break;
        }
    }
}
