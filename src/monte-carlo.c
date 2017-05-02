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

#define NUM_RAND 500000

#ifndef PI
#  define PI 3.141592653589793238
#endif

int my_cpu_id,numthreads;

  // double totaltime=0;
  // struct timeval  tv1, tv2;
  // gettimeofday(&tv1, NULL);

  //   gettimeofday(&tv2, NULL);
  //   totaltime = (double) (tv2.tv_usec - tv1.tv_usec) / 1000000 +
  //        (double) (tv2.tv_sec - tv1.tv_sec);
  //   printf("average runtime is %lf\n",totaltime);


int main(int argc, char **argv)
{
    int i, randInt_A, randInt_B;
    int randInts_A[NUM_RAND], randInts_B[NUM_RAND];
    int count = 0;
    double radius;
    double rand_double_A, rand_double_B;
    double pi_result;

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
    for(i=1; i<NUM_RAND; i++) {
    randInt_A = abs(randInts_A[i]);
    randInt_B = abs(randInts_B[i]);
    
    rand_double_A = randInt_A / (double)INT_MAX;
    rand_double_B = randInt_B / (double)INT_MAX;

    radius = rand_double_A*rand_double_A + rand_double_B*rand_double_B;
    if (radius < 1) count++;
    //printf("Random Integer: %d, Random double: %lf\n", randInt, rand_double);
    }

    pi_result = count / (double) NUM_RAND * 4;

    printf("calculated PI: %lf\n", pi_result);

    // #ifdef _OPENMP
    // numthreads=omp_get_max_threads();
    // #else
    // numthreads=1;
    // #endif

    // printf("numthreads = %d\n", numthreads);


    // printf("rand:%d\n", rand());

return 0;
}