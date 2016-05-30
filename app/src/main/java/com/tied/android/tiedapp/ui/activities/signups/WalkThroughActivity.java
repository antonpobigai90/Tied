package com.tied.android.tiedapp.ui.activities.signups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.WalkThroughFragment;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;


/**
 * Created by yuweichen on 16/4/30.
 */
public class WalkThroughActivity extends Activity implements View.OnClickListener{

    public static final String TAG = WalkThroughFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private Button register, sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_welcome);

        User user = User.getUser(getApplicationContext());
        if(user != null){
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }

        initComponent();
    }

    public void initComponent(){

        register = (Button) findViewById(R.id.register);
        sign_in = (Button) findViewById(R.id.sign_in);

        register.setOnClickListener(this);
        sign_in.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Intent intent = new Intent(this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.sign_in:
                Intent intent2 = new Intent(this, SignInActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                break;
        }
    }
}
