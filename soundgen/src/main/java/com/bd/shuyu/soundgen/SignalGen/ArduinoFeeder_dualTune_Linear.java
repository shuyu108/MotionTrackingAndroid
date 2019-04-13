package com.bd.shuyu.soundgen.SignalGen;

import android.util.Log;

public class ArduinoFeeder_dualTune_Linear {

    public static final String TAG = "AudioGen";
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    //Arduino ADC has a sampling rate of 8000Hz max
    //Generate linear signal from 300Hz to 2100Hz for degree, and from 2500Hz to 4000Hz for magnitude

    //angle being modulated from 300 to 2100 Hz
    static final double paramA2F_angle_scale = 5;
    static final double paramA2F_angle_bias = 300;

    static final double paramA2F_strength_scale = 1500;
    static final double paramA2F_strength_bias = 2500;
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
        double freqOfTone_angle = paramA2F_angle_scale * angle + paramA2F_angle_bias;
        double freqOfTone_strength = paramA2F_strength_scale * strength + paramA2F_strength_bias;
        Log.d(TAG, " " + freqOfTone_strength);
        Log.d(TAG, " " + freqOfTone_angle);
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

            byte sp = (byte) (0.5 * (Math.sin(2 * Math.PI * freqOfTone_angle * i / sampleRate)
                    * 127 + Math.sin(2 * Math.PI * freqOfTone_strength * i / sampleRate) * 127)  + 127) ;
            generatedSnd[i] = sp;

        }
        return generatedSnd;
    }



}
