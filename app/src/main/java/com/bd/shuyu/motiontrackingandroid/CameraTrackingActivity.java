package com.bd.shuyu.motiontrackingandroid;

import android.graphics.Rect;

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
import org.opencv.core.Rect2d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerKCF;

import com.bd.shuyu.motiontrackingandroid.OpencvNatives.OpencvNativeCls;
import com.bd.shuyu.motiontrackingandroid.interface_regionSelection.RegionSelection_Cam;
import com.bd.shuyu.motiontrackingandroid.interface_regionSelection.RegionSelection_Tracking_Cam;

public class CameraTrackingActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{


    final String TAG = "OpenCV_Camera";
    static{
        System.loadLibrary("MyLibs");
    }

    Tracker tracker = TrackerKCF.create();
    Mat mRgba, mRgb = new Mat();

    float scaleWIN2CAM_X, scaleWIN2CAM_Y;
    int biasWIN2CAM_X, biasWIN2CAM_Y;
    boolean isTrackerStarted = false;

    JavaCameraView javaCameraView;
    RegionSelection_Tracking_Cam camView;

    static Rect mRect, camRect;
    static Rect2d roiRect;
    static Scalar roiColor = new Scalar(255, 0, 0);

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

        //Create a Callback that invoked when releasing finger
        if (null != camView) {


            camView.setOnDownCallback(new RegionSelection_Tracking_Cam.OnDownCallback() {
                @Override
                public void onRectStarted() {

                    if(isTrackerStarted){

                        //RESET the State of the Tracker
                        tracker.clear();
                        isTrackerStarted = false;
                    }

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
                    tracker = TrackerKCF.create();
                    tracker.init(mRgb, roiRect);
                    isTrackerStarted = true;


                }
            });
        }


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
        mRgba = new Mat(h, w, CvType.CV_8UC3);


        //Try to scale the cameraview to fit the screen
        int screenH = camView.getHeight();
        int screenW = camView.getWidth();

        scaleWIN2CAM_X = (float) w / screenH /((float) w/h) ;
        scaleWIN2CAM_Y = (float) h/screenH;
        biasWIN2CAM_X = (int) - Math.floor((screenW - screenH * ((float) w/h)) / 2) ;
        //biasWIN2CAM_Y = (int) Math.floor((screenH - h) / 2);
        biasWIN2CAM_Y = 0;
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

        //rgba = cv2.cvtColor(rgb_data, cv2.COLOR_RGB2RGBA)

        if(mRect != null){
            Log.d(TAG, "onCameraFrame: Drawing");
            ///Rect camRect = cvtRectWIN2CAM(mRect);
            camRect = cvtRectWIN2CAM(mRect);

            //Imgproc.rectangle(mRgba, new Point(mRect.left, mRect.top), new Point(mRect.right, mRect.bottom), roiColor, 2, 2);
            Imgproc.rectangle(mRgba, new Point(camRect.left, camRect.top), new Point(camRect.right, camRect.bottom), roiColor, 2, 2);
        }

        if(isTrackerStarted){
            Imgproc.cvtColor(mRgba, mRgb, Imgproc.COLOR_RGBA2BGR);
            Log.d(TAG, "onCameraFrame: Tracker Started");
            boolean ok = tracker.update(mRgb, roiRect);
            if(ok){
                Imgproc.rectangle(mRgba, new Point(roiRect.x, roiRect.y),
                        new Point(roiRect.x + roiRect.width, roiRect.y + roiRect.height),
                        roiColor, 2, 2);
            }else{
                Imgproc.putText(mRgba, "Tracking failure detected", new Point(100,80), 0, 0.75, new Scalar(0,0,255),2);
            }


        }

        return mRgba;
    }

    public static void setDrawingRect(Rect rt){

        mRect = rt;
    }

    public Rect cvtRectWIN2CAM(Rect winRect){
        Log.d(TAG, "RectWIN2CAM: Converting");
        return new Rect( (int)Math.floor(scaleWIN2CAM_X * winRect.left + biasWIN2CAM_X),
                (int)Math.floor(scaleWIN2CAM_Y * winRect.top + biasWIN2CAM_Y),
                (int)Math.floor(scaleWIN2CAM_X * winRect.right + biasWIN2CAM_X),
                (int)Math.floor(scaleWIN2CAM_Y * winRect.bottom + biasWIN2CAM_Y));
    }

}
