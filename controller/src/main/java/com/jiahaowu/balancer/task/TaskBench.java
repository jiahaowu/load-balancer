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

    public double benchmark() {
        return Bench(1500);
    }
}
