#include "com_jiahaowu_balancer_task_TaskPi.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <omp.h>
#include <limits.h>

#include <unistd.h>
#include <time.h>
#include <sys/time.h>

JNIEXPORT jint JNICALL Java_com_jiahaowu_balancer_task_TaskPi_Monte(
    JNIEnv *env, jobject obj, jint NUM_RAND, jint NUM_REPEAT) {

    int my_cpu_id, numthreads;

#ifdef _OPENMP
    numthreads = omp_get_max_threads();
#else
    numthreads = 1;
#endif

    int i, j;
    int randInts_A[1], randInts_B[1];
    int count[numthreads];
    int count_total = 0;
    double radius;
    double rand_double_A, rand_double_B;
    double pi_result;
    int truerand;

    /* Measure runtime */
    double totaltime = 0;
    struct timeval tv1, tv2;
    gettimeofday(&tv1, NULL);

    for (i = 0; i < numthreads; i++) {
        count[i] = 0;
    }

    for (j = 0; j < NUM_REPEAT; j++) {
#pragma omp parallel private(i, my_cpu_id, randInts_A, randInts_B, radius,     \
                             rand_double_A, rand_double_B)
        {
#ifdef _OPENMP
            my_cpu_id = omp_get_thread_num();
            // generate random seed
            truerand = rdtsc();
            srandom((int)(time(NULL)) ^ my_cpu_id + truerand);
#else
            my_cpu_id = 0;
#endif

#pragma omp for
            for (i = 1; i < NUM_RAND; i++) {

                /* One way to get random integers -- full range */
                /*if( !(RAND_bytes((unsigned char
                *)randInts_A,sizeof(randInts_A)))) { printf("ERROR: call to
                RAND_pseudo_bytes() failed\n"); exit(1);
                }
                if( !(RAND_bytes((unsigned char
                *)randInts_B,sizeof(randInts_B)))) { printf("ERROR: call to
                RAND_pseudo_bytes() failed\n"); exit(1);
                }*/

                randInts_A[0] = random();
                randInts_B[0] = random();
                rand_double_A = randInts_A[0] / (double)INT_MAX;
                rand_double_B = randInts_B[0] / (double)INT_MAX;

                radius = rand_double_A * rand_double_A +
                         rand_double_B * rand_double_B;
                if (radius <= 1)
                    count[my_cpu_id]++;
                // printf("Random Integer: %d, Random double: %lf\n", randInt,
                // rand_double);
            }
        }
    }

    for (i = 0; i < numthreads; i++) {
        count_total += count[i];
    }

    return count_total;
}

unsigned long long rdtsc() {
    unsigned int lo, hi;
    __asm__ __volatile__("rdtsc" : "=a"(lo), "=d"(hi));
    return ((unsigned long long)hi << 32) | lo;
}
