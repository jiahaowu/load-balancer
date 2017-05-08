package com.jiahaowu.balancer.task;

/**
 * Created by jiahao on 5/4/17.
 */

// -Djava.library.path=/Users/jiahao/workspace/load-balancer/controller/build/jni/
public class TaskBench {
    static {
        System.loadLibrary("jnibench");
    }

    private native double Bench(int n);
    private native double Bench2(int n, int r);

    public double benchmark() {
        //return Bench(1500);
        return Bench2(10000000, 1);
    }
}
