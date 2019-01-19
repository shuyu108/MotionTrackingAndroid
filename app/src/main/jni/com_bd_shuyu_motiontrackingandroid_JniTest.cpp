#include "com_bd_shuyu_motiontrackingandroid_JniTest.h"

JNIEXPORT jstring JNICALL Java_com_bd_shuyu_motiontrackingandroid_JniTest_getMessageFromJNI
  (JNIEnv *env, jclass obj){
    return env->NewStringUTF("This is a message from JniTest");
  }