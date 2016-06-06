package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.IndustryModel;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.ServerInfo;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class IndustryFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = IndustryFragment.class
            .getSimpleName();

    private RelativeLayout continue_btn;

    ArrayList<IndustryModel> industry_data = new ArrayList<IndustryModel>();
    ListView industry_listview;
    SearchAdapter industry_adapter;

    Context context;
    SharedPreferences prefs;
    String str_industry;
    String[] selected_id;

    private SignUpFragmentListener mListener;

    ArrayList<String> industries = new ArrayList<String>();

    private Bundle bundle;
    // Reference to our image view we will use
    public ImageView img_user_picture;

    public IndustryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_industry, container, false);
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
            mListener.onFragmentInteraction(Constants.AddBoss, bundle);
        }
    }

    public void initComponent(View view) {

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            Uri myUri = Uri.parse(user.getAvatar_uri());
            img_user_picture.setImageURI(myUri);
        }

        industry_listview = (ListView) view.findViewById(R.id.industry_listview);
        industry_adapter = new SearchAdapter(industry_data, getActivity());
        industry_listview.setAdapter(industry_adapter);
        industry_listview.setDividerHeight(0);

        industry_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IndustryModel item = industry_data.get(position);
                if (item.isCheck_status()) {
                    item.setCheck_status(false);
                } else {
                    item.setCheck_status(true);
                }
                industry_adapter.notifyDataSetChanged();
            }
        });
        DialogUtils.displayProgress(getActivity());
        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<List<IndustryModel>> response = signUpApi.getIndustries();
        response.enqueue(new Callback<List<IndustryModel>>() {
            @Override
            public void onResponse(Call<List<IndustryModel>> call, Response<List<IndustryModel>> listResponse) {
                if (getActivity() == null) return;
                DialogUtils.closeProgress();
                List<IndustryModel> industryModelList = listResponse.body();
                industry_data.addAll(industryModelList);
                industry_adapter.notifyDataSetChanged();
                Log.d(TAG + " onResponse", industryModelList.toString());
            }

            @Override
            public void onFailure(Call<List<IndustryModel>> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                for (int i = 0 ; i < industry_data.size() ; i++) {
                    IndustryModel item = industry_data.get(i);
                    if (item.isCheck_status()) {
                       industries.add(item.getName());
                    }
                }
                continue_action();
                break;
        }
    }

    class SearchAdapter extends ArrayAdapter<IndustryModel> {

        private ArrayList<IndustryModel> itemList;
        private Context context;


        public SearchAdapter(ArrayList<IndustryModel> itemList, Context ctx) {
            super(ctx, android.R.layout.simple_list_item_1, itemList);
            this.itemList = itemList;
            this.context = ctx;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(R.layout.industry_list_item, null);
            }

            final IndustryModel item = industry_data.get(position);

            ImageView img_status = (ImageView) v.findViewById(R.id.img_status);

            TextView txt_industry_item_label= (TextView) v.findViewById(R.id.txt_industry_item_label);
            txt_industry_item_label.setText(item.getName());

            if (item.isCheck_status()) {
                img_status.setBackgroundResource(R.mipmap.checked_icon);
                txt_industry_item_label.setTextColor(getResources().getColor(R.color.text_disable_color));
            } else {
                img_status.setBackgroundResource(R.mipmap.empty_unchecked_icon);
                txt_industry_item_label.setTextColor(Color.WHITE);
            }
            return v;
        }
    }

    public void continue_action() {
        if(industries.size() > 0){
            DialogUtils.displayProgress(getActivity());

            bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setIndustries(industries);
            user.setSign_up_stage(Constants.AddBoss);

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<ServerInfo> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<ServerInfo>() {
                @Override
                public void onResponse(Call<ServerInfo> call, Response<ServerInfo> UpdateUserResponse) {
                    ServerInfo UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG + " onResponse", UpdateUserResponse.body().toString());
                    if (UpdateUser.isSuccess()) {
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER, json);
                            DialogUtils.closeProgress();
                            nextAction(bundle);
                        } else {
                            DialogUtils.closeProgress();
                            Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), UpdateUser.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    DialogUtils.closeProgress();
                }

                @Override
                public void onFailure(Call<ServerInfo> UpdateUserCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG + " onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }else{
            Toast.makeText(getActivity(), "No industry selected", Toast.LENGTH_LONG).show();
        }
    }
}
