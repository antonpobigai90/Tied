package com.tied.android.tiedapp.ui.fragments.client;

import android.app.Activity;
import android.content.Intent;
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
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.adapters.MapClientListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ClientsListFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    public static final String TAG = ClientsListFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;
    List<Client> clients = new ArrayList<Client>();
//    List<Client> search_data = new ArrayList<Client>();
    protected MapClientListAdapter adapter;

    String search_name;

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
    public static ClientsListFragment newInstance(Bundle bundle, ArrayList<Client> clients) {
        ClientsListFragment cmf=new ClientsListFragment();
        cmf.setArguments(bundle);
        cmf.setClients(clients);
        return cmf;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        adapter.notifyDataSetChanged();
    }
    public void addClients(ArrayList<Client> clients) {
        this.clients.addAll( clients);
        adapter.notifyDataSetChanged();
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        adapter = new MapClientListAdapter(clients, getActivity());
        listView.setAdapter(adapter);
        loadClientsFilter(MapAndListFragment.search_name);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
        //Client client = clients.get(position);
        Bundle bundle =new Bundle();
        bundle.putSerializable(Constants.USER_DATA, user);
        bundle.putSerializable(Constants.CLIENT_DATA, clients.get(position));
        MyUtils.startRequestActivity(getActivity(), ActivityClientProfile.class, Constants.ClientDelete, bundle);
    }

    @Override
    public void onClick(View v) {

    }

    public void loadClientsFilter(String search_name) {
        ClientFilter clientFilter = new ClientFilter();
        clientFilter.setName(search_name);
        clientFilter.setDistance(MapAndListFragment.distance);
        clientFilter.setUnit("mi");
        clientFilter.setGroup(MapAndListFragment.group);
        clientFilter.setLast_visited(MapAndListFragment.last_visited);
        clientFilter.setOrder_by(MapAndListFragment.orderby);
        clientFilter.setOrder(MapAndListFragment.order);
        if (MapAndListFragment.selectedTerritories.size() == 0)
            clientFilter.setTerritories(null);
        else
            clientFilter.setTerritories(MapAndListFragment.selectedTerritories);

        if (MapAndListFragment.selectedLines.size() == 0)
            clientFilter.setLines(null);
        else
            clientFilter.setLines(MapAndListFragment.selectedLines);
        clientFilter.setPage_number(1);

        Coordinate coordinate = MyUtils.getCurrentLocation();
        if (coordinate == null) {
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientFilter.setCoordinate(coordinate);
        DialogUtils.displayProgress(getActivity());
        ClientApi clientApi = MainApplication.createService(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsFilter(user.getId(), clientFilter);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity()== null ) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                Logger.write(clientRes.toString());
                try {
                    if (clientRes.isAuthFailed()) {
                        User.LogOut(getActivity());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        clients.clear();
                        clients.addAll(clientRes.getClients());

                        if(clients.size()==0) {
                            MyUtils.showNoResults(getView(), R.id.no_results);
                        } else {
                            MyUtils.hideNoResults(getView());
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Logger.write("Error onResponse", clientRes.getMessage());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }


    private void loadClients() {
        ClientLocation clientLocation = new ClientLocation();

        clientLocation.setDistance("100000" + MyUtils.getPreferredDistanceUnit());
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if( coordinate == null ){
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        final ClientApi clientApi =  MainApplication.createService(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getId(), clientLocation);
        response.enqueue(new retrofit2.Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity()== null ) {
                   // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                Logger.write(clientRes.toString());
                try {
                    if (clientRes.isAuthFailed()) {
                         User.LogOut(getActivity());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        clients.clear();
                        clients.addAll(clientRes.getClients());

                        if(clients.size()==0) {
                            MyUtils.showNoResults(getView(), R.id.no_results);
                        } else {
                            MyUtils.hideNoResults(getView());
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Logger.write("Error onResponse", clientRes.getMessage());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {

                Logger.write(" onFailure", t.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ClientFilter && resultCode == Activity.RESULT_OK) {
            if (MapAndListFragment.isClientFilter) {
                loadClientsFilter(MapAndListFragment.search_name);
            } else if (MapAndListFragment.isClear) {
                loadClients();
            }
        }
    }
}
