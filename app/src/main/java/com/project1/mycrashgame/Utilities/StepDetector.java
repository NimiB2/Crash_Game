package com.project1.mycrashgame.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.project1.mycrashgame.Interfaces.Callback_Sensors;

public class StepDetector {
   private final int CHECK_TIME = 300;
   private final Float CHECK_MOBILITY = 3.0F;
    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_LEFT = -1;

    private static final int FAST = 600;
    private static final int SLOW = 800;
    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Callback_Sensors callback_sensors;

    private int stepX=0;
    private int stepY=0;
    private long timeStamp=0;

    public StepDetector(Context context, Callback_Sensors callback_sensors) {
        this.sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.context = context;
        this.callback_sensors = callback_sensors;

        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener= new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x= event.values[0];
                float y= event.values[1];
                calculateStep(x,y);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

    }

    private void calculateStep(float x, float y) {
        if(System.currentTimeMillis()-timeStamp>CHECK_TIME){
            timeStamp=System.currentTimeMillis();
            if(callback_sensors!=null){
                if(x>CHECK_MOBILITY){
                    callback_sensors.step(MOVE_LEFT);
                }
                else if(x<-(CHECK_MOBILITY)){
                    callback_sensors.step(MOVE_RIGHT);
                }
                    if(y>CHECK_MOBILITY){
                        callback_sensors.speed(FAST);
                    }
                    else if(y<-(CHECK_MOBILITY)){
                        callback_sensors.speed(SLOW);
                    }
            }

        }

    }

    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }
    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
