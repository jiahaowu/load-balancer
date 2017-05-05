package com.jiahaowu.balancer.task;

/**
 * Created by jiahao on 5/4/17.
 */
public class TaskPi {

    static {
        System.loadLibrary("jnipi");
    }
    private native int Monte(int num_rand, int num_repeat);

    public int simulation(int numRand, int numRepeat) {
        return Monte(numRand, numRepeat);
    }
}
