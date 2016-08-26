package com.tied.android.tiedapp.ui.fragments.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.ClientLinesAdapter;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ClientLinesFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    public static final String TAG = ClientLinesFragment.class
            .getSimpleName();

//    protected FragmentIterationListener mListener;

    protected User user;
    protected Bundle bundle;
    protected ListView listView;

    protected ClientLinesAdapter adapter;
    private Client client;

    Line[] array1 = {
            new Line("1","Root Candles","$3000",true),
            new Line("2","Mary Kay","$25,000",false),
    };
    public final ArrayList<Line> linesList = new ArrayList<Line>(Arrays.asList(array1));


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
        listView = (ListView) view.findViewById(R.id.list);
        bundle = getArguments();
        if (bundle != null) {
            
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            String client_json = bundle.getString(Constants.CLIENT_DATA);
            user = gson.fromJson(user_json, User.class);
            client = gson.fromJson(client_json, Client.class);
        }
        initLines();
    }


    protected void initLines(){
        adapter = new ClientLinesAdapter(linesList, getActivity());
        listView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG, "here---------------- listener");
//        adapter.setSelectedIndex(position);
    }
//
//    protected void nextAction(int action,Bundle bundle) {
//        if (mListener != null) {
//            mListener.OnFragmentInteractionListener(action, bundle);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof FragmentIterationListener) {
//            mListener = (FragmentIterationListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onClick(View v) {

    }

}
