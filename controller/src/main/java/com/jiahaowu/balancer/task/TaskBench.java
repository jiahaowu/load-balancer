package com.jiahaowu.balancer.task;

import java.io.File;

/**
 * Created by jiahao on 5/4/17.
 */

// -Djava.library.path=/Users/jiahao/workspace/load-balancer/controller/build/jni/
public class TaskBench {
    static {
        System.loadLibrary("jnibench");
    }
    public native double Bench(int n);

    public static void main(String[] args) {
        double bench_result = new TaskBench().Bench(2000);
        System.out.println("Bench_result: "+bench_result);
    }

}
