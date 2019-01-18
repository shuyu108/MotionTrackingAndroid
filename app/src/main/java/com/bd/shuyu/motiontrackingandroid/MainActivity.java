package com.bd.shuyu.motiontrackingandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "MainActivity";
    static {
        System.loadLibrary("opencv_java3");
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV successfully loaded");
        }
        else{
            Log.d(TAG, "not loaded");
        }
    }

    Mat imgRgba;
    Mat imgGray;
    Mat imgCanny;
    Mat imgHSV;
    JavaCameraView javaCameraView;

    /*
    Dedicated for the onResume method
     */
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
    // Used to load the 'native-lib' library on application startup.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
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
        System.loadLibrary("opencv_java3");
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "on Resume: OpenCV successfully loaded");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            Log.d(TAG, "on Resume: not loaded");
        }
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
    */
    //public native String stringFromJNI();


    //3 method for the listener
    @Override
    public void onCameraViewStarted(int w, int h){
        //we have 4 channels here
        imgRgba = new Mat(h, w, CvType.CV_8UC4);
        imgGray = new Mat(h, w, CvType.CV_8UC1);
        imgCanny = new Mat(h, w, CvType.CV_8UC1);
        imgHSV = new Mat(h, w, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped(){
        imgRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        imgRgba = inputFrame.rgba();
        //Imgproc.cvtColor(imgRgba, imgGray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(imgGray, imgCanny, 50, 150);
        Imgproc.cvtColor(imgRgba, imgHSV, Imgproc.COLOR_RGB2HSV);

        return imgHSV;

        /*Mat rgbaT = imgRgba.t();
        Core.flip(imgRgba.t(), rgbaT, 1);
        Imgproc.resize(rgbaT, rgbaT, imgRgba.size());
        return rgbaT;*/

    }
}
