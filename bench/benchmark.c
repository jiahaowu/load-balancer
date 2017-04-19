#include <stdlib.h>
#include <omp.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <sys/time.h>

double bench_thread(int length);

int main ( ) {
  int i;
  int length = 50000;
  double average_time;
  double bench_score;

  int numthreads=omp_get_max_threads();
  printf("available threads: %d\n", numthreads);

  average_time = bench_thread(length);
  bench_score = 1/average_time;
  
  /* all threads done */
  printf("Average time: %lf\n",average_time);
  printf("Bench Score: %lf\n", bench_score);

  return 0;
}

double bench_thread(int length)
{
  int i,j;
  int u;

  double average_time=0;
  struct timeval  tv1, tv2;
  
  gettimeofday(&tv1, NULL);
  #pragma omp parallel private(i,j)
  {
  #pragma omp for
  for (i = 0; i < length; i++)
    for (j = 0; j < 1024*300; j++)
    {
      u = 123 * 456;
    }
  }
  gettimeofday(&tv2, NULL);

  average_time = (double) (tv2.tv_usec - tv1.tv_usec) / 1000000 +
         (double) (tv2.tv_sec - tv1.tv_sec);
  
  return average_time;
}