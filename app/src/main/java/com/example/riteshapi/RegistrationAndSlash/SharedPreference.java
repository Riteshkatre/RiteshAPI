package com.example.riteshapi.RegistrationAndSlash;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.riteshapi.Variablebags.VeriableBag;

public class SharedPreference {
    private static final String SHARED_PREF_NAME = "MyAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_LOGGED_OUT = "isLogOut";



    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void setStringvalue(String key,String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringvalue(String key) {
        return sharedPreferences.getString(key,"");
    }
    public void setUserId(String userId) {
        editor.putString(VeriableBag.User_id,userId).commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(VeriableBag.User_id,"");
    }


}
