package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.client.tab.LastVisitedClientListFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class GeneralSelectObjectActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    public static final String TAG = LastVisitedClientListFragment.class
            .getSimpleName();


    public static final String SELECTED_IDS="selected_ids";
    public static final int SELECT_CLIENT_TYPE=100;
   // public static final int SELECT_CLIENT_TYPE_MULTIPLE=102;
    public static final int SELECT_LINE_TYPE=200;
   // public static final int SELECT_LINE_TYPE_MULTIPLE=201;
    public static int ACTIVITY_TYPE=SELECT_CLIENT_TYPE;
    public static boolean IS_MULTIPLE=true;

    public FragmentIterationListener mListener;
    ArrayList selectedObjects = new ArrayList();


    private ArrayList clientsWithDistance;
    private ArrayList<String> selectedIDs=new ArrayList<String>(0);
    private ListView listView;

    private int[] range = {0,500,1000,2000,5000};
    private boolean[] added;

    // Pop up
    private EditText search;
    private MyClientLineAdapter adapter;
    private Bundle bundle;
    private User user;

    private TextView txt_continue, selectedCountText;
    private View addLayout;
    View finishSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_select_client_list_general);
        initComponent();
    }
    public static void setType(int objectType, boolean isMultiple) {
        ACTIVITY_TYPE=objectType;
        IS_MULTIPLE=isMultiple;
        GeneralSelectObjectActivity.IS_MULTIPLE=isMultiple;
    }



    public void initComponent() {
        clientsWithDistance = new ArrayList<Client>();
        listView = (ListView) findViewById(R.id.list);

        txt_continue = (TextView) findViewById(R.id.txt_continue);

        search = (EditText) findViewById(R.id.search);
        listView.setOnItemClickListener(this);
        addLayout = findViewById(R.id.add_layout);

        if(!IS_MULTIPLE) {
            addLayout.setVisibility(View.GONE);
        }
        finishSelection=findViewById(R.id.add_button);
        finishSelection.setVisibility(View.GONE);
        finishSelection.setOnClickListener(this);
        selectedCountText=(TextView)findViewById(R.id.selected_count);
        updateNumSelected();


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
                //adapter.filter(searchData);
            }
        });




        //user = User.getUser(getActivity().getApplicationContext());//
        //

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        if(bundle!=null) {
            selectedObjects=(ArrayList<Object>)bundle.getSerializable(SELECTED_IDS);
        }
        if(selectedObjects!=null) {
            int len=selectedObjects.size();
            selectedIDs=new ArrayList<>(len);
            for(int i=0; i<len; i++) {
                if( ACTIVITY_TYPE==SELECT_CLIENT_TYPE) {
                    selectedIDs.add(((Client) selectedObjects.get(i)).getId());

                }
                else{
                    selectedIDs.add(((Line)selectedObjects.get(i)).getId());

                }
            }
        }
        //if( ACTIVITY_TYPE==SELECT_CLIENT_TYPE) {
            initClient();
      //  }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
                finishSelection();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");

       // if(clientsWithDistance.get(position))
        if(!IS_MULTIPLE) {
            selectedIDs.clear();
            selectedObjects.clear();
        }
      if( ACTIVITY_TYPE==SELECT_CLIENT_TYPE) {
            Client obj=(Client)clientsWithDistance.get(position);

            if(selectedIDs.contains(obj.getId())) {
                selectedIDs.remove(obj.getId());
                selectedObjects.remove(obj);
            }else {
                selectedIDs.add(obj.getId());
                selectedObjects.add(obj);
            }
        }else{
            Line obj=(Line)clientsWithDistance.get(position);
            if(selectedIDs.contains(obj.getId())) {
                selectedIDs.remove(obj.getId());
                selectedObjects.remove(obj);
            }else {
                selectedIDs.add(obj.getId());
                selectedObjects.add(obj);
            }
        }
       /* Intent intent = new Intent();
        Bundle b =new Bundle();
        b.putSerializable("selected", selectedObjects);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();*/
        if(IS_MULTIPLE) {
            adapter.setSelected(selectedIDs);
            adapter.notifyDataSetChanged();
        }else{
            finishSelection();
            return;
        }
       updateNumSelected();

    }
    private void updateNumSelected() {
        int size=selectedIDs.size();
        selectedCountText.setText(size+" Selected");
        if(size==0) finishSelection.setVisibility(View.GONE);
        else finishSelection.setVisibility(View.VISIBLE);
    }
    private void finishSelection() {
        Intent intent = new Intent();
        Bundle b =new Bundle();
        b.putSerializable("selected", selectedObjects);
        intent.putExtras(b);
        Logger.write("finishginnnnn.");
        setResult(RESULT_OK, intent);
        finishActivity(RESULT_OK);
        finish();
    }

    private void initClient(){

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("100000"+MyUtils.getPreferredDistanceUnit());
        MyUtils.setCurrentLocation(new Coordinate(33.894212, -84.231574));
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
                if ( this == null ) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if(clientRes.isAuthFailed()){
                    User.LogOut(getApplicationContext());
                }
                else if(clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200){
                    ArrayList<Client> clients = clientRes.getClients();
                    Log.d(TAG + "", clients.toString());
                    clientsWithDistance = clients;
                    adapter = new MyClientLineAdapter(clientsWithDistance, selectedIDs, GeneralSelectObjectActivity.this, IS_MULTIPLE);
                    listView.setAdapter(adapter);
                    listView.setFastScrollEnabled(true);
                }else{
                    Toast.makeText(GeneralSelectObjectActivity.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
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

    public ArrayList getClientsByRecentlyAdded(ArrayList<Client> clients){

        return clients;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(user==null) {
            finish();
        }
    }
    public void goBack(View v) {
        onBackPressed();
    }
}
