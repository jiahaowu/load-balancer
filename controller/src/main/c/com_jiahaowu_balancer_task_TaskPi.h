/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <omp.h>
#include <limits.h>

#include <unistd.h>
#include <time.h>
#include <sys/time.h>
#include <openssl/rand.h>
/* Header for class com_jiahaowu_balancer_task_TaskPi */

#ifndef _Included_com_jiahaowu_balancer_task_TaskPi
#define _Included_com_jiahaowu_balancer_task_TaskPi
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_jiahaowu_balancer_task_TaskPi
 * Method:    Monte
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_jiahaowu_balancer_task_TaskPi_Monte
  (JNIEnv *, jobject, jint, jint);

int isamax ( int n, float x[], int incx );
void matgen ( int lda, int n, float a[], float x[], float b[] );
void msaxpy ( int nr, int nc, float a[], int n, float x[], float y[] );
void msaxpy2 ( int nr, int nc, float a[], int n, float x[], float y[] );
int msgefa ( float a[], int lda, int n, int ipvt[] );
int msgefa2 ( float a[], int lda, int n, int ipvt[] );
void saxpy ( int n, float a, float x[], int incx, float y[], int incy );
float sdot ( int n, float x[], int incx, float y[], int incy );
int sgefa ( float a[], int lda, int n, int ipvt[] );
void sgesl ( float a[], int lda, int n, int ipvt[], float b[], int job );
void sscal ( int n, float a, float x[], int incx );
void sswap ( int n, float x[], int incx, float y[], int incy );
void timestamp ( void );


#ifdef __cplusplus
}
#endif
#endif