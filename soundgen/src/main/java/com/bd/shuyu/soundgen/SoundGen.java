package com.bd.shuyu.soundgen;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import java.lang.Double;

import com.bd.shuyu.soundgen.SignalGen.ArduinoFeeder;
import com.bd.shuyu.soundgen.SignalGen.ArduinoFeeder_dualTune_Linear;
import com.bd.shuyu.soundgen.SignalGen.RetDouble;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static java.lang.System.nanoTime;

public class SoundGen extends AppCompatActivity {

    private static final String TAG = "SoundGenActivity";
    //Handler handler = new Handler();


    boolean isJoyStickJustTouched = false;
    int ANGLE, STRENGTH;   // need class variables to pass params into Runnable
    byte generateSnd[];
    final static float BUFFER_DELAY = 0.04f;
    Switch recenterSwitch;
    int SLEEP_TIME_MILLISECOND = 100;

    RetDouble phase_sin = new RetDouble(0);
    double t_buffered = 0 ;

    AudioTrack audioTrack;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundgen);
        recenterSwitch = findViewById(R.id.switch1);
        final JoystickView joystick = (JoystickView) findViewById(R.id.JoyStick);
        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);


        int sr = Integer.parseInt(am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
        Log.i(TAG, "device sample rate: " + sr);

        //Create an AudioTrack Instance on Stream Mode
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC, sr, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, (int)Math.floor(sr * BUFFER_DELAY),
                AudioTrack.MODE_STREAM);
        //get actual buffer length to determine the audio latency
        if(Build.VERSION.SDK_INT >= 24) Log.i(TAG, "Actual Buffer Size:  " + audioTrack.getBufferSizeInFrames() + "Actual Buffer Delay:   " + audioTrack.getBufferSizeInFrames()/sr);

        ArduinoFeeder.setSampleRate(sr);



        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                ANGLE = angle;
                STRENGTH = strength;
                double dur = BUFFER_DELAY / 2;

                if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING){

                    audioTrack.play();
                    //Log.d(TAG, "audioTrack enabled   " + audioTrack.getPlayState());
                }

                Thread gen_1 = new Thread(new GenSound(dur));
                t_buffered += dur;
                gen_1.start();

                if(t_buffered < BUFFER_DELAY){
                    Thread gen_2 = new Thread(new GenSound(dur));
                    t_buffered += dur;
                    gen_2.start();
                }

                isJoyStickJustTouched = true;
                t_buffered += (double) - 1/30;
            }
        },30);


        //continuously check if the joystick is enabled or not
        Thread keeper_thread = new Thread(new Runnable(){

            @Override
            public void run(){

                long prevTime = System.nanoTime();

                while(!Thread.interrupted()){
                    try {
                        Thread.sleep(SLEEP_TIME_MILLISECOND);
                        t_buffered -= (double) SLEEP_TIME_MILLISECOND / 1000;
                    }catch(Exception e){

                        Log.d(TAG, "keeper thread interrupted");
                        this.run();
                        break;
                    }
                    joystick.setAutoReCenterButton(recenterSwitch.isChecked());
                    //if HANDS ON
                    if(isJoyStickJustTouched){
                        if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                            audioTrack.play();
                            //Log.d(TAG, "audioTrack enabled   " + audioTrack.getPlayState() + "   " + joystick.getNormalizedX()  + "  " + joystick.getNormalizedY());
                        }

                        //reset Detection Variable
                        isJoyStickJustTouched = false;
                        SLEEP_TIME_MILLISECOND = 100;
                        continue;
                    }


                    //if HANDS LEFT
                    if (!isJoyStickJustTouched
                            && audioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED){

                        if(recenterSwitch.isChecked()){

                            audioTrack.stop();
                            audioTrack.flush();
                            t_buffered = 0;

                            // * FOR wPHASE OPTION:
                            //phase_sin.setVal(0);

                            //Log.d(TAG, "audioTrack disabled");

                            SLEEP_TIME_MILLISECOND = 100;

                        }else{
                            SLEEP_TIME_MILLISECOND = (int) (BUFFER_DELAY*1000);
                            Log.d(TAG, joystick.getNormalizedX() + "  " + joystick.getNormalizedY());
                            //Log.d(TAG, (System.nanoTime() - prevTime)/1000000 + "");
                            long ELAPSED_TIME = (System.nanoTime() - prevTime)/1000000 ;
                            if(ELAPSED_TIME > 200) ELAPSED_TIME = (int) (BUFFER_DELAY*1000);
                            prevTime = System.nanoTime();
                            int itr = 2;
                            double dur = BUFFER_DELAY / 2;
                            for (int i=0; i<itr; ++i){

                                Thread tmpThread = new Thread(new GenSound(dur));
                                t_buffered += dur;
                                tmpThread.start();
                                /*
                                try{
                                    tmpThread.join(10);

                                }catch(Exception e){}
                                 */
                            }
                        }
                    }
                }
            }
        });
        //Log.d(TAG, AudioTrack.getMaxVolume() + "  " + audioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT));
        Log.d(TAG, joystick.getNormalizedX() +"   "+ joystick.getNormalizedY());
        keeper_thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class GenSound implements Runnable{

        double Dur;
        public GenSound(double dur){
            Dur = dur;
        }
        @Override
        public void run(){

            genSound(Dur);
        }
    }
    public synchronized void genSound(double duration){


        //FORMAT is PCM_8BIT !!
        generateSnd = ArduinoFeeder.genTone(duration, ANGLE, (double) STRENGTH / 100, phase_sin);
        audioTrack.write(generateSnd, 0, generateSnd.length, AudioTrack.WRITE_BLOCKING);

    }

    class JoyStickMove implements JoystickView.OnMoveListener{



        @Override
        public void onMove(int angle, int strength){

            ANGLE = angle;
            STRENGTH = strength;


            isJoyStickJustTouched = true;
            /*
            final Thread thread = new Thread(new Runnable() {
                public void run() {



                }
            }, "ArduinoFeeder");

            thread.start();
            */
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

        }
    }
}
