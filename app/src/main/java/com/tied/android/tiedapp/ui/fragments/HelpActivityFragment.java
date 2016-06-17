package com.tied.android.tiedapp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.HelpModel;
import com.tied.android.tiedapp.ui.adapters.HelpAdapter;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HelpActivityFragment extends Fragment implements AdapterView.OnItemClickListener , View.OnClickListener{

    ListView list;
    private RelativeLayout done;

    int[] IMAGE = {R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon};
    String[] TITLE = {"How does Tied work?","How can I add multiple clients?","Who can use Tied?","Is Tied that helpful?","How much does it cost?"};
    //    String [] DESCRIPTION = getResources().getStringArray(R.array.help_description);
    String [] DESCRIPTION = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun"};


    private ArrayList<HelpModel> helpArrayList;
    private HelpAdapter helpAdapter;

    Bundle bundle;
    int index = 0;

    public HelpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view) {
        list= (ListView) view.findViewById(R.id.list);
        done = (RelativeLayout) view.findViewById(R.id.done);
        done.setOnClickListener(this);
        helpArrayList = new ArrayList<HelpModel>();

        bundle = getArguments();
        if(bundle != null){
            index = bundle.getInt(Constants.PREVIOUS);
        }

        for (int i = 0; i < TITLE.length; i++) {

            HelpModel helpClass = new HelpModel(TITLE[i], DESCRIPTION[i],IMAGE[i]);
            helpArrayList.add(helpClass);

        }
        helpAdapter = new HelpAdapter(getActivity(), helpArrayList);
        list.setAdapter(helpAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:

                break;
        }
    }
}
