package com.bd.shuyu.motiontrackingandroid;

import android.graphics.Rect;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Rect2d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerBoosting;
import org.opencv.tracking.TrackerCSRT;
import org.opencv.tracking.TrackerKCF;
import org.opencv.tracking.TrackerMIL;
import org.opencv.tracking.TrackerMOSSE;
import org.opencv.tracking.TrackerMedianFlow;
import org.opencv.tracking.TrackerTLD;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.bd.shuyu.motiontrackingandroid.OpencvNatives.OpencvNativeCls;
import com.bd.shuyu.motiontrackingandroid.SignalGen.ArduinoFeeder;
import com.bd.shuyu.motiontrackingandroid.SignalGen.RetDouble;
import com.bd.shuyu.motiontrackingandroid.interface_regionSelection.RegionSelection_Cam;
import com.bd.shuyu.motiontrackingandroid.interface_regionSelection.RegionSelection_Tracking_Cam;


public class CameraTrackingEvaluateActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    int counter = 0;



    final String TAG = "OpenCV_Camera";
    static{
        System.loadLibrary("MyLibs");
    }

    //****************************
    int ANGLE;   // need class variables to pass params into Runnable
    double STRENGTH,maxStrength;
    byte generateSnd[];
    final static float BUFFER_DELAY = 0.02f;
    int SLEEP_TIME_MILLISECOND = 100;
    RetDouble phase_sin = new RetDouble(0);
    double t_buffered = 0 ;
    AudioTrack audioTrack;
    int rectCenterX,rectCenterY,screenCenterX,screenCenterY;
    //****************************

    final String[] trackerTypes = new String[]{"BOOSTING", "MIL", "KCF", "TLD","MEDIANFLOW", "MOSSE", "CSRT"};
    //GOTURN need additional environment for CNN
    final String trackerType = trackerTypes[6];

    Tracker tracker = null;
    Mat mRgba, mRgb = new Mat();

    float scaleWIN2CAM_X, scaleWIN2CAM_Y;
    int biasWIN2CAM_X, biasWIN2CAM_Y;
    boolean isTrackerStarted = false;

    JavaCameraView javaCameraView;
    RegionSelection_Tracking_Cam camView;

    static Rect mRect, camRect;
    static Rect2d roiRect;
    static Scalar roiColor = new Scalar(255, 0, 0);

    private SensorManager mSensorManager;
    private Sensor mLinAccSensor;
    private SensorEventListener seListener;


    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_sel_tracking);
        camView = (RegionSelection_Tracking_Cam) findViewById(R.id.dragRect);

        //**********************

        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int sr = Integer.parseInt(am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC, sr, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, (int) Math.floor(sr * BUFFER_DELAY),
                AudioTrack.MODE_STREAM);
        //************************

        //Create a Callback that invoked when releasing finger
        if (null != camView) {


            camView.setOnDownCallback(new RegionSelection_Tracking_Cam.OnDownCallback() {
                @Override
                public void onRectStarted() {

                    counter = 0;


                    if (isTrackerStarted) {

                        //RESET the State of the Tracker
                        tracker.clear();
                        isTrackerStarted = false;
                    }
                    //***************************
                    if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {

                        audioTrack.stop();
                        audioTrack.flush();
                    }
                    //***************************
                }
            });

            camView.setOnMoveCallback(new RegionSelection_Tracking_Cam.OnMoveCallback() {
                @Override
                public void onRectChanging(Rect rect) {

                    //Log.d(TAG, "start drawing");
                    setDrawingRect(rect);
                }
            });

            camView.setOnUpCallback(new RegionSelection_Tracking_Cam.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                            Toast.LENGTH_LONG).show();

                    setDrawingRect(null);

                    //init tracker at this point
                    roiRect = new Rect2d(camRect.left, camRect.top,
                            camRect.right - camRect.left, camRect.bottom - camRect.top);

                    Imgproc.cvtColor(mRgba, mRgb, Imgproc.COLOR_RGBA2BGR);

                    tracker = createTracker();
                    tracker.init(mRgb, roiRect);


                    Log.d(TAG, trackerType + " " + (camRect.right - camRect.left) + "*" + (camRect.bottom - camRect.top));
                    isTrackerStarted = true;


                }
            });
        }


        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLinAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }
    @Override
    protected void onResume() {
        super.onResume();

        System.loadLibrary("MyLibs");
        if(OpenCVLoader.initDebug()){
            //Log.d(TAG, "on Resume: OpenCV successfully loaded");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            Log.d(TAG, "on Resume: not loaded");
        }
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
        seListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent e) {

                Log.i(TAG, "Sensor_measured_movement: " + e.values[0] + " " + e.values[1] + " " + e.values[2]);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {


            }
        };


        mSensorManager.registerListener(seListener, mLinAccSensor, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView!=null) javaCameraView.disableView();
        mSensorManager.unregisterListener(seListener);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView!=null) javaCameraView.disableView();
    }
    public void onCameraViewStarted(int w, int h){
        //we have 4 channels here
        mRgba = new Mat(h, w, CvType.CV_8UC3);


        //Try to scale the cameraview to fit the screen
        int screenH = camView.getHeight();
        int screenW = camView.getWidth();

        //************************
//        screenCenterX=screenW/2;
//        screenCenterY=screenH/2;
        screenCenterX=w/2;
        screenCenterY=h/2;


        scaleWIN2CAM_X = (float) w / screenH /((float) w/h) ;
        scaleWIN2CAM_Y = (float) h/screenH;

        maxStrength=Math.sqrt(screenCenterY*screenCenterY+screenCenterX*screenCenterX);
        //************************


        biasWIN2CAM_X = (int) - Math.floor((screenW - screenH * ((float) w/h)) / 2) ;
        //biasWIN2CAM_Y = (int) Math.floor((screenH - h) / 2);
        biasWIN2CAM_Y = 0;
        Log.d(TAG, "window height is:  " + Integer.toString(screenH) + "  " +
                Integer.toString(screenW) + "  "+ Integer.toString(w) + "  "+ Integer.toString(h));

    }

    @Override
    public void onCameraViewStopped(){
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        //rgba = cv2.cvtColor(rgb_data, cv2.COLOR_RGB2RGBA)

        if(mRect != null){
            //Log.d(TAG, "onCameraFrame: Drawing");
            ///Rect camRect = cvtRectWIN2CAM(mRect);
            camRect = cvtRectWIN2CAM(mRect);

            //Imgproc.rectangle(mRgba, new Point(mRect.left, mRect.top), new Point(mRect.right, mRect.bottom), roiColor, 2, 2);
            Imgproc.rectangle(mRgba, new Point(camRect.left, camRect.top), new Point(camRect.right, camRect.bottom), roiColor, 2, 2);
        }

        if(isTrackerStarted){
            //cvtRectWIN2CAM(mRect);
            float timer = (float) Core.getTickCount();
            Imgproc.cvtColor(mRgba, mRgb, Imgproc.COLOR_RGBA2BGR);
            //Log.d(TAG, "current counter: " + counter);
            boolean ok = tracker.update(mRgb, roiRect);
            float fps = (float)Core.getTickFrequency() / ((float)Core.getTickCount() - timer);
            if(ok){

                //****************************************
                rectCenterX=(int)(roiRect.x+roiRect.width/2);
                rectCenterY=(int)(roiRect.y+roiRect.height/2);

                Log.d(TAG, "Camera_measured_movement: " + (rectCenterX-screenCenterX) + " " + (screenCenterY-rectCenterY) );

                int angle=(int)Math.toDegrees(Math.atan2(screenCenterY-rectCenterY,rectCenterX-screenCenterX));
                ANGLE = angle < 0 ? angle+360 : angle;

                STRENGTH = (float)(Math.sqrt((rectCenterX-screenCenterX)*(rectCenterX-screenCenterX)+(rectCenterY-screenCenterY)*(rectCenterY-screenCenterY))/maxStrength);
                //double dur = BUFFER_DELAY;
                double dur = 1/(fps);

                if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING){

                    audioTrack.play();
                }
                audioTrack.flush();
                Thread gen_1 = new Thread(new GenSound(dur));
                t_buffered += dur;
                gen_1.start();

                //Log.d(TAG,"ANGLE="+ANGLE+"("+rectCenterX+","+rectCenterY+")->("+screenCenterX+","+screenCenterY+")");
                /*if(t_buffered < BUFFER_DELAY){
                    Thread gen_2 = new Thread(new GenSound(dur));
                    t_buffered += dur;
                    gen_2.start();
                }*/


                //t_buffered += (double) - 1/fps;

                //****************************************
                Imgproc.rectangle(mRgba, new Point(roiRect.x, roiRect.y),
                        new Point(roiRect.x + roiRect.width, roiRect.y + roiRect.height),
                        roiColor, 2, 2);
                Imgproc.putText(mRgba, "FPS: " + fps, new Point(100,80), 0, 1.5, new Scalar(0,0,255),2);
                //Log.d(TAG, "FPS: " + fps);
            }else{
                Imgproc.putText(mRgba, "Tracking failure detected", new Point(100,80), 0, 1.5, new Scalar(255,0,0),2);
            }


        }

        return mRgba;
    }
    //*****************************
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


        counter+=1;
        //Log.d(TAG, "counter is: " + counter);

        //FORMAT is PCM_8BIT !!
        generateSnd = ArduinoFeeder.genTone(duration, ANGLE,  STRENGTH , phase_sin);
        //audioTrack.write(generateSnd, 0, generateSnd.length, AudioTrack.WRITE_NON_BLOCKING);
        audioTrack.write(generateSnd, 0, generateSnd.length,AudioTrack.WRITE_BLOCKING);
    }
    //******************************
    public static void setDrawingRect(Rect rt){

        mRect = rt;
    }

    public Rect cvtRectWIN2CAM(Rect winRect){
        //Log.d(TAG, "RectWIN2CAM: Converting");
        return new Rect( (int)Math.floor(scaleWIN2CAM_X * winRect.left + biasWIN2CAM_X),
                (int)Math.floor(scaleWIN2CAM_Y * winRect.top + biasWIN2CAM_Y),
                (int)Math.floor(scaleWIN2CAM_X * winRect.right + biasWIN2CAM_X),
                (int)Math.floor(scaleWIN2CAM_Y * winRect.bottom + biasWIN2CAM_Y));
    }

    Tracker createTracker(){

        switch(trackerType){

            case "BOOSTING":   return TrackerBoosting.create();

            case "MIL": return TrackerMIL.create();

            case "KCF": return TrackerKCF.create();

            case "TLD": return TrackerTLD.create();

            case "MEDIANFLOW": return TrackerMedianFlow.create();

            case "MOSSE": return TrackerMOSSE.create();

            case "CSRT": return TrackerCSRT.create();

            default: return TrackerKCF.create();
        }
    }



}