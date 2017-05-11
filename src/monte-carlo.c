// Random Generator: C-program for MT19937-64
// http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/VERSIONS/C-LANG/mt19937-64.c

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <omp.h>
#include <limits.h>

#include <unistd.h>
#include <time.h>
#include <sys/time.h>
//#include <openssl/rand.h>

#define NUM_RAND 1000000000
#define NUM_REPEAT 1

unsigned long long rdtsc();

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
    int truerandseed1, truerandseed2;

    /*truerandseed1 = rdtsc();
	truerandseed2 = rdtsc();*/
    /* Measure runtime */
    double totaltime = 0;
    struct timeval  tv1, tv2;
    gettimeofday(&tv1, NULL);

    for (i=0; i<numthreads; i++) {
        count[i] = 0;
    }

	for (j=0; j<NUM_REPEAT; j++) {
      #pragma omp parallel private(i, my_cpu_id, radius, truerandseed1, truerandseed2, randInts_A, randInts_B, rand_double_A, rand_double_B)
      {
        #ifdef _OPENMP
        my_cpu_id=omp_get_thread_num();
		
        truerandseed1 = rdtsc();
		truerandseed2 = rdtsc();
        //generate random seed
		//if (j%1000 == 0) {
            //truerand = rdtsc();
            // init_genrand64(truerand * (my_cpu_id+5));
            //srandom(truerand * (my_cpu_id+5));
			
        //}
        #else
        my_cpu_id=0;
        #endif
        
        #pragma omp for
        for(i=1; i<NUM_RAND; i++) {
        randInts_A[0] = (double) rand_r(&truerandseed1);
        randInts_B[0] = (double) rand_r(&truerandseed2);
        rand_double_A = randInts_A[0] / (double)RAND_MAX;
        rand_double_B = randInts_B[0] / (double)RAND_MAX;


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

    pi_result = (double) count_total /(double) NUM_RAND * 4 / NUM_REPEAT;
    printf("calculated PI: %lf\n", pi_result);

return 0;
}

unsigned long long rdtsc(){
    unsigned int lo,hi;
    __asm__ __volatile__ ("rdtsc" : "=a" (lo), "=d" (hi));
    return ((unsigned long long)hi << 32) | lo;
}
