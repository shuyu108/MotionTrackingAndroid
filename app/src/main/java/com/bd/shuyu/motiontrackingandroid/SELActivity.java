package com.bd.shuyu.motiontrackingandroid;
import android.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.bd.shuyu.motiontrackingandroid.R;
import com.bd.shuyu.motiontrackingandroid.interface_regionSelection.RegionSelection_Cam;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.graphics.Rect;

public class SELActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "MyActivity";

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons);

        final RegionSelection view = (RegionSelection) findViewById(R.id.dragRect);

        if (null != view) {
            view.setOnUpCallback(new RegionSelection.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }*/
    //private static final String TAG = "MainActivity";

    static{
        System.loadLibrary("MyLibs");
    }
    Mat mRgba, mGray;
    JavaCameraView javaCameraView;
    static Rect mRect;

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
        setContentView(R.layout.camera_sel);
        final RegionSelection_Cam view = (RegionSelection_Cam) findViewById(R.id.dragRect);

        if (null != view) {
            view.setOnUpCallback(new RegionSelection_Cam.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
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
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();


    //3 method for the listener

    @Override
    public void onCameraViewStarted(int w, int h){
        //we have 4 channels here
        mRgba = new Mat(h, w, CvType.CV_8UC4);


        //mGray = new Mat(h, w, CvType.CV_8UC1);


    }
    @Override
    public void onCameraViewStopped(){
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba = inputFrame.rgba();
        //int debugInt = OpencvNativeFaceDetection.faceDetection(mRgba.getNativeObjAddr());

        /*if(debugInt == 1){
            Log.d(TAG, "faceDetection(): xml loading failed");
        }*/

        if(mRect != null){
            Scalar roiColor = new Scalar(255, 0, 0);
            Imgproc.rectangle(mRgba, new Point(mRect.left, mRect.top), new Point(mRect.right, mRect.bottom), roiColor, 2, 2);
        }

        return mRgba;
    }

    public static void setDrawingRect(Rect rt){

        mRect = rt;
    }
}