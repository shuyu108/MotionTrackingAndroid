package com.bd.shuyu.motiontrackingandroid;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bd.shuyu.motiontrackingandroid.SignalGen.ArduinoFeeder;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class SoundGenActivity extends AppCompatActivity {

    private static final String TAG = "SoundGenActivity";
    //Handler handler = new Handler();
    static final double paramA2F_power = 360 / Math.log(4000/250);
    static final double paramA2F_coeff = 250;
    static int ANGLE, STRENGTH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundgen);
        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int sr = Integer.parseInt(am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
        Log.d(TAG, "device sample rate: " + sr);

        ArduinoFeeder.setSampleRate(sr);
        JoystickView joystick = (JoystickView) findViewById(R.id.JoyStick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                // Use a new tread as this can take a while
                /*
                double freq = paramA2F_coeff * Math.exp(angle / paramA2F_power);
                ArduinoFeeder.genTone(freq, (double) strength / 100);
                handler.post(new Runnable() {

                    public void run() {
                        ArduinoFeeder.playSound();
                    }


                });
                */

                ANGLE = angle;
                STRENGTH = strength;
                final Thread thread = new Thread(new Runnable() {
                    public void run() {
                        double freq = paramA2F_coeff * Math.exp(ANGLE / paramA2F_power);
                        ArduinoFeeder.genTone(freq, (double) STRENGTH / 100);
                        ArduinoFeeder.playSound();
                        /*
                        handler.post(new Runnable() {

                            public void run() {

                            }

                        });
                        */
                    }
                });

                thread.start();


            }
        }, ArduinoFeeder.fps);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
