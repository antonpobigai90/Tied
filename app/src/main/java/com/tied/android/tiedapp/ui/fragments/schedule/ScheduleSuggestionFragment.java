package com.tied.android.tiedapp.ui.fragments.schedule;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Client;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.ui.activities.schedule.ViewSchedule;
import com.tied.android.tiedapp.ui.adapters.ClientScheduleAdapter;
import com.tied.android.tiedapp.ui.adapters.ClientScheduleHorizontalAdapter;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleSuggestionFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ScheduleSuggestionFragment.class
            .getSimpleName();

    private Bundle bundle;

    private TextView view_schedule;
    private ArrayList<Client> clients;
    private ListView listView;

    private RecyclerView horizontalList;
    LinearLayoutManager horizontalManager;
    private ClientScheduleHorizontalAdapter horizontalAdapter;

    private ClientScheduleAdapter adapter;

    int[] IMAGE = {R.mipmap.avatar_profile, R.mipmap.avatar_schedule, R.mipmap.default_avatar};
    String[] NAME = {"Emily Emmanuel","Johnson Good","Nonso Lagos"};
    Location[] ADDRESS = {new Location("Ikeja","123","Lagos","Ikunna"),new Location("Old Town","546","NY","Mile street"),new Location("LA","567","Carlifornia","Myles Strt")};

    private FragmentInterationListener fragmentInterationListener;

    public ScheduleSuggestionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_clients_suggestions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterationListener) {
            fragmentInterationListener = (FragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentInterationListener != null) {
            fragmentInterationListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view) {

        clients = new ArrayList<Client>();
        listView = (ListView) view.findViewById(R.id.list);

        horizontalList = (RecyclerView)view.findViewById(R.id.horizontal_list);

        //set horizontal LinearLayout as layout manager to creating horizontal list view
        horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalList.setLayoutManager(horizontalManager);


        LoadClient loadClient = new LoadClient();
        loadClient.execute();

        view_schedule = (TextView) view.findViewById(R.id.view_schedule);
        view_schedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_schedule:
//                nextAction(Constants.ActivitySchedule, bundle);
                Intent intent = new Intent(getActivity(), ViewSchedule.class);
                startActivity(intent);
                break;
        }
    }

    class LoadClient extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            if (NAME.length > 0) {
                for (int i = 0; i < NAME.length; i++) {
                    Client detail = new Client(NAME[i], IMAGE[i],ADDRESS[i]);
                    clients.add(detail);
                }
            } else {
                Toast.makeText(getActivity(), "No clients.", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ClientScheduleAdapter(clients, getActivity());
            horizontalAdapter = new ClientScheduleHorizontalAdapter(clients,getActivity());
//            verticalAdapter = new ClientScheduleHorizontalAdapter(clients,getActivity());

            horizontalList.setAdapter(horizontalAdapter);
//            verticallList.setAdapter(horizontalAdapter);
            listView.setAdapter(adapter);
            listView.setFastScrollEnabled(true);
        }
    }

}
