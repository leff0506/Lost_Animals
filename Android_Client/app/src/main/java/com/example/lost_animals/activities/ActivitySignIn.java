package com.example.lost_animals.activities;

import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.example.lost_animals.R;
import com.example.lost_animals.fragments.FragmentSignIn;

import com.example.lost_animals.fragments.SingleFragmentActivity;

public class ActivitySignIn extends SingleFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

    }

    @Override
    protected String getName() {
        return "Login_Activity";
    }

    @Override
    protected int getFrameId() {
        return R.id.intro_frame;
    }

    @Override
    protected Fragment getInitialFragment() {
        return FragmentSignIn.newInstance();
    }
}
