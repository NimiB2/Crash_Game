package com.project1.mycrashgame.Utilities;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.project1.mycrashgame.R;

public class MySignal {
    private static MySignal instance=null;
    private static Vibrator vibrator;
    private Context context;

    private MySignal(Context context){
        this.context=context;
    }

    public static void init(Context context) {
        synchronized (MySignal.class) {
            if (instance == null) {
                instance = new MySignal(context);
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            }
        }
    }

    public void toast(String message) {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    public void vibrate(long miliSec) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(miliSec, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(miliSec);
        }
    }

    public static MySignal getInstance() {
        return instance;
    }
}
