#include "JniSample.h"

JNIEXPORT jint JNICALL Java_JniGo_Monte (JNIEnv *env, jobject obj, jint num_rand, jint num_repeat) {

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
    double pi_result;
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
        if (j%1000 == 0) {
            truerand = rdtsc();
            // init_genrand64(truerand * (my_cpu_id+5));
            srandom(truerand * (my_cpu_id+5));
        }
        #else
        my_cpu_id=0;
        #endif
        
        #pragma omp for
        for(i=1; i<num_rand; i++) {
        
        randInts_A[0] = (double) random();
        randInts_B[0] = (double) random();
        rand_double_A = randInts_A[0] / (double)INT_MAX;
        rand_double_B = randInts_B[0] / (double)INT_MAX;


        radius = rand_double_A * rand_double_A + rand_double_B *rand_double_B;
        if (radius <= 1) count[my_cpu_id]++;
        //printf("Random Integer: %d, Random double: %lf\n", randInt, rand_double);
        }
      }
    }
    
    for (i=0;i<numthreads;i++) {      
        count_total += count[i];
    }

  return count_total;
}

unsigned long long rdtsc(){
    unsigned int lo,hi;
    __asm__ __volatile__ ("rdtsc" : "=a" (lo), "=d" (hi));
    return ((unsigned long long)hi << 32) | lo;
}

