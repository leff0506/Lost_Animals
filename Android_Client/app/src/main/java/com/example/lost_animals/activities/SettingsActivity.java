package com.example.lost_animals.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.lost_animals.R;
import com.example.lost_animals.data_base.SharedPreferencesHelper;

public class SettingsActivity extends AppCompatActivity {
    private Button back;
    private static String TAG = "SettingsActivity";
    View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.setting_back);
        back.setOnClickListener(onBackClickListener);
        final SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        int color = sharedPreferencesHelper.getSettings();
        RadioGroup radioGroup =  findViewById(R.id.radio_group);
        RadioButton mag = findViewById(R.id.magenta);
        RadioButton red = findViewById(R.id.red);
        if (color == Color.MAGENTA){
            mag.setChecked(true);
            red.setChecked(false);
        }else{
            red.setChecked(true);
            mag.setChecked(false);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG,"Settings chanched");
                if (checkedId == R.id.magenta){
                    sharedPreferencesHelper.setSettings(Color.MAGENTA);
                }else{
                    sharedPreferencesHelper.setSettings(Color.RED);
                }

            }
        });
    }
}
