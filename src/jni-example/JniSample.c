#include <stdio.h>
#include "JniSample.h"

JNIEXPORT jint JNICALL Java_JniSample_sayHello (JNIEnv *env, jobject obj) {
  printf("Hello World\n");
  return 0;
}