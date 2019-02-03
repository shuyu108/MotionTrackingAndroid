//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.optflow;



// C++: class PCAPrior
//javadoc: PCAPrior

public class PCAPrior {

    protected final long nativeObj;
    protected PCAPrior(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static PCAPrior __fromPtr__(long addr) { return new PCAPrior(addr); }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // native support for java finalize()
    private static native void delete(long nativeObj);

}
