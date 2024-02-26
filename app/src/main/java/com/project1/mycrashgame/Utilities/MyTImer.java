package com.project1.mycrashgame.Utilities;

import android.os.Handler;
import android.os.Looper;

import com.project1.mycrashgame.Interfaces.Callback_Timer;

import java.util.Timer;
import java.util.TimerTask;

public class MyTImer {
    private int delay;
    private Timer timer ;
    private Callback_Timer callbackTimer;


    public MyTImer(Callback_Timer callbackTimer,int delay) {
        timer=new Timer();
        this.callbackTimer = callbackTimer;
        this.delay=delay;
    }

    public void timerOn(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(callbackTimer !=null){
                    callbackTimer.tick();
                }
            }
        }, delay, delay);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void timerOff(){
        timer.cancel();
    }
}
