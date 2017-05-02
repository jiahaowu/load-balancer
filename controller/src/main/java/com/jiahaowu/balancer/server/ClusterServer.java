package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.Cluster;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Created by jiahao on 5/1/17.
 */
public class ClusterServer {
    private Integer serverPort;
    private Cluster cluster;

    public ClusterServer(Integer port) {
        serverPort = port;
        cluster = Cluster.newBuilder().build();
    }

    public void start() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(serverPort)
                .addService(new JoinService())
                .build();

        server.start();
        System.out.println("ClusterServer started on localhost port: " + serverPort);
        server.awaitTermination();
    }
}
