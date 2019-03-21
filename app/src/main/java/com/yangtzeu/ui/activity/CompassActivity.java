package com.yangtzeu.ui.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.blankj.utilcode.util.ScreenUtils;
import com.lib.compass.ChaosCompassView;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;

public class CompassActivity extends BaseActivity {
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private ChaosCompassView chaosCompassView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenUtils.setFullScreen(this);
        super.onCreate(savedInstanceState);

        chaosCompassView = new ChaosCompassView(this);
        chaosCompassView.setTextColor(getResources().getColor(R.color.colorPrimary));
        setContentView(chaosCompassView);
        init();
    }


    @Override
    public void findViews() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    public void setEvents() {
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                chaosCompassView.setVal(event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        Sensor mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(mSensorEventListener, mOrientation, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mSensorEventListener);
    }
}
