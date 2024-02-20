package com.project1.mycrashgame.Utilities;

import android.os.Handler;
import android.os.Looper;

import com.project1.mycrashgame.Interfaces.Callback_Timer;

import java.util.Timer;
import java.util.TimerTask;

public class MyTImer {
    private static final long DELAY = 600;
    private Timer timer ;
    private Callback_Timer callbackTimer;


    public MyTImer(Callback_Timer callbackTimer) {
        timer=new Timer();
        this.callbackTimer = callbackTimer;
    }

    public void timerOn(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(callbackTimer !=null){
                    callbackTimer.tick();
                }
            }
        }, DELAY, DELAY);
    }

    public void timerOff(){
        timer.cancel();
    }
}
