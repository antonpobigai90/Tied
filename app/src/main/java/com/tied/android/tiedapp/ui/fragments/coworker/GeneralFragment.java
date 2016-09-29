package com.tied.android.tiedapp.ui.fragments.coworker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.LinesAndTerritories;
import com.tied.android.tiedapp.ui.activities.client.ClientMapAndListActivity;
import com.tied.android.tiedapp.ui.activities.coworker.CoWorkerSchedulesActivity;
import com.tied.android.tiedapp.ui.adapters.GoalsAdapter;
import com.tied.android.tiedapp.util.MyUtils;

public class GeneralFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GeneralFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;

    protected GoalsAdapter adapter;

    private LinearLayout lines, schedules, territories, clients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coworker_general_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);

        lines = (LinearLayout) view.findViewById(R.id.lines);
        schedules = (LinearLayout) view.findViewById(R.id.schedules);
        territories = (LinearLayout) view.findViewById(R.id.territories);
        clients = (LinearLayout) view.findViewById(R.id.clients);
        lines.setOnClickListener(this);
        schedules.setOnClickListener(this);
        territories.setOnClickListener(this);
        clients.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lines:
                bundle.putBoolean(Constants.SHOW_TERRITORY, false);
                MyUtils.startActivity(getActivity(), LinesAndTerritories.class, bundle);
                break;
            case R.id.territories:
                bundle.putBoolean(Constants.SHOW_TERRITORY, true);
                MyUtils.startActivity(getActivity(), LinesAndTerritories.class, bundle);
                break;
            case R.id.schedules:
                MyUtils.startActivity(getActivity(), CoWorkerSchedulesActivity.class, bundle);
                break;
            case R.id.clients:
                MyUtils.startActivity(getActivity(), ClientMapAndListActivity.class, bundle);
                break;

        }
    }
}
