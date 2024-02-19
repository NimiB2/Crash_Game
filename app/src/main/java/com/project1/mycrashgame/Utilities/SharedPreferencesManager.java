package com.project1.mycrashgame.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String DB_FILE = "DB_FILE";
    private static volatile SharedPreferencesManager instance = null;
    private SharedPreferences sharedPreferences;

    private SharedPreferencesManager(Context context) {
        this.sharedPreferences =context.getSharedPreferences(DB_FILE,Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        synchronized (SharedPreferencesManager.class) {
            if (instance == null) {
                instance = new SharedPreferencesManager(context);
            }
        }
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

}
