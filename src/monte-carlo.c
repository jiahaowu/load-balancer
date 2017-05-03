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

#define NUM_RAND 10000000
#define NUM_REPEAT 10

int main(int argc, char **argv)
{

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

	for (j=0; j<NUM_REPEAT; j++) {
      #pragma omp parallel private(my_cpu_id)
      {
        #ifdef _OPENMP
        my_cpu_id=omp_get_thread_num();
        #else
        my_cpu_id=0;
        #endif
        
        #pragma omp for
        for(i=1; i<NUM_RAND; i++) {
 
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

    gettimeofday(&tv2, NULL);
    totaltime = (double) (tv2.tv_usec - tv1.tv_usec) / 1000000 +
            (double) (tv2.tv_sec - tv1.tv_sec);
    printf("average runtime is %lf\n",totaltime);

    pi_result = count_total /(double) NUM_RAND * 4 / NUM_REPEAT;
    printf("calculated PI: %lf\n", pi_result);

return 0;
}
