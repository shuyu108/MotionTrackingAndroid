LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := openCVLibrary344
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
    D:\capstone\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv\*.hpp \
    D:\capstone\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv2\*\*.hpp \
    D:\capstone\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include\opencv2\*.hpp \







LOCAL_C_INCLUDES += D:\capstone\MotionTrackingAndroid\openCVLibrary344\src\main\jni
LOCAL_C_INCLUDES += D:\capstone\MotionTrackingAndroid\openCVLibrary344\src\sdk\native\jni\include

include $(BUILD_SHARED_LIBRARY)
