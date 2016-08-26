package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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
import com.tied.android.tiedapp.ui.adapters.LineClientAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineClientList extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineClientList.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    LinearLayout back_layout;
    protected ListView listView;

    protected LineClientAdapter adapter;
    protected ArrayList clientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_client_list);

        user = MyUtils.getUserLoggedIn();

        back_layout = (LinearLayout) findViewById(R.id.back_layout);

        if (back_layout != null) {
            back_layout.setOnClickListener(this);
        }

        clientsList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        if (clientsList.size() == 0) {
            initClient();
        }

    }


    protected void initClient() {

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("0km");
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if (coordinate == null) {
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        final ClientApi clientApi = MainApplication.createService(ClientApi.class, user.getToken());
        Call<ClientRes> response = clientApi.getClientsByLocation(clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if (clientRes.isAuthFailed()) {
                    User.LogOut(LineClientList.this);
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                    ArrayList<Client> clients = clientRes.getClients();
                    Log.d(TAG + "", clients.toString());
                    if (clients.size() > 0) {
                        initFormattedClient(clients);
                    } else {
                        bundle.putBoolean(Constants.NO_CLIENT_FOUND, true);
                        MyUtils.startActivity(LineClientList.this, MainActivity.class, bundle);
                    }
                } else {
                    Toast.makeText(LineClientList.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
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

    public void initFormattedClient(ArrayList<Client> clients) {
        clientsList = clients;
        adapter = new LineClientAdapter(clientsList, this);
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                MyUtils.startActivity(this, ViewLineActivity.class);
                break;

        }
    }

}
