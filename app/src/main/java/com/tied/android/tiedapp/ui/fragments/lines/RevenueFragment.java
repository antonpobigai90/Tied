package com.tied.android.tiedapp.ui.fragments.lines;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.fragments.MyFormFragment;

import java.util.Map;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class RevenueFragment extends MyFormFragment implements View.OnClickListener{
    private ImageView avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_revenue, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar:

                break;
        }
    }
    public Map getFields() {
 return myFieldsMap;
    }
}
