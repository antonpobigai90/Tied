package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.ServerInfo;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TerritoryFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = TerritoryFragment.class
            .getSimpleName();

    private RelativeLayout continue_btn;

    LinearLayout alert_valid;

    private Bundle bundle;

    // Reference to our image view we will use
    public ImageView img_user_picture;

    private SignUpFragmentListener mListener;

    ArrayList<TerritoryModel> territory_data = new ArrayList<TerritoryModel>();
    ListView territory_listview;
    SearchAdapter territory_adapter;
    Context context;

    ArrayList<String> territories = new ArrayList<String>();

    public TerritoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_territory, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpFragmentListener) {
            mListener = (SignUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(Constants.SalesRep, bundle);
        }
    }

    public void initComponent(View view) {

        TerritoryModel item = new TerritoryModel();
        item.setTerritory_name("");
        item.setiNew(true);
        territory_data.add(item);

        territory_listview = (ListView) view.findViewById(R.id.territory_listview);
        territory_adapter = new SearchAdapter(territory_data, getActivity());
        territory_listview.setAdapter(territory_adapter);
        territory_listview.setDividerHeight(0);

        alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid.setVisibility(View.GONE);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            ((SignUpActivity) getActivity()).loadAvatar(user, img_user_picture);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                Log.d("territory_data", territory_data.toString());
                for (int i = 0 ; i < territory_data.size() ; i++) {
                    TerritoryModel item = territory_data.get(i);
                    if (!item.getTerritory_name().equals("")) {
                        territories.add(item.getTerritory_name());
                    }
                }
                continue_action();
                break;
        }
    }

    public void continue_action(){
        if(territories.size() > 0){
            DialogUtils.displayProgress(getActivity());

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setTerritories(territories);
            user.setSign_up_stage(Constants.SalesRep);

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<ServerInfo> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<ServerInfo>() {
                @Override
                public void onResponse(Call<ServerInfo> call, Response<ServerInfo> UpdateUserResponse) {
                    ServerInfo UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG +" onResponse", UpdateUserResponse.body().toString());
                    if(UpdateUser.isSuccess()){
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if(saved){
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER, json);
                            DialogUtils.closeProgress();
                            nextAction(bundle);
                        }else{
                            DialogUtils.closeProgress();
                            Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), UpdateUser.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    DialogUtils.closeProgress();
                }

                @Override
                public void onFailure(Call<ServerInfo> UpdateUserCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG +" onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }else{
            alert_valid.setVisibility(View.VISIBLE);
            Utility.moveViewToScreenCenter( alert_valid, Utility.getResourceString(context, R.string.alert_valide_no_territory));
        }
    }

    class SearchAdapter extends ArrayAdapter<TerritoryModel> {

        private ArrayList<TerritoryModel> itemList;
        private Context context;


        public SearchAdapter(ArrayList<TerritoryModel> itemList, Context ctx) {
            super(ctx, android.R.layout.simple_list_item_1, itemList);
            this.itemList = itemList;
            this.context = ctx;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(R.layout.territory_list_item, null);
            }

            final TerritoryModel item = territory_data.get(position);

            TextView txt_territory_item_label = (TextView) v.findViewById(R.id.txt_territory_item_label);
            final TextView txt_item_territory = (TextView) v.findViewById(R.id.txt_item_territory);

            final EditText txt_territory = (EditText) v.findViewById(R.id.txt_territory);
            txt_territory.setBackgroundResource(android.R.color.transparent);

            ImageView img_add = (ImageView) v.findViewById(R.id.img_add);
            img_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txt_territory.getText().length() == 0) {
                        alert_valid.setVisibility(View.VISIBLE);
                        Utility.moveViewToScreenCenter( alert_valid, Utility.getResourceString(context, R.string.alert_valide_territory));
                    } else {
                        item.setTerritory_name(txt_territory.getText().toString());
                        item.setiNew(false);

                        TerritoryModel last_item = new TerritoryModel();
                        last_item.setTerritory_name("");
                        last_item.setiNew(true);
                        territory_data.add(last_item);

                        territory_adapter.notifyDataSetChanged();
                    }
                }
            });

            if (item.isiNew()) {
                txt_item_territory.setVisibility(View.GONE);
                txt_territory.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.VISIBLE);
            } else {
                txt_item_territory.setVisibility(View.VISIBLE);
                txt_item_territory.setText(item.getTerritory_name());

                txt_territory.setVisibility(View.GONE);
                img_add.setVisibility(View.GONE);
            }
            txt_territory_item_label.setText(String.format("TERRITORY #%d", position + 1));

            return v;
        }
    }
}
