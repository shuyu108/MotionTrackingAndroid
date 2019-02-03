//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.optflow;

import org.opencv.video.DenseOpticalFlow;

// C++: class DISOpticalFlow
//javadoc: DISOpticalFlow

public class DISOpticalFlow extends DenseOpticalFlow {

    protected DISOpticalFlow(long addr) { super(addr); }

    // internal usage only
    public static DISOpticalFlow __fromPtr__(long addr) { return new DISOpticalFlow(addr); }

    public static final int
            PRESET_ULTRAFAST = 0,
            PRESET_FAST = 1,
            PRESET_MEDIUM = 2;


    //
    // C++:  bool cv::optflow::DISOpticalFlow::getUseMeanNormalization()
    //

    //javadoc: DISOpticalFlow::getUseMeanNormalization()
    public  boolean getUseMeanNormalization()
    {
        
        boolean retVal = getUseMeanNormalization_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::optflow::DISOpticalFlow::getUseSpatialPropagation()
    //

    //javadoc: DISOpticalFlow::getUseSpatialPropagation()
    public  boolean getUseSpatialPropagation()
    {
        
        boolean retVal = getUseSpatialPropagation_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::optflow::DISOpticalFlow::getVariationalRefinementAlpha()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementAlpha()
    public  float getVariationalRefinementAlpha()
    {
        
        float retVal = getVariationalRefinementAlpha_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::optflow::DISOpticalFlow::getVariationalRefinementDelta()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementDelta()
    public  float getVariationalRefinementDelta()
    {
        
        float retVal = getVariationalRefinementDelta_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::optflow::DISOpticalFlow::getVariationalRefinementGamma()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementGamma()
    public  float getVariationalRefinementGamma()
    {
        
        float retVal = getVariationalRefinementGamma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::optflow::DISOpticalFlow::getFinestScale()
    //

    //javadoc: DISOpticalFlow::getFinestScale()
    public  int getFinestScale()
    {
        
        int retVal = getFinestScale_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::optflow::DISOpticalFlow::getGradientDescentIterations()
    //

    //javadoc: DISOpticalFlow::getGradientDescentIterations()
    public  int getGradientDescentIterations()
    {
        
        int retVal = getGradientDescentIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::optflow::DISOpticalFlow::getPatchSize()
    //

    //javadoc: DISOpticalFlow::getPatchSize()
    public  int getPatchSize()
    {
        
        int retVal = getPatchSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::optflow::DISOpticalFlow::getPatchStride()
    //

    //javadoc: DISOpticalFlow::getPatchStride()
    public  int getPatchStride()
    {
        
        int retVal = getPatchStride_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::optflow::DISOpticalFlow::getVariationalRefinementIterations()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementIterations()
    public  int getVariationalRefinementIterations()
    {
        
        int retVal = getVariationalRefinementIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setFinestScale(int val)
    //

    //javadoc: DISOpticalFlow::setFinestScale(val)
    public  void setFinestScale(int val)
    {
        
        setFinestScale_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setGradientDescentIterations(int val)
    //

    //javadoc: DISOpticalFlow::setGradientDescentIterations(val)
    public  void setGradientDescentIterations(int val)
    {
        
        setGradientDescentIterations_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setPatchSize(int val)
    //

    //javadoc: DISOpticalFlow::setPatchSize(val)
    public  void setPatchSize(int val)
    {
        
        setPatchSize_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setPatchStride(int val)
    //

    //javadoc: DISOpticalFlow::setPatchStride(val)
    public  void setPatchStride(int val)
    {
        
        setPatchStride_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setUseMeanNormalization(bool val)
    //

    //javadoc: DISOpticalFlow::setUseMeanNormalization(val)
    public  void setUseMeanNormalization(boolean val)
    {
        
        setUseMeanNormalization_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setUseSpatialPropagation(bool val)
    //

    //javadoc: DISOpticalFlow::setUseSpatialPropagation(val)
    public  void setUseSpatialPropagation(boolean val)
    {
        
        setUseSpatialPropagation_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementAlpha(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementAlpha(val)
    public  void setVariationalRefinementAlpha(float val)
    {
        
        setVariationalRefinementAlpha_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementDelta(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementDelta(val)
    public  void setVariationalRefinementDelta(float val)
    {
        
        setVariationalRefinementDelta_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementGamma(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementGamma(val)
    public  void setVariationalRefinementGamma(float val)
    {
        
        setVariationalRefinementGamma_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementIterations(int val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementIterations(val)
    public  void setVariationalRefinementIterations(int val)
    {
        
        setVariationalRefinementIterations_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool cv::optflow::DISOpticalFlow::getUseMeanNormalization()
    private static native boolean getUseMeanNormalization_0(long nativeObj);

    // C++:  bool cv::optflow::DISOpticalFlow::getUseSpatialPropagation()
    private static native boolean getUseSpatialPropagation_0(long nativeObj);

    // C++:  float cv::optflow::DISOpticalFlow::getVariationalRefinementAlpha()
    private static native float getVariationalRefinementAlpha_0(long nativeObj);

    // C++:  float cv::optflow::DISOpticalFlow::getVariationalRefinementDelta()
    private static native float getVariationalRefinementDelta_0(long nativeObj);

    // C++:  float cv::optflow::DISOpticalFlow::getVariationalRefinementGamma()
    private static native float getVariationalRefinementGamma_0(long nativeObj);

    // C++:  int cv::optflow::DISOpticalFlow::getFinestScale()
    private static native int getFinestScale_0(long nativeObj);

    // C++:  int cv::optflow::DISOpticalFlow::getGradientDescentIterations()
    private static native int getGradientDescentIterations_0(long nativeObj);

    // C++:  int cv::optflow::DISOpticalFlow::getPatchSize()
    private static native int getPatchSize_0(long nativeObj);

    // C++:  int cv::optflow::DISOpticalFlow::getPatchStride()
    private static native int getPatchStride_0(long nativeObj);

    // C++:  int cv::optflow::DISOpticalFlow::getVariationalRefinementIterations()
    private static native int getVariationalRefinementIterations_0(long nativeObj);

    // C++:  void cv::optflow::DISOpticalFlow::setFinestScale(int val)
    private static native void setFinestScale_0(long nativeObj, int val);

    // C++:  void cv::optflow::DISOpticalFlow::setGradientDescentIterations(int val)
    private static native void setGradientDescentIterations_0(long nativeObj, int val);

    // C++:  void cv::optflow::DISOpticalFlow::setPatchSize(int val)
    private static native void setPatchSize_0(long nativeObj, int val);

    // C++:  void cv::optflow::DISOpticalFlow::setPatchStride(int val)
    private static native void setPatchStride_0(long nativeObj, int val);

    // C++:  void cv::optflow::DISOpticalFlow::setUseMeanNormalization(bool val)
    private static native void setUseMeanNormalization_0(long nativeObj, boolean val);

    // C++:  void cv::optflow::DISOpticalFlow::setUseSpatialPropagation(bool val)
    private static native void setUseSpatialPropagation_0(long nativeObj, boolean val);

    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementAlpha(float val)
    private static native void setVariationalRefinementAlpha_0(long nativeObj, float val);

    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementDelta(float val)
    private static native void setVariationalRefinementDelta_0(long nativeObj, float val);

    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementGamma(float val)
    private static native void setVariationalRefinementGamma_0(long nativeObj, float val);

    // C++:  void cv::optflow::DISOpticalFlow::setVariationalRefinementIterations(int val)
    private static native void setVariationalRefinementIterations_0(long nativeObj, int val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
