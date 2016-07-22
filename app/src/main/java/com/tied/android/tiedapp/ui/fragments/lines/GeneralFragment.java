package com.tied.android.tiedapp.ui.fragments.lines;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.ui.MyEditText;
import com.tied.android.tiedapp.ui.fragments.MyFormFragment;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class GeneralFragment extends MyFormFragment implements View.OnClickListener{
    private ImageView avatar;
    MyEditText note;

    Spinner stateSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_general, container, false);
        initComponents();
        return view;
    }

    private void initComponents() {
        addEditTextToMap(R.id.description);
        addEditTextToMap(R.id.street);
        addEditTextToMap(R.id.city);
        addEditTextToMap(R.id.zip);

        stateSpinner = (Spinner) view.findViewById(R.id.state);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.my_spinner_item, MyUtils.States.asArrayList());
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown);
        stateSpinner.setAdapter(adapter);

    }
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar:

                break;
        }
    }


}
