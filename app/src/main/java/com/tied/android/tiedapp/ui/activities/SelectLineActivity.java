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
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.client.tab.LastVisitedClientListFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

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
    // public static final int SELECT_CLIENT_TYPE_MULTIPLE=102;
    public static final int SELECT_LINE_TYPE=200;
    // public static final int SELECT_LINE_TYPE_MULTIPLE=201;
    //public static int ACTIVITY_TYPE=SELECT_CLIENT_TYPE;
    //public static boolean IS_MULTIPLE=true;

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
    private boolean isMultiple=false;
    private String selectedIds;
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
           /* try{
                selectedIDs=bundle.getStringArrayList(SELECTED_IDS);
                if(selectedIDs==null) selectedIDs=new ArrayList<String>(0);
            }catch (Exception e) {}*/
        }
        setContentView(R.layout.schedule_select_client_list_general);
        MyUtils.setFocus(findViewById(R.id.getFocus));
        initComponent();
    }
   /* public static void setType(int objectType, boolean isMultiple) {
      //  ACTIVITY_TYPE=objectType;
      //  IS_MULTIPLE=isMultiple;
       // this.objectType=objectType;
       // this.isMultiple=isMultiple
       // GeneralSelectObjectActivity.IS_MULTIPLE=isMultiple;
    }*/



    public void initComponent() {
        clientsWithDistance = new ArrayList<Client>();
        listView = (ListView) findViewById(R.id.list);
        findViewById(R.id.clear_but).setOnClickListener(this);

//        txt_continue = (TextView) findViewById(R.id.txt_continue);

        search = (EditText) findViewById(R.id.search);
        listView.setOnItemClickListener(this);
        addLayout = findViewById(R.id.add_layout);

        if(!isMultiple) {
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
        if(user==null) user=MyUtils.getUserLoggedIn();
        if(bundle!=null) {
            selectedObjects=(ArrayList<Object>)bundle.getSerializable(SELECTED_OBJECTS);
        }
        if(selectedObjects!=null) {
            int len=selectedObjects.size();
            selectedIDs=new ArrayList<>(len);
            for(int i=0; i<len; i++) {
               /* if( objectType==SELECT_CLIENT_TYPE) {
                    selectedIDs.add(((Client) selectedObjects.get(i)).getId());

                }
                else{*/
                    selectedIDs.add(((Line)selectedObjects.get(i)).getId());

               // }
            }
        }else{
            selectedObjects=new ArrayList<Objects>();
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
            case R.id.clear_but:
                showClearWarning();
                break;
        }
    }
    private void showClearWarning() {
        final AlertDialog ad= new AlertDialog.Builder(this).create();
        ad.setMessage("This will clear all selections. Are you sure you want to proceed?");
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedIDs.clear();
                selectedObjects.clear();
                finishSelection();
            }
        });
        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ad.show();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");

        // if(clientsWithDistance.get(position))
        if(!isMultiple) {
            selectedIDs.clear();
            selectedObjects.clear();
        }
        if( objectType==SELECT_CLIENT_TYPE) {
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
        if(isMultiple) {
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
//        if(size==0) finishSelection.setVisibility(View.GONE);
      //  else finishSelection.setVisibility(View.VISIBLE);
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
        finishActivity(Constants.SELECT_CLIENT);
        finish();
    }

    private void initClient(){


        LineApi lineApi =  MainApplication.createService(LineApi.class, user.getToken());
        Call<ResponseBody> response = lineApi.getLines(user.getToken());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if ( this == null ) return;
                DialogUtils.closeProgress();
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(SelectLineActivity.this);
                        finish();
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {
                        ArrayList lines = (ArrayList) response.getDataAsList(Constants.LINES_lIST, Line.class);
                        if(lines.size() > 0){
                            clientsWithDistance.addAll(lines);

                        }

                        adapter = new MyClientLineAdapter(clientsWithDistance, selectedIDs, SelectLineActivity.this, isMultiple);
                        listView.setAdapter(adapter);
                        listView.setFastScrollEnabled(true);
                    } else {
                       MyUtils.showConnectionErrorToast(SelectLineActivity.this);
                    }
                    Log.d(TAG + " onResponse", resResponse.body().toString());
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(SelectLineActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                MyUtils.showConnectionErrorToast(SelectLineActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }

    public void initAdded(){
        added = new boolean[range.length-1];
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