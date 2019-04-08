package com.bd.shuyu.motiontrackingandroid.ThreadOperation;

import android.graphics.Bitmap;
import android.widget.ImageView;

//Class implementing the message object for BitmapManager;
public class BitmapTask{

    private Bitmap taskBm;
    private BitmapManager bmManager = BitmapManager.getInstance();
    private ImageView videoView;

    public void setVideoView(ImageView iv){
        videoView = iv;
    }

    public void setBitmap(Bitmap newBm){
        taskBm = newBm.copy(newBm.getConfig(), true);
    }

    public Bitmap getBitmap(){
        return taskBm;
    }

    public ImageView getImgView(){
        return videoView;
    }

    public void handleDecodeState(int state) {
        //Log.i(TAG, "here222");
        bmManager.handleState(this, state);
    }
}