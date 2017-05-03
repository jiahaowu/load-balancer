#include <stdio.h>
#include "JniSample.h"

JNIEXPORT jint JNICALL Java_JniSample_sayHello (JNIEnv *env, jobject obj, jint a) {
  printf("Hello World\n");
  
  return a;
}