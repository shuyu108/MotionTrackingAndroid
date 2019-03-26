package com.bd.shuyu.soundgen.SignalGen;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class ArduinoFeeder_wPhase {

    public static final String TAG = "AudioGen";
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    static final double paramA2F_power = 360 / Math.log(4000/250);
    static final double paramA2F_coeff = 250;
    public static final int fps = 30;
    private static double duration = 1/fps; // seconds
    private static int sampleRate = 48000;
    private static int numSamples = (int) (duration * sampleRate);

    //private static final double freqOfTone = 440; // hz

    private static byte generatedSnd[];
    //private static byte generatedSnd[] = new byte[numSamples];


    public static void setSampleRate(int sr){
        if(sampleRate == sr) return;

        sampleRate = sr;

    }


    public synchronized static byte[] genTone(double dur, int angle, double strength, RetDouble phase_sin) {
        double freqOfTone = paramA2F_coeff * Math.exp(angle / paramA2F_power);
        duration = dur;
        numSamples = (int) Math.floor(duration * sampleRate);
        /*
        To Make The Signal Length Integer Multiple of the Frequency(WaveLength)
        double SAMPLES_PER_WAVELENGTH = 1 / freqOfTone * sampleRate;
        //int tmpSamples = (int) Math.floor(Math.floor(numSamples / SAMPLES_PER_WAVELENGTH) * SAMPLES_PER_WAVELENGTH);
        generatedSnd = new byte[tmpSamples];
        */



        generatedSnd = new byte[numSamples];
        for (int i = 0; i < numSamples; i++) {

            byte sp = (byte) (Math.sin(2 * Math.PI * freqOfTone * i / sampleRate + phase_sin.getVal())
                    * 127 * strength + 127);
            generatedSnd[i] = sp;

        }

        phase_sin.setVal(Math.asin(Math.sin( 2*Math.PI * freqOfTone * numSamples / sampleRate + phase_sin.getVal())));
        Log.d(TAG, "phase updated:  " + phase_sin.getVal()/(2*Math.PI) + "pi");
        /*
        numSamples = numSamples - 1;

        //'PADDING' the REMAINDINGS!!!!!!!!
        byte paddingSnd[] = new byte[numSamples-tmpSamples];
        int paddingLength = numSamples - tmpSamples;
        int freqOfPadding = 0;
        if(paddingLength > (double) 0.5 /freqOfTone * sampleRate){

            freqOfPadding = (int) Math.floor(1/paddingLength*sampleRate);

            Log.d(TAG, "larger than HALF WAVELENGTH");

        }else if(paddingLength < (double) 0.5 /freqOfTone * sampleRate){

            freqOfPadding = (int) Math.floor(0.5 / paddingLength*sampleRate);

            Log.d(TAG, "smaller than HALF WAVELENGTH");

        }

        //generate a sin function from '0' to ensure phase!
        for (int i = 0; i<paddingLength; i++) {

            byte sp = (byte) (Math.sin(2 * Math.PI * freqOfPadding * i / sampleRate)
                    * 127 * loudness + 127);
            generatedSnd[i + tmpSamples] = sp;
        }
        */
        return generatedSnd;
    }

    public static void playSound(){


        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);  // WAS MODE_STATIC

        //audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        audioTrack.release();
    }
    /*
    void setThreadAffinity(){

        int currentCPU = sched_getcpu();
        pid_t threadId = getid();
        cpu_set_t cpu_set;
        CPU_ZERO(cpu_set);
        CPU_SET(currentCPU, cpu_set);
        sche_setaffinity(threadId, sizeof(cpu_set_t), cpu_set);
    }

    public static void playSound_AAudio(){


    }
    */
}
