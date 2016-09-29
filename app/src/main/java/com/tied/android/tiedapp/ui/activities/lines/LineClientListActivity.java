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
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.adapters.LineClientAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineClientListActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineClientListActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;
    Line line;

    LinearLayout back_layout;
    protected ListView listView;

    protected LineClientAdapter adapter;
    protected ArrayList clientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_client_list);

        user = MyUtils.getUserLoggedIn();
        bundle = getIntent().getExtras();
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
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
        Call<ClientRes> response = clientApi.getLineClients(user.getToken(), line.getId(), 0, clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    ClientRes clientRes = resResponse.body();
                    Logger.write(clientRes.toString());
                    if (clientRes.isAuthFailed()) {
                       // User.LogOut(LineClientListActivity.this);
                        Logger.write(clientRes.toString());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        ArrayList<Client> clients = clientRes.getClients();
                        Log.d(TAG + "", clients.toString());
                        if (clients.size() > 0) {
                            initFormattedClient(clients);
                        } else {
                            bundle.putBoolean(Constants.NO_CLIENT_FOUND, true);
                           // MyUtils.startActivity(LineClientListActivity.this, MainActivity.class, bundle);
                        }
                    } else {
                        Logger.write(clientRes.getMessage());
                    }
                    Log.d(TAG + " onResponse", resResponse.body().toString());
                }catch (Exception e) {
Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Logger.write(TAG + " onFailure", t.toString());
                MyUtils.showConnectionErrorToast(LineClientListActivity.this);
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
                onBackPressed();
                break;

        }
    }

}
