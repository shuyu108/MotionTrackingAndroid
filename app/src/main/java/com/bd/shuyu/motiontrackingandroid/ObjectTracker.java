/*

package com.bd.shuyu.motiontrackingandroid;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.tracking.TrackerKCF;
import org.opencv.video.Video;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import java.util.Scanner;
import org.opencv.videoio.VideoCapture;
import org.opencv.tracking.Tracker;
import org.opencv.core.Point;
import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.putText;

import android.media.MediaMetadataRetriever;

public class ObjectTrackerActivity extends AppCompatActivity {

    Tracker tracker=TrackerKCF.create();

    // Read video
    VideoCapture video = new VideoCapture("song_Trim.avi");

    // Exit if video is not opened
    if(!video.isOpened())
    {
        System.out.print("Video not found" );
    }

    // Read first frame
    Mat frame;
    ImageView videoView;

    int x=122,y=9,height=351,width=202;

    // Define initial bounding box
    Rect2d bbox = new Rect2d( x , y , width , height );

    // Display bounding box.

    Point pt1=new Point(x,y);
    Point pt2=new Point(x+width,y+height);

    imshow(frame);
        tracker.init(frame, bbox);
        while(video.read(frame))
    {
        // Start timer
        float timer = (float)Core.getTickCount();

        // Update the tracking result
        boolean ok = tracker.update(frame, bbox);

        // Calculate Frames per second (FPS)
        float fps = (float)Core.getTickFrequency() / ((float)Core.getTickCount() - timer);

        if (ok)
        {
            // Track`ing success : Draw the tracked object
            rectangle(frame,pt1,pt2,new Scalar( 255, 0, 0 ), 5 );
        }
        else
        {
            // Tracking failure detected.
            putText(frame, "Tracking failure detected", new Point(100,80), 0, 0.75, new Scalar(0,0,255),2);
        }

        // Display frame.
        imshow(frame);

        // Exit if ESC pressed.
        int k = waitKey(1);
        if(k == 27)
        {
            break;
        }

    }


}

*/
