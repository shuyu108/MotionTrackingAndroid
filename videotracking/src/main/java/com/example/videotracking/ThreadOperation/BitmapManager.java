package com.example.videotracking.ThreadOperation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

//UIThread
public class BitmapManager{

    static final int TASK_COMPLETE = 1;
    static final BitmapManager bmManager= new BitmapManager();
    final String TAG = "ObjectTrackerActivity";
    Handler mHandler;

    private BitmapManager(){

        // instantiate the handler as a global variable
        //Connect the handler to a UI thread (Looper)
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){

                BitmapTask bmTask =
                        (BitmapTask) msg.obj;

                ImageView imView = bmTask.getImgView();

                //"state" Info WORKS HERE:
                switch(msg.what){

                    case TASK_COMPLETE:
                        imView.setImageBitmap(bmTask.getBitmap());
                        //Log.d(TAG, "here");
                        break;

                    default:
                        /*
                         * Pass along other messages from the UI
                         */
                        super.handleMessage(msg);

                }

            }

        };
    }

    public static BitmapManager getInstance(){

        return bmManager;
    }

    public void handleState(BitmapTask bmTask, int state){

        switch(state){

            case TASK_COMPLETE:
                /*
                 * Creates a message for the Handler
                 * with the state and the task object
                 */
                Message completeMsg = mHandler.obtainMessage(state, bmTask);
                completeMsg.sendToTarget();
                //Log.d(TAG, "here33333");
                break;

        }
    }
}