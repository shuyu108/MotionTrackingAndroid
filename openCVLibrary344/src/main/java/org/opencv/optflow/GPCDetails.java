//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.optflow;



// C++: class GPCDetails
//javadoc: GPCDetails

public class GPCDetails {

    protected final long nativeObj;
    protected GPCDetails(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static GPCDetails __fromPtr__(long addr) { return new GPCDetails(addr); }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // native support for java finalize()
    private static native void delete(long nativeObj);

}
