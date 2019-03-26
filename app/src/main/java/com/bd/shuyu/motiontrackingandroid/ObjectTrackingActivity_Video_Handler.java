package com.bd.shuyu.motiontrackingandroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bd.shuyu.motiontrackingandroid.ThreadOpration.BitmapManager;
import com.bd.shuyu.motiontrackingandroid.ThreadOpration.BitmapTask;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerKCF;
import org.opencv.videoio.VideoCapture;

import java.util.Arrays;

import static android.os.Process.setThreadPriority;
import static org.opencv.imgproc.Imgproc.putText;

public class ObjectTrackingActivity_Video_Handler extends AppCompatActivity {

    public final static int DECODE_STATE_COMPLETED = 1;
    final String TAG = "ObjectTrackerActivity";
    Tracker tracker= TrackerKCF.create();
    //Tracker tracker = new Tracker("TLD");
    static{
        System.loadLibrary("MyLibs");
    }
    Mat frame = new Mat();
    ImageView videoView;
    Bitmap bm;
    VideoCapture video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = (ImageView) findViewById(R.id.video_view);

        videoTracking();
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.loadLibrary("MyLibs");
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "on Resume: OpenCV successfully loaded");
            //mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            Log.d(TAG, "on Resume: not loaded");
        }
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
    }

    void videoTracking() {

        video = new VideoCapture("/storage/emulated/0/data/song_Trim.avi");


        if (!video.isOpened()) {

            Log.d(TAG, "Video Loading Failed!!");
        }

        int x = 334, y = 13, height = 351, width = 200;
        Rect2d bbox = new Rect2d(x, y, width, height);
        Point pt1 = new Point(x, y);
        Point pt2 = new Point(x + width, y + height);
        // Display bounding box.
        video.read(frame);

        tracker.init(frame, bbox);

        bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);


        Scalar roiColor = new Scalar(255, 0, 0); //color

        Imgproc.rectangle(frame, pt1, pt2, roiColor, 2, 1);
        Utils.matToBitmap(frame, bm);
        Size sizeFrame= frame.size();
        Log.i(TAG, sizeFrame.toString());

        BitmapTask bmTask = new BitmapTask();
        bmTask.setVideoView(videoView);
        bmTask.setBitmap(bm);
        bmTask.handleDecodeState(DECODE_STATE_COMPLETED);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                Scalar roiColor = new Scalar(255, 0, 0); //color
                while(video.read(frame)){
                    Size sizeFrame= frame.size();
                    if(sizeFrame.area() < 10){
                        return;
                    }

                    Rect2d bbox = new Rect2d();
                    boolean ok = tracker.update(frame, bbox);

                    if(ok){

                        Point pt1 = new Point(bbox.x, bbox.y);
                        Point pt2 = new Point(bbox.x+bbox.width, bbox.y + bbox.height);
                        Imgproc.rectangle(frame, pt1, pt2, roiColor, 2, 1);
                    }else{
                        // Tracking failure detected.
                        Imgproc.putText(frame, "Tracking failure detected", new Point(100,80), 0, 0.75, new Scalar(0,0,255),2);
                    }

                    Utils.matToBitmap(frame, bm);

                    BitmapTask bmTask = new BitmapTask();
                    bmTask.setVideoView(videoView);
                    bmTask.setBitmap(bm);
                    bmTask.handleDecodeState(DECODE_STATE_COMPLETED);
                }
            }
        }).start();




    }

}
