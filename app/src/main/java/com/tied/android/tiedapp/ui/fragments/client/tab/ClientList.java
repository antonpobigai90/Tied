package com.tied.android.tiedapp.ui.fragments.client.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.adapters.ClientListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 8/14/2016.
 */
public class ClientList extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = ClientList.class
            .getSimpleName();

    protected FragmentIterationListener mListener;

    protected User user;
    protected Bundle bundle;
    protected ListView listView;

    protected BaseAdapter adapter;
    protected ArrayList clientsList;

    // Pop up
    protected EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_select_client_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view){
        clientsList = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        search = (EditText) view.findViewById(R.id.search);
        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);
        }
        if(clientsList.size() == 0){
            initClient();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");
        if(clientsList.get(position) instanceof Client){
            Client data = (Client) clientsList.get(position);

//            Intent intent = new Intent(getActivity(), CreateAppointmentActivity.class);
            Intent intent = new Intent(getActivity(), AddClientActivity.class);

            intent.putExtra(Constants.CLIENT_DATA, data);
            startActivity(intent);
        }
    }


    protected void initClient(){

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
                        initFormattedClient(clients);
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

    protected void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
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

    public void initFormattedClient(ArrayList<Client> clients){
        clientsList = clients;
        adapter = new ClientListAdapter(clientsList, getActivity());
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
    }
}
