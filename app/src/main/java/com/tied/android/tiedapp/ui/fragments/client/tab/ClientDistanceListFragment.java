package com.tied.android.tiedapp.ui.fragments.client.tab;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.adapters.ClientDistantAdapter;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class ClientDistanceListFragment extends ClientList{

    public static final String TAG = ClientDistanceListFragment.class
            .getSimpleName();

    private TextView txt_distance;

    private int[] range = {0,500,1000,2000,5000};
    private boolean[] added;

    private TextView txt_continue;


    public void initComponent(View view) {
        super.initComponent(view);
        txt_continue = (TextView) view.findViewById(R.id.txt_continue);
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
                if(adapter != null){
                    ((ClientDistantAdapter)adapter).filter(searchData);
                }
            }
        });
    }

    public void initAdded(){
        added = new boolean[range.length-1];
    }

    public void initFormattedClient(ArrayList<Client> clients){

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

        clientsList = data;
        adapter = new ClientDistantAdapter(clientsList, getActivity());
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
    }
}
