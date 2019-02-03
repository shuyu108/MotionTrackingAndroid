LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := openCVLibrary344
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv\*.hpp \
	F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv2\*\*.hpp \
	F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv2\*\*\*.hpp \
	F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv2\*\*\*\*.hpp \
	F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv2\*\*\*\*\*.hpp \

#LOCAL_C_INCLUDES += F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\debug\jni
#LOCAL_C_INCLUDES += F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\main\jni
LOCAL_C_INCLUDES += F:\Android_Projects\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include

include $(BUILD_SHARED_LIBRARY)