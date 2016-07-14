package com.tied.android.tiedapp.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.IndustryModel;
import com.tied.android.tiedapp.ui.adapters.ClientSelectIndustryAdapter;
import com.tied.android.tiedapp.ui.fragments.client.AddClientFragment;

import java.util.ArrayList;

/**
 * Created by ZuumaPC on 7/14/2016.
 */
public class ClientSelectIndustryDialog implements View.OnClickListener{


    public static final String TAG = ClientSelectIndustryDialog.class
            .getSimpleName();

    private SelectedListener selectedListener;

    private TextView close;
    private Dialog dialog;
    private Context context;
    private IndustryModel industryModel;
//    private User user;

    int[] id = {1, 2, 3, 4};
    String[] txt_industry = {"Food","Travel","Technology","Business"};
    Boolean[] industry_status = {true,false,false,false};

    ArrayList<IndustryModel> listIndustry = new ArrayList<IndustryModel>();
    ClientSelectIndustryAdapter adapter;
    ListView listView;

    public void showDialog(final AddClientFragment fragment){
        dialog = new Dialog(fragment.getActivity());
        context = fragment.getActivity();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_client_select_industry);

        listView = (ListView) dialog.findViewById(R.id.list);

        close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(this);

        for(int i = 0; i < id.length; i++){
            IndustryModel list_industry = new IndustryModel(id[i],txt_industry[i],industry_status[i]);
            listIndustry.add(list_industry);
        }
        adapter = new ClientSelectIndustryAdapter(listIndustry,context);
        listView.setAdapter(adapter);

        this.industryModel = listIndustry.get(0);

        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                adapter.setSelectedIndex(position);
                selectedListener = (AddClientFragment) fragment;
                industryModel = listIndustry.get(position);
                selectedListener.selectedNow(industryModel);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                dialog.dismiss();
                break;
        }
    }

    public interface SelectedListener{
        void selectedNow(IndustryModel industryModel);
    }
}
