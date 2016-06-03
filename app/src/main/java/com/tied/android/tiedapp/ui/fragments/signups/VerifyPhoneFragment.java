package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class VerifyPhoneFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = VerifyPhoneFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

//    private Button continue_btn;
    private RelativeLayout continue_btn;
    private ProgressBar progressBar;
    private EditText code;

    private String codeText;

    public VerifyPhoneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_verify_code, container, false);
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
            mListener.onFragmentInteraction(Constants.OfficeAddress,bundle);
        }
    }

    public void initComponent(View view){

        code = (EditText) view.findViewById(R.id.code);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
    }


    public void continue_action(){
        if(validated()){
            //Todo Verify Code from Server request should be sent here.
            Bundle bundle = getArguments();
            nextAction(bundle);

        }else{
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }



    public boolean validated(){
        String code_sent = getArguments().getString(Constants.CODE);
        codeText = code.getText().toString();
        return codeText.equals(code_sent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
        }
    }
}