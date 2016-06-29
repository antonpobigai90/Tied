package com.tied.android.tiedapp.ui.activities.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.tied.android.tiedapp.interfaces.retrofits.ClientApi;
import com.tied.android.tiedapp.objects.Client;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.schedule.CreateApointmentActivity;
import com.tied.android.tiedapp.ui.adapters.ClientAdapter;
import com.tied.android.tiedapp.util.DialogUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectClientActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = SelectClientActivity.class
            .getSimpleName();


    private ArrayList<Client> clients;
    private ListView listView;

    // Pop up
    private EditText search;
    private ClientAdapter adapter;
    private Bundle bundle;
    private User user;

    private TextView txt_continue;

    int[] IMAGE = {R.mipmap.avatar_profile, R.mipmap.avatar_schedule, R.mipmap.default_avatar};
    String[] NAME = {"Emily Emmanuel","Johnson Good","Nonso Lagos"};
    Location[] ADDRESS = {new Location("Ikeja","123","Lagos","Ikunna"),new Location("Old Town","546","NY","Mile street"),new Location("LA","567","Carlifornia","Myles Strt")};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_select_client);

        clients = new ArrayList<Client>();
        listView = (ListView) findViewById(R.id.list);

        txt_continue = (TextView) findViewById(R.id.txt_continue);

        search = (EditText) findViewById(R.id.search);
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

        user = User.getUser(getApplicationContext());
        initClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txt_continue:

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");
        Client data = clients.get(position);
        Log.d("SelectContact", data.toString());

        Intent intent = new Intent(this, CreateApointmentActivity.class);
        startActivity(intent);
    }

    private void initClient(){
        ClientApi clientApi =  MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.getClients(user.getToken());
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (SelectClientActivity.this == null) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if(clientRes.isAuthFailed()){
                    User.LogOut(SelectClientActivity.this);
                }
                else if(clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200){
                    clients = clientRes.getClients();
                    Log.d(TAG + "", clients.toString());
                    adapter = new ClientAdapter(clients, SelectClientActivity.this);
                    listView.setAdapter(adapter);
                    listView.setFastScrollEnabled(true);
                }else{
                    Toast.makeText(SelectClientActivity.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
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

    // Load data on background
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
                Toast.makeText(SelectClientActivity.this, "No clients.", Toast.LENGTH_LONG).show();
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ClientAdapter(clients, SelectClientActivity.this);
            listView.setAdapter(adapter);
            listView.setFastScrollEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
