package com.tied.android.tiedapp.ui.fragments.signups;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.HelpModel;
import com.tied.android.tiedapp.ui.adapters.HelpAdapter;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import java.util.ArrayList;

/**
 * Created by greepon123 on 6/6/2016.
 */
public class HelpFragment extends Fragment implements AdapterView.OnItemClickListener {

    private SignUpFragmentListener mListener;
    ListView list;

    int[] IMAGE = {R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon};
    String[] TITLE = {"How does Tied work?","How can I add multiple clients?","Who can use Tied?","Is Tied that helpful?","How much does it cost?"};
//    String [] DESCRIPTION = getResources().getStringArray(R.array.help_description);
    String [] DESCRIPTION = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun"};


    private ArrayList<HelpModel> helpArrayList;
    private HelpAdapter helpAdapter;

    public HelpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help_listview, container, false);

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

    public void initComponent(View view) {
        list= (ListView) view.findViewById(R.id.list);

        helpArrayList = new ArrayList<HelpModel>();


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


}
