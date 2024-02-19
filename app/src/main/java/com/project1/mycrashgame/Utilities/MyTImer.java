package com.project1.mycrashgame.Utilities;

import com.project1.mycrashgame.Interfaces.Callback_Timer;

import java.util.Timer;
import java.util.TimerTask;

public class MyTImer {
    private static final long DELAY = 800;
    private Timer timer;

    private Callback_Timer callbackTimer;

    public MyTImer(Callback_Timer callbackTimer) {
        timer = new Timer();
        this.callbackTimer = callbackTimer;
    }

    public void timerOn(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                callbackTimer.tick();
            }
        },DELAY,DELAY);
    }

    public void timerOff(){
        timer.cancel();
    }
}
