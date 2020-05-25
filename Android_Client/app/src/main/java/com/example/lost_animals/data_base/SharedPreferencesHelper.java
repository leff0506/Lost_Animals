package com.example.lost_animals.data_base;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String PREFERENCES_KEY = "PREFERENCES_KEY";
    private SharedPreferences mSharedPrefernces;

    public SharedPreferencesHelper(Context context){
        mSharedPrefernces = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }
    public int getSettings(){
        int setting = mSharedPrefernces.getInt(PREFERENCES_KEY, Color.MAGENTA);
        return setting;
    }

    public void setSettings(int setting){
        mSharedPrefernces.edit().putInt(PREFERENCES_KEY,setting).apply();
    }
}
