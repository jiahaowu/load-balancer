#include "JniBench.h"

JNIEXPORT jdouble JNICALL Java_JniGo_Bench (JNIEnv *env, jobject obj, jint num_rand, jint num_repeat) {

    int my_cpu_id,numthreads;
    
    #ifdef _OPENMP
    numthreads=omp_get_max_threads();
    #else
    numthreads=1;
    #endif
    
    int i,j;
    int randInts_A[1], randInts_B[1];
    int count[numthreads];
    int count_total = 0;
    double radius;
    double rand_double_A, rand_double_B;
    double bench_score;
    int truerand;

    /* Measure runtime */
    double totaltime = 0;
    struct timeval  tv1, tv2;
    gettimeofday(&tv1, NULL);

    for (i=0; i<numthreads; i++) {
        count[i] = 0;
    }

    for (j=0; j<num_repeat; j++) {
      #pragma omp parallel private(i, my_cpu_id, randInts_A, randInts_B, radius, rand_double_A, rand_double_B)
      {
        #ifdef _OPENMP
        my_cpu_id=omp_get_thread_num();
        //generate random seed
        truerand = rdtsc();
        srandom((int)(time(NULL)) ^ my_cpu_id + truerand);
        #else
        my_cpu_id=0;
        #endif
        
        #pragma omp for
        for(i=1; i<num_rand; i++) {
        
        randInts_A[0] = random();
        randInts_B[0] = random();
        rand_double_A = randInts_A[0] / (double)INT_MAX;
        rand_double_B = randInts_B[0] / (double)INT_MAX;

        radius = rand_double_A * rand_double_A + rand_double_B *rand_double_B;
        if (radius <= 1) count[my_cpu_id]++;
        
        }
      }
    }
    
    for (i=0;i<numthreads;i++) {      
        count_total += count[i];
    }

    gettimeofday(&tv2, NULL);
    totaltime = (double) (tv2.tv_usec - tv1.tv_usec) / 1000000 +
            (double) (tv2.tv_sec - tv1.tv_sec);
    
    bench_score = 1/ totaltime * 10;
    
    return totaltime;
}

unsigned long long rdtsc(){
    unsigned int lo,hi;
    __asm__ __volatile__ ("rdtsc" : "=a" (lo), "=d" (hi));
    return ((unsigned long long)hi << 32) | lo;
}

