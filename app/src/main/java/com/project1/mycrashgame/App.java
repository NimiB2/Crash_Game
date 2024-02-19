package com.project1.mycrashgame;

import android.app.Application;

import com.project1.mycrashgame.Utilities.MySignal;
import com.project1.mycrashgame.Utilities.SharedPreferencesManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
        MySignal.init(this);
    }
}
