package com.bd.shuyu.soundgen.SignalGen;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class ArduinoFeeder {

    public static final String TAG = "AudioGen";
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    static final double paramA2F_angle_power = 360 / Math.log(4000/250);
    static final double paramA2F_angle_coeff = 250;
    static final double paramA2F_strength_power = Math.log(8000/4000);
    static final double paramA2F_strength_coeff = 4000;
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
        double freqOfTone_angle = paramA2F_angle_coeff * Math.exp(angle / paramA2F_angle_power);
        double freqOfTone_strength = paramA2F_strength_coeff * Math.exp(strength / paramA2F_strength_power);
        duration = dur;
        numSamples = (int) Math.floor(duration * sampleRate);

        generatedSnd = new byte[numSamples];
        for (int i = 0; i < numSamples; i++) {

            byte sp = (byte) (0.5 * (Math.sin(2 * Math.PI * freqOfTone_angle * i / sampleRate)
                     +Math.sin(2 * Math.PI * freqOfTone_strength * i / sampleRate)) * 127  + 127) ;
            generatedSnd[i] = sp;

        }
        return generatedSnd;
    }

}

