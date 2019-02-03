//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.optflow;



// C++: class GPCPatchDescriptor
//javadoc: GPCPatchDescriptor

public class GPCPatchDescriptor {

    protected final long nativeObj;
    protected GPCPatchDescriptor(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static GPCPatchDescriptor __fromPtr__(long addr) { return new GPCPatchDescriptor(addr); }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // native support for java finalize()
    private static native void delete(long nativeObj);

}
