#include <stdio.h>
#include "JniSample.h"

JNIEXPORT jdouble JNICALL Java_JniSample_sayHello (JNIEnv *env, jobject obj, jint a) {
  //printf("Hello World\n");
  jdouble result;
  result = 100.2;
  return result;
}