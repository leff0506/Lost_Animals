package com.example.lost_animals.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lost_animals.R;

public class SignInPhotoFragment1 extends Fragment {
    public static SignInPhotoFragment1 newInstance() {
        
        Bundle args = new Bundle();
        
        SignInPhotoFragment1 fragment = new SignInPhotoFragment1();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.sign_in_photo_1,container,false);
        return rootView;

    }
}
