package com.tied.android.tiedapp.ui.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.ui.activities.lines.ViewNewLineActivity;
import com.tied.android.tiedapp.ui.adapters.LinesAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import junit.framework.Test;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class LinesFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = LinesFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;

    protected LinesAdapter adapter;
    ImageView img_plus;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new LinesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lines, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.blue_status_bar));
        }

        initComponent(view);
        return view;
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);

        img_plus = (ImageView) view.findViewById(R.id.img_plus);
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.showNewLineDialog(getActivity(), "Create New Line", new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {

                    }
                });
            }
        });

        listView = (ListView) view.findViewById(R.id.lines_listview);
        listView.setOnItemClickListener(this);

        adapter = new LinesAdapter(MainApplication.linesList, getActivity(), bundle);
        listView.setAdapter(adapter);
        MyUtils.initLines(getActivity(), user, adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
        Line line = (Line) MainApplication.linesList.get(position);
        bundle.putSerializable(Constants.LINE_DATA, line);
        MyUtils.startActivity(getActivity(), ViewLineActivity.class, bundle);
    }
}
