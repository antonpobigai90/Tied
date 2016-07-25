package com.tied.android.tiedapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.ui.MyEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Femi on 7/21/2016.
 */
public class MyFormFragment extends Fragment {
   protected Map<Integer, View> myFieldsMap=new HashMap<Integer, View>();
    protected View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_general, container, false);

        return view;
    }
    public void loadData() {

    }
    public void addEditTextToMap(Integer resourceID) {
        myFieldsMap.put(resourceID, (MyEditText)view.findViewById(resourceID));
    }
    public void addViewToMap(Integer resourceID) {
        myFieldsMap.put(resourceID, view.findViewById(resourceID));
    }
    public void addViewToMap(View v) {
        myFieldsMap.put(v.getId(), view);
    }
    public Map getFields() {
        return myFieldsMap;
    }
}
