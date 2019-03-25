package com.bd.shuyu.motiontrackingandroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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
import org.opencv.imgproc.Imgproc;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerKCF;
import org.opencv.videoio.VideoCapture;

import java.util.Arrays;

public class ObjectTrackerActivity_Video extends AppCompatActivity {

    final String TAG = "ObjectTrackerActivity";
    Tracker tracker= TrackerKCF.create();
    //Tracker tracker = new Tracker("TLD");
    static{
        System.loadLibrary("MyLibs");
    }
    Mat frame;
    ImageView videoView;
    Bitmap bm;
    /*BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status){

            if (status == LoaderCallbackInterface.SUCCESS ) {

                videoView.setEnabled(true);




            } else {

                super.onManagerConnected(status);
            }

        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState){
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


    void videoTracking(){

        Mat frame = new Mat();

        VideoCapture video = new VideoCapture("/storage/emulated/0/data/song_Trim.avi");


        if(!video.isOpened()){

            Log.d(TAG, "Video Loading Failed!!");
        }

        int x=334,y=13,height=351,width=200;
        Rect2d bbox = new Rect2d( x , y , width , height );
        Point pt1=new Point(x,y);
        Point pt2=new Point(x+width,y+height);
        // Display bounding box.
        video.read(frame);

        //tracker.init(frame, bbox);

        bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);


        Scalar roiColor = new Scalar( 255, 0, 0); //color

        Imgproc.rectangle(frame, pt1, pt2, roiColor,2, 1);
        Utils.matToBitmap(frame, bm);
        videoView.setImageBitmap(bm);

        boolean ok = false;

        int w = bm.getWidth();
        int h = bm.getHeight();
        int[] pixels = new int[w*h];

        while(video.read(frame)){
            float timer = (float)Core.getTickCount();
            Utils.matToBitmap(frame, bm);
            //bm.getPixels(pixels, 0, w,0,0, w, h);
            //Log.i(TAG, Arrays.toString(pixels));

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    videoView.setImageBitmap(bm);
                }
            });

            /*runOnUiThread(new Runnable(){
                public void run(){
                    videoView.setImageBitmap(bm);

                }
            });*/
            try {
                //wait(10000);
                Thread.sleep(50);
            } catch (Exception e) {}

            Log.i(TAG, "loop");


            //ok = tracker.update(frame, bbox);
            /*Utils.matToBitmap(frame, bm);
            videoView.setImageBitmap(bm);*/
            /*if(ok){
                pt1 = new Point(bbox.x,bbox.y);
                pt2=new Point(bbox.x+bbox.width,bbox.y+bbox.height);

                Imgproc.rectangle(frame, pt1, pt2, roiColor,2, 1);
                Utils.matToBitmap(frame, bm);


                videoView.setImageBitmap(bm);


            }else{

                Log.i(TAG, "tracking failed");
                Utils.matToBitmap(frame, bm);
                videoView.setImageBitmap(bm);
            }
*/
        }








    }
}
