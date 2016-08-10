package com.tied.android.tiedapp.ui.fragments.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.ui.adapters.ClientAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class SelectClientListFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    public static final String TAG = SelectClientListFragment.class
            .getSimpleName();

    public FragmentIterationListener mListener;


    private ArrayList clientsWithDistance;
    private ListView listView;

    private int[] range = {0,500,1000,2000,5000};
    private boolean[] added;

    // Pop up
    private EditText search;
    private ClientAdapter adapter;
    private Bundle bundle;
    private User user;

    private TextView txt_continue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_select_client_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view) {
        clientsWithDistance = new ArrayList<Client>();
        listView = (ListView) view.findViewById(R.id.list);

        txt_continue = (TextView) view.findViewById(R.id.txt_continue);

        search = (EditText) view.findViewById(R.id.search);
        listView.setOnItemClickListener(this);

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

//        user = User.getUser(getActivity().getApplicationContext());
        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);
        }
        initClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar:

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");
        if(clientsWithDistance.get(position) instanceof Client){
            Client data = (Client) clientsWithDistance.get(position);
            Log.d("SelectContact", data.toString());
            Intent intent = new Intent(getActivity(), CreateAppointmentActivity.class);
            intent.putExtra(Constants.CLIENT_DATA, data);
            startActivity(intent);
        }
    }

    private void initClient(){

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("0km");
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if( coordinate == null ){
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        ClientApi clientApi =  MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getToken(), clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if ( getActivity() == null ) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if(clientRes.isAuthFailed()){
                    User.LogOut(getActivity());
                }
                else if(clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200){
                    ArrayList<Client> clients = clientRes.getClients();
                    Log.d(TAG + "", clients.toString());
                    if(clients.size() > 0){
                        clientsWithDistance = getClientsWithLocationDistance(clients);
                        adapter = new ClientAdapter(clientsWithDistance, getActivity());
                        listView.setAdapter(adapter);
                        listView.setFastScrollEnabled(true);
                    }else{
                        bundle.putBoolean(Constants.NO_CLIENT_FOUND, true);
                        MyUtils.startActivity(getActivity(), MainActivity.class, bundle);
                    }
                }else{
                    Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                }
                Log.d(TAG + " onResponse", resResponse.body().toString());
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    public void initAdded(){
        added = new boolean[range.length-1];
    }

    public ArrayList getClientsWithLocationDistance(ArrayList<Client> clients){

        initAdded();

        ArrayList data = new ArrayList();
        int rangeIndex = 0;
        int minIndex = range[0];
        for(int i = 0; i < range.length - 1; i++){
            for(int j = 0; j < clients.size(); j++ ) {
                Client this_client = clients.get(j);
                if(this_client.getDis_from() >= range[rangeIndex] && this_client.getDis_from() <= range[rangeIndex + 1]){
                    if(!added[rangeIndex]){
                        String lower = minIndex+"";
                        String upper = range[rangeIndex + 1]+"";
                        Distance distance = new Distance(lower, upper, "Miles");
                        data.add(distance);
                        added[rangeIndex] = true;
                        Log.d(TAG, "DISTANCE IS RANGE: "+distance.toString() +" j = "+j);
                        minIndex = range[rangeIndex + 1];
                    }
                    Log.d(TAG, "this_client DISTANCE IS : "+this_client.getDis_from() +" name "+this_client.getFull_name() +" j = "+j);
                    data.add(this_client);
                    clients.remove(j);
                    j--;
                }
            }
            rangeIndex++;
        }

        if(clients.size() > 0){
            String lower = range[rangeIndex]+"";
            String upper = "n";
            Distance distance = new Distance(lower, upper, "Miles");
            data.add(distance);
            data.addAll(clients);
        }

        return data;
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
