package com.example.lost_animals.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lost_animals.R;

public class SignInPhotoFragment0 extends Fragment {
    public static SignInPhotoFragment0 newInstance() {
        
        Bundle args = new Bundle();
        
        SignInPhotoFragment0 fragment = new SignInPhotoFragment0();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.sign_in_photo_0,container,false);
        return rootView;

    }
}
