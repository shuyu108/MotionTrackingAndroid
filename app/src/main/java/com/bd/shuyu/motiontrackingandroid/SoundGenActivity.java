package com.bd.shuyu.motiontrackingandroid;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bd.shuyu.motiontrackingandroid.SignalGen.ArduinoFeeder;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class SoundGenActivity extends AppCompatActivity {

    Handler handler = new Handler();
    static final double paramA2F_power = 360 / Math.log(4000/100);
    static final double paramA2F_coeff = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundgen);

        JoystickView joystick = (JoystickView) findViewById(R.id.JoyStick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // Use a new tread as this can take a while

                double freq = paramA2F_coeff * Math.exp(angle / paramA2F_power);
                ArduinoFeeder.genTone(freq, (double) strength / 100);
                handler.post(new Runnable() {

                    public void run() {
                        ArduinoFeeder.playSound();
                    }


                });
            }
        }, 30);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
