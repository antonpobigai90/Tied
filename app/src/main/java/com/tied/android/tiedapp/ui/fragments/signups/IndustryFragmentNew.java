package com.tied.android.tiedapp.ui.fragments.signups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.adapters.IndustryAdapter;

import java.util.ArrayList;

/**
 * Created by hitendra on 9/9/2016.
 */
public class IndustryFragmentNew extends Fragment {
    private EditText etIndustry;
    private ListView lvIndustry;
    ArrayList<String> industryList = new ArrayList<String>();
    IndustryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_industry, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        industryList.add("Apple");
        industryList.add("Banana");
        industryList.add("United States");
        industryList.add("United Kingdom");
        industryList.add("Germany");
        industryList.add("Tied");

        etIndustry = (EditText) rootView.findViewById(R.id.etIndustry);
        lvIndustry = (ListView) rootView.findViewById(R.id.lvIndustry);
        adapter = new IndustryAdapter(getActivity(), industryList);
        lvIndustry.setAdapter(adapter);

        etIndustry.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }
}
