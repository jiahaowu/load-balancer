#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <omp.h>

#include <unistd.h>
#include <time.h>
#include <sys/time.h>
#include <openssl/rand.h>

#define AMAX_NAME  128
#define CHUNK 128

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
    int i, randInt;
    int randInts[10];

    /* One way to get random integers -- full range */
    if( !(RAND_pseudo_bytes((unsigned char *)randInts, sizeof(randInts)))) {
    printf("ERROR: call to RAND_pseudo_bytes() failed\n");
    exit(1);
    }


    /* Print out our random integers.  Note we abs() them to fold into non-negative integers.  One might also wish to exclude 0 from
    the stream for obvious reasons */
    for(i=1; i<10; i++) {
    randInt = abs(randInts[i]);
    printf("Random Integer: %d\n", randInt);
    }

    #ifdef _OPENMP
    numthreads=omp_get_max_threads();
    #else
    numthreads=1;
    #endif

    printf("numthreads = %d\n", numthreads);


    printf("rand:%d\n", rand());

return 0;
}