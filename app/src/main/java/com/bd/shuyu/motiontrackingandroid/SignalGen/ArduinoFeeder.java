package com.bd.shuyu.motiontrackingandroid.SignalGen;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bd.shuyu.motiontrackingandroid.R;

public class ArduinoFeeder {


    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    public static final int fps = 30;
    private static final float duration = (float)1/fps; // seconds
    private static final int sampleRate = 8000;
    private static final int numSamples = (int) (duration * sampleRate);
    private static final double sample[] = new double[numSamples];
    //private static final double freqOfTone = 440; // hz

    //private static final byte generatedSnd[] = new byte[2 * numSamples];
    private static final byte generatedSnd[] = new byte[numSamples];
    public static final String TAG = "AudioGen";



    public static void genTone(double freqOfTone, double loudness){
        short val = 0;
        /*
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((loudness * dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        */
        long sum = 0;
        for(int i = 0; i < numSamples; i++){
            //val = (short) (Math.sin(2 * Math.PI * freqOfTone * i / sampleRate)
            //        * 127 * loudness);
            //Log.d(TAG, "VALUE:  " + val);
            //generatedSnd[i] = (byte) (val & 0x00ff);

            byte sp = (byte)(Math.sin(2 * Math.PI * freqOfTone * i / sampleRate)
                    * 127 * loudness + 127) ;
            generatedSnd[i] = sp;
            sum += sp;
            //generatedSnd[2*i] = sp;
            //generatedSnd[2*i+1] = (byte) 0;
        }
        Log.d(TAG, "GEN AVG:  " + (float) sum/generatedSnd.length);


        //Log.d(TAG, "generatedLength: " + generatedSnd.length);
    }

    public static void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        //Log.d(TAG, "  " + audioTrack.getBufferSizeInFrames());
        //audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        audioTrack.release();
    }
}
