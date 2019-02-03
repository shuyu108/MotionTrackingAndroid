LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := openCVLibrary344
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	sdk\native\jni\include\opencv\*.hpp \
	sdk\native\jni\include\opencv2\*\*.hpp \
	sdk\native\jni\include\opencv2\*\*\*.hpp \
	sdk\native\jni\include\opencv2\*\*\*\*.hpp \
	sdk\native\jni\include\opencv2\*\*\*\*\*.hpp \

#LOCAL_C_INCLUDES += F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\debug\jni
#LOCAL_C_INCLUDES += F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\main\jni
#LOCAL_C_INCLUDES += F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\sdk\jni\include

include $(BUILD_SHARED_LIBRARY)