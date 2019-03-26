LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT = ../../../openCVLibrary344/src/sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=ON
OPENCV_LIB_TYPE:=SHARED
include $(OPENCVROOT)/jni/OpenCV.mk


LOCAL_SRC_FILES := src/main/jni/com_bd_shuyu_motiontrackingandroid_OpencvNatives_OpencvNativeCls.cpp \
    src/main/jni/com_bd_shuyu_motiontrackingandroid_OpencvNatives_OpencvNativeFaceDetection.cpp
LOCAL_LDFLAGS += -llog
LOCAL_MODULE := MyLibs

include $(BUILD_SHARED_LIBRARY)