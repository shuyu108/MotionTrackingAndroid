package com.bd.shuyu.motiontrackingandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerKCF;

import com.bd.shuyu.motiontrackingandroid.OpencvNatives.OpencvNativeCls;
import com.bd.shuyu.motiontrackingandroid.interface_regionSelection.RegionSelection_Cam;

public class CameraTrackingActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{


    final String TAG = "OpenCV_Camera";
    static{
        System.loadLibrary("MyLibs");
    }

    Tracker tracker = TrackerKCF.create();
    Mat mRgba, mGray = new Mat();

    float scaleWIN2CAM_X, scaleWIN2CAM_Y;
    int biasWIN2CAM_X, biasWIN2CAM_Y;

    JavaCameraView javaCameraView;
    RegionSelection_Cam camView;

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
        setContentView(R.layout.activity_camera_sel);
        camView = (RegionSelection_Cam) findViewById(R.id.dragRect);

        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        //tv.setText(stringFromJNI());
        Log.d(TAG, "on Create: done");
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView!=null) javaCameraView.disableView();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView!=null) javaCameraView.disableView();
    }

    /*
    Everytime the App resume, we need to call BaseLoaderCallback
     */
    @Override
    protected void onResume(){
        super.onResume();
        System.loadLibrary("MyLibs");
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "on Resume: OpenCV successfully loaded");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            Log.d(TAG, "on Resume: not loaded");
        }
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
    }

    @Override
    public void onCameraViewStarted(int w, int h){
        //we have 4 channels here
        mRgba = new Mat(h, w, CvType.CV_8UC4);


        //Try to scale the cameraview to fit the screen
        int screenH = camView.getHeight();
        int screenW = camView.getWidth();

        scaleWIN2CAM_X = w/screenW;
        scaleWIN2CAM_Y = h/screenH;
        biasWIN2CAM_X = (int) Math.floor((screenW - screenH * (w/h)) / 2);
        biasWIN2CAM_Y = (int) Math.floor((screenH - h) / 2);

        Log.i(TAG, "window height is:  " + Integer.toString(screenH) + "  " +
                Integer.toString(screenW) + "  "+ Integer.toString(w) + "  "+ Integer.toString(h));

    }

    @Override
    public void onCameraViewStopped(){
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        //EXAMPLE, DELETE if NOT NEEDED
        OpencvNativeCls.cvtGray(mRgba.getNativeObjAddr(), mGray.getNativeObjAddr());
        mRgba = mGray;


        return mRgba;
    }

}
