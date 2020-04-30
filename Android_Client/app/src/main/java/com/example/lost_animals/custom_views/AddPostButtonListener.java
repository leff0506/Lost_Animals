package com.example.lost_animals.custom_views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.lost_animals.activities.AddPostActivity;
import com.example.lost_animals.activities.MapsActivity;

public class AddPostButtonListener implements View.OnClickListener {
    private Activity activity;

    public AddPostButtonListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(activity,"Add Post",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity, AddPostActivity.class);
        activity.startActivity(intent);
    }

}
