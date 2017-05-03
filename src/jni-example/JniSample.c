#include "JniSample.h"

JNIEXPORT jint JNICALL Java_JniSample_sayHello (JNIEnv *env, jobject obj, jint num_rand, jint num_repeat) {

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

    /* Measure runtime */
    double totaltime = 0;
    struct timeval  tv1, tv2;
    gettimeofday(&tv1, NULL);

    // printf("numthreads: %d\n",numthreads);
    for (i=0; i<numthreads; i++) {
        count[i] = 0;
    }

	for (j=0; j<num_repeat; j++) {
      #pragma omp parallel private(my_cpu_id)
      {
        #ifdef _OPENMP
        my_cpu_id=omp_get_thread_num();
        #else
        my_cpu_id=0;
        #endif
        
        #pragma omp for
        for(i=1; i<num_rand; i++) {
 
		/* One way to get random integers -- full range */
		if( !(RAND_pseudo_bytes((unsigned char *)randInts_A,sizeof(randInts_A)))) {
		printf("ERROR: call to RAND_pseudo_bytes() failed\n");
		exit(1);
		}
		if( !(RAND_pseudo_bytes((unsigned char *)randInts_B, sizeof(randInts_B)))) {
		printf("ERROR: call to RAND_pseudo_bytes() failed\n");
		exit(1);
		}

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