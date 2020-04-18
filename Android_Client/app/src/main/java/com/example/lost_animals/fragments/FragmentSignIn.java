package com.example.lost_animals.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lost_animals.R;
import com.example.lost_animals.adapters.SlidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentSignIn extends Fragment {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private List<Fragment> photo_slide_list = new ArrayList<>();
    public static FragmentSignIn newInstance() {

        Bundle args = new Bundle();

        FragmentSignIn fragment = new FragmentSignIn();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_sign_in,container,false);
        photo_slide_list.add(SignInPhotoFragment0.newInstance());
        photo_slide_list.add(SignInPhotoFragment1.newInstance());
        photo_slide_list.add(SignInPhotoFragment2.newInstance());
        pager = v.findViewById(R.id.sign_in_pager);
        pagerAdapter = new SlidePagerAdapter(getFragmentManager(),photo_slide_list);
        pager.setAdapter(pagerAdapter);

        return v;
    }
}
