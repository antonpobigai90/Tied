package com.tied.android.tiedapp.ui.fragments.lines;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.adapters.LineClientAdapter;
import com.tied.android.tiedapp.ui.fragments.MyFormFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class ClientFragment extends MyFormFragment implements View.OnClickListener{
    private ImageView avatar;

    public static final String TAG = ClientFragment.class
            .getSimpleName();

    public FragmentIterationListener mListener;


    private ArrayList<Client> clients;
    private ListView listView;

    // Pop up
    private EditText search;
    private LineClientAdapter adapter;

    int[] IMAGE = {R.mipmap.avatar_profile, R.mipmap.avatar_schedule, R.mipmap.default_avatar};
    String[] NAME = {"Emily Emmanuel","Johnson Good","Nonso Lagos"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_client_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view) {
        clients = new ArrayList<Client>();
        listView = (ListView) view.findViewById(R.id.list);

        search = (EditText) view.findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String searchData = search.getText().toString().trim().toLowerCase();
                adapter.filter(searchData);
            }
        });

        initClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar:

                break;
        }
    }

    private void initClient(){
        for(int i = 0; i < NAME.length; i++){
            Client client = new Client();
            client.setFull_name(NAME[i]);
            clients.add(client);
        }
        adapter = new LineClientAdapter(clients,getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

}
