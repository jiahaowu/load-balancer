package com.jiahaowu.balancer.client;

import com.jiahaowu.balancer.protocol.ConnectionServiceGrpc;
import com.jiahaowu.balancer.protocol.Ping;
import com.jiahaowu.balancer.protocol.Pong;

/**
 * Created by jiahao on 5/4/17.
 */
public class Liveness implements Runnable {
    private ConnectionServiceGrpc.ConnectionServiceBlockingStub stub;
    private String ip;

    public Liveness(ConnectionServiceGrpc.ConnectionServiceBlockingStub stub, String ip) {
        this.stub = stub;
        this.ip = ip;
    }

    @Override
    public void run() {
        while(!ClusterClient.isTerminated()) {
            Ping request = Ping.newBuilder().setIp(ip).setFlag(true).build();
            Pong response = stub.alive(request);

            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
