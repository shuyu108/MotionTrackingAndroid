LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_SRC_FILES := src/main/jni/com_bd_shuyu_motiontrackingandroid_JniTest.cpp
LOCAL_LDFLAGS += -llog
LOCAL_MODULE := MyLibs

include $(BUILD_SHARED_LIBRARY)