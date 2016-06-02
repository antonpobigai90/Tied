package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddInviteFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = AddInviteFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private Button continue_btn, later, via_app, vai_sms, via_email;
    private ProgressBar progressBar;

    private String emailText, passwordText;

    public AddInviteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_add_invite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view){

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        vai_sms = (Button) view.findViewById(R.id.vai_sms);
        via_email = (Button) view.findViewById(R.id.via_email);
        via_app = (Button) view.findViewById(R.id.via_app);
        later = (Button) view.findViewById(R.id.later);
        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        later.setOnClickListener(this);
        via_email.setOnClickListener(this);
        vai_sms.setOnClickListener(this);
        via_app.setOnClickListener(this);
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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action,bundle);
        }
    }

    public void continue_action() {

    }

    public boolean validated(){
        return true;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = getArguments();
        Gson gson = new Gson();
        String user_json = bundle.getString("user");
        User user = gson.fromJson(user_json, User.class);
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.via_app:
                user.setSign_up_stage(Constants.CoWorker);
                boolean saved = user.save(getActivity().getApplicationContext());
                if(saved){
                    String json = gson.toJson(user);
                    bundle.putString(Constants.USER, json);
                    progressBar.setVisibility(View.INVISIBLE);
                    nextAction(Constants.CoWorker, bundle);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.later:
                user.setSign_up_stage(Constants.Completed);
                saved = user.save(getActivity().getApplicationContext());
                if(saved){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(Constants.USER, user);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}