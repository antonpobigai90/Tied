package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.coworker.CoWorkerLinesActivity;
import com.tied.android.tiedapp.ui.adapters.ClientLinesAdapter;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.client.tab.LastVisitedClientListFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class SelectLineActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    public static final String TAG = LastVisitedClientListFragment.class
            .getSimpleName();


    public static final String SELECTED_OBJECTS="selected_ids";
    public static final String OBJECT_TYPE="object_type";
    public static final String IS_MULTIPLE="is_multiple";
    public static final int SELECT_CLIENT_TYPE=100;
    public static final int SELECT_LINE_TYPE=200;

    public FragmentIterationListener mListener;
    ArrayList selectedObjects = new ArrayList();

    ArrayList<Line> search_data = new ArrayList<Line>();
    ArrayList<Line> lineDataModels = new ArrayList<Line>();
    private ListView listView;
    private ClientLinesAdapter adapter;

    // Pop up
    private EditText search;

    private Bundle bundle;
    private User user;

    private TextView txt_continue, selectedCountText;
    private View addLayout;
    View finishSelection;
    private boolean isMultiple=false;
    int objectType=SELECT_CLIENT_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null ) {
            try{
                isMultiple=bundle.getBoolean(IS_MULTIPLE);

            }catch (Exception e) {}
            try{
                objectType=bundle.getInt(OBJECT_TYPE);
            }catch (Exception e) {}
        }
        setContentView(R.layout.schedule_select_client_list_general);
        MyUtils.setFocus(findViewById(R.id.getFocus));
        initComponent();
    }

    public void initComponent() {
        listView = (ListView) findViewById(R.id.list);

        search = (EditText) findViewById(R.id.search);
        listView.setOnItemClickListener(this);
        addLayout = findViewById(R.id.add_layout);

        if(!isMultiple) {
            addLayout.setVisibility(View.GONE);
        }
        finishSelection=findViewById(R.id.add_button);
        finishSelection.setOnClickListener(this);

        selectedCountText=(TextView)findViewById(R.id.selected_count);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                search_data.clear();
                // TODO Auto-generated method stub
                for(int i = 0 ; i < lineDataModels.size() ; i++) {
                    Line model = (Line) lineDataModels.get(i);

                    String title = model.getName();
                    if(title.matches("(?i).*" + search.getText().toString() + ".*")) {
                        search_data.add(model);
                    }
                }

                adapter = new ClientLinesAdapter(search_data, SelectLineActivity.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        if(user==null) user=MyUtils.getUserLoggedIn();

        adapter = new ClientLinesAdapter( lineDataModels, this);
        listView.setAdapter(adapter);

        initLines();
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
        Line item = lineDataModels.get(position);

        selectedObjects.clear();

        for (int i = 0; i < lineDataModels.size(); i++) {
            Line model = lineDataModels.get(i);
            if (item.getId().equals(model.getId())) {
                item.setCheck_status(true);

                selectedObjects.add(item);
            } else {
                model.setCheck_status(false);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void finishSelection() {
        Intent intent = new Intent();
        Bundle b =new Bundle();
        if(!selectedObjects.isEmpty()) {
            if (isMultiple) {
                b.putSerializable("selected", selectedObjects);
            } else {
                b.putSerializable("selected", (Line) selectedObjects.get(0));
            }
        }
        intent.putExtras(b);
        Logger.write("finishginnnnn.");
        setResult(RESULT_OK, intent);
        finishActivity(Constants.SELECT_LINE);
        finish();
    }


    public void initLines(){
        final LineApi lineApi =  MainApplication.createService(LineApi.class);
        Call<ResponseBody> response = lineApi.getUserLines(user.getId(), 1);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(SelectLineActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {

                        lineDataModels .addAll( (ArrayList) response.getDataAsList(Constants.LINES_lIST, Line.class));
                        Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+lineDataModels.size());
                        adapter.notifyDataSetChanged();

                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
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
