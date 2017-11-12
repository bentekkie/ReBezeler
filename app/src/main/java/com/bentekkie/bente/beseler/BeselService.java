package com.bentekkie.bente.beseler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class BeselService extends Service implements View.OnClickListener,SensorEventListener {
    private ImageView leftBesel;
    private ImageView rightBesel;
    private ImageView leftBeselsys;
    private ImageView rightBeselsys;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private WindowManager.LayoutParams params;
    private WindowManager.LayoutParams paramssys;
    private int width = 1;
    private WindowManager wm;
    Point size;
    @Override
    public void onCreate() {
        super.onCreate();
        final Handler handler = new Handler();
        final int delay = 500; //milliseconds


        Log.e("hello", Settings.canDrawOverlays(this)+"");
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        leftBesel = new ImageView(this);
        leftBesel.setBackgroundColor(Color.BLACK);
        rightBesel = new ImageView(this);
        rightBesel.setBackgroundColor(Color.BLACK);
        leftBeselsys = new ImageView(this);
        leftBeselsys.setBackgroundColor(Color.BLACK);
        rightBeselsys = new ImageView(this);
        rightBeselsys.setBackgroundColor(Color.BLACK);
        size = new Point();
        wm.getDefaultDisplay().getRealSize(size);
        width = 1;
        params = new WindowManager.LayoutParams(
                width,
                size.y*2,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        paramssys = new WindowManager.LayoutParams(
                width,
                size.y*2,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT;
        wm.addView(leftBesel, params);
        params.gravity = Gravity.RIGHT;
        wm.addView(rightBesel, params);
        paramssys.gravity = Gravity.LEFT;
        wm.addView(leftBeselsys, paramssys);
        paramssys.gravity = Gravity.RIGHT;
        wm.addView(rightBeselsys, paramssys);
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                width += 1;
                setWidth();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        width += size.x/200;
        setWidth();
    }

    private void setWidth(){
        params.width = width;
        params.gravity = Gravity.LEFT;
        wm.updateViewLayout(leftBesel, params);
        params.gravity = Gravity.RIGHT;
        wm.updateViewLayout(rightBesel, params);
        paramssys.width = width;
        paramssys.gravity = Gravity.LEFT;
        wm.updateViewLayout(leftBeselsys, paramssys);
        paramssys.gravity = Gravity.RIGHT;
        wm.updateViewLayout(rightBeselsys, paramssys);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        if (mAccel > 11) {
            width = 1;
            setWidth();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
