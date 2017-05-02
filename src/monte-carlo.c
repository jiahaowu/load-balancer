// Random Generator Reference
// https://www.mitchr.me/SS/exampleCode/random/opensslPRandEx.c.html


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

#define NUM_RAND 50000
#define NUM_REPEAT 100

#ifndef PI
#  define PI 3.141592653589793238
#endif

int main(int argc, char **argv)
{

    int my_cpu_id,numthreads;
    
    #ifdef _OPENMP
    numthreads=omp_get_max_threads();
    #else
    numthreads=1;
    #endif
    
    int i,j;
    int randInts_A[NUM_RAND], randInts_B[NUM_RAND];
    int count[numthreads];
    int count_total = 0;
    double radius[NUM_RAND];
    double rand_double_A[NUM_RAND], rand_double_B[NUM_RAND];
    double pi_result;

    /* Measure runtime */
    double totaltime = 0;
    struct timeval  tv1, tv2;
    gettimeofday(&tv1, NULL);

    printf("numthreads: %d\n",numthreads);
    for (i=0; i<numthreads; i++) {
        count[i] = 0;
    }

    for (j=0; j<NUM_REPEAT; j++) {
    /* One way to get random integers -- full range */
    if( !(RAND_pseudo_bytes((unsigned char *)randInts_A, sizeof(randInts_A)))) {
    printf("ERROR: call to RAND_pseudo_bytes() failed\n");
    exit(1);
    }

    if( !(RAND_pseudo_bytes((unsigned char *)randInts_B, sizeof(randInts_B)))) {
    printf("ERROR: call to RAND_pseudo_bytes() failed\n");
    exit(1);
    }

    /* Print out our random integers.  Note we abs() them to fold into non-negative integers.  One might also wish to exclude 0 from
    the stream for obvious reasons */

      #pragma omp parallel private(i, my_cpu_id)
      {
        #ifdef _OPENMP
        my_cpu_id=omp_get_thread_num();
        #else
        my_cpu_id=0;
        #endif
        
        #pragma omp for
        for(i=1; i<NUM_RAND; i++) {
    
        rand_double_A[i] = abs(randInts_A[i]) / (double)INT_MAX;
        rand_double_B[i] = abs(randInts_B[i]) / (double)INT_MAX;

        radius[i] = rand_double_A[i]*rand_double_A[i] + rand_double_B[i]*rand_double_B[i];
        if (radius[i] < 1) count[my_cpu_id]++;
        //printf("Random Integer: %d, Random double: %lf\n", randInt, rand_double);
        }
      }
    }
    
    for (i=0;i<numthreads;i++) {      
        //printf("count[%d]: %d\n",i,count[i]);
        count_total += count[i];
    }

    gettimeofday(&tv2, NULL);
    totaltime = (double) (tv2.tv_usec - tv1.tv_usec) / 1000000 +
            (double) (tv2.tv_sec - tv1.tv_sec);
    printf("average runtime is %lf\n",totaltime);

    pi_result = count_total / (double) NUM_RAND * 4 / NUM_REPEAT;
    printf("calculated PI: %lf\n", pi_result);

return 0;
}