LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT = C:\OpenCV-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=ON
OPENCV_LIB_TYPE:=SHARED
include $(OPENCVROOT)/sdk/native/jni/OpenCV.mk


LOCAL_SRC_FILES := src/main/jni/com_bd_shuyu_motiontrackingandroid_OpencvNativeCls.cpp \
   src/main/jni/com_bd_shuyu_motiontrackingandroid_OpencvNativeFaceDetection.cpp
LOCAL_LDFLAGS += -llog
LOCAL_MODULE := MyLibs

include $(BUILD_SHARED_LIBRARY)
