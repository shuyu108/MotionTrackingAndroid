package com.bd.shuyu.motiontrackingandroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.tracking.Tracker;
import org.opencv.videoio.VideoCapture;

public class VideoTrackingActivity  extends AppCompatActivity {

    private static final String TAG = "VideoTrackingActivity";

    static{
        System.loadLibrary("MyLibs");
    }

    ImageView videoView;

    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status){

            if (status == LoaderCallbackInterface.SUCCESS ) {

                videoView.setEnabled(true);

                videoTracking();


            } else {

                super.onManagerConnected(status);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        videoView = (ImageView) findViewById(R.id.video_view);


    }
    @Override
    public void onResume() {
        super.onResume();
        System.loadLibrary("MyLibs");

        if (OpenCVLoader.initDebug()) {

            Log.d(TAG, "on Resume: OpenCV successfully loaded");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {

            Log.d(TAG, "on Resume: not loaded");
        }
    }



    void videoTracking(){

        Mat frame = new Mat();

        VideoCapture video = new VideoCapture("/storage/emulated/0/data/song_Trim.avi");

        //Tracker tracker = new Tracker("TLD");

        if(!video.isOpened()){

            Log.d(TAG, "Video Loading Failed!!");
        }

        video.read(frame);

        Bitmap bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);


        Rect2d roiBox = new Rect2d(334, 13, 200, 333);
        Point roiPt1 = new Point(roiBox.x, roiBox.y);
        Point roiPt2 = new Point(roiBox.x + roiBox.width, roiBox.y + roiBox.height);
        Scalar roiColor = new Scalar( 255, 0, 0);

        Imgproc.rectangle(frame, roiPt1, roiPt2, roiColor,2, 1);
        Utils.matToBitmap(frame, bm);
        videoView.setImageBitmap(bm);
        while(video.read(frame)){

            double timer = (double) Core.getTickCount();

            //double timer




        }


    }



}
