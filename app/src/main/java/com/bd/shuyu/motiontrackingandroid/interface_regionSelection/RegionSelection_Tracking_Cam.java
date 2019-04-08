package com.bd.shuyu.motiontrackingandroid.interface_regionSelection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bd.shuyu.motiontrackingandroid.CameraRegionSEL_Activity;
import com.bd.shuyu.motiontrackingandroid.CameraTrackingActivity;

public class RegionSelection_Tracking_Cam extends View {
    private Paint mRectPaint;
    private int mStartX = 0;
    private int mStartY = 0;
    private int mEndX = 0;
    private int mEndY = 0;
    private Rect mRect;
    private boolean mDrawRect = false;
    private TextPaint mTextPaint = null;

    private OnDownCallback mDownCallback = null;
    private OnMoveCallback mMoveCallback = null;
    private OnUpCallback mUpCallback = null;

    public interface OnDownCallback {

        void onRectStarted();
    }

    public interface OnMoveCallback{

        void onRectChanging(Rect rect);
    }


    public interface OnUpCallback {
        void onRectFinished(Rect rect);
    }

    public RegionSelection_Tracking_Cam(final Context context) {
        super(context);
        init();
    }

    public RegionSelection_Tracking_Cam(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegionSelection_Tracking_Cam(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnDownCallback(OnDownCallback callback) {
        mDownCallback = callback;
    }
    public void setOnMoveCallback(OnMoveCallback callback) {
        mMoveCallback = callback;
    }

    /**
     * Sets callback for up
     *
     * @param callback {@link RegionSelection.OnUpCallback}
     */
    public void setOnUpCallback(OnUpCallback callback) {
        mUpCallback = callback;
    }

    /**
     * Inits internal data
     */
    private void init() {
        mRectPaint = new Paint();
        mRectPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(5); // TODO: should take from resources

        mTextPaint = new TextPaint();
        mTextPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mTextPaint.setTextSize(20);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        // TODO: be aware of multi-touches
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawRect = false;
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();

                if (mDownCallback != null) {
                    mDownCallback.onRectStarted();
                }

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                final int x = (int) event.getX();
                final int y = (int) event.getY();

                if (!mDrawRect || Math.abs(x - mEndX) > 5 || Math.abs(y - mEndY) > 5) {
                    mEndX = x;
                    mEndY = y;


                    invalidate();
                    if (mMoveCallback != null) {
                        mMoveCallback.onRectChanging(new Rect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
                                Math.max(mEndX, mStartX), Math.max(mEndY, mStartY)));
                    }
                }

                mDrawRect = true;

                //CameraRegionSEL_Activity.setDrawingRect(new Rect(mStartX, mStartY, mEndX, mEndY));

                break;

            case MotionEvent.ACTION_UP:
                if (mUpCallback != null) {
                    mUpCallback.onRectFinished(new Rect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
                            Math.max(mEndX, mStartX), Math.max(mEndY, mStartX)));
                }
                invalidate();

                //CameraTrackingActivity.setDrawingRect(null);

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

                /*if (mDrawRect) {
                        canvas.drawRect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
                                Math.max(mEndX, mStartX), Math.max(mEndY, mStartY), mRectPaint);
                        canvas.drawText("  (" + Math.abs(mStartX - mEndX) + ", " + Math.abs(mStartY - mEndY) + ")",
                                Math.max(mEndX, mStartX), Math.max(mEndY, mStartY), mTextPaint);
                }*/
    }
}
