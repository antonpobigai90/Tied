package com.tied.android.tiedapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.ui.adapters.LinesAdapter;
import com.tied.android.tiedapp.util.MyUtils;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class LinesFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    public static final String TAG = LinesFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;

    protected LinesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_lines_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        adapter = new LinesAdapter(MainApplication.linesList, getActivity(), bundle);
        listView.setAdapter(adapter);
        MyUtils.initLines(getActivity(), user, adapter);
       // if (MainApplication.linesList.size() == 0){

      //  }
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

    @Override
    public void onClick(View v) {

    }

}
