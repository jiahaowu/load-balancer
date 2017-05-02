package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.Cluster;
import com.jiahaowu.balancer.protocol.ComputingNode;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jiahao on 5/1/17.
 */
public class ClusterServer {
    private Integer serverPort;
    private Cluster.Builder clusterBuilder;

    public ClusterServer(Integer port) {
        serverPort = port;
        clusterBuilder = Cluster.newBuilder();
    }

    public void start() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(serverPort)
                .addService(new JoinService(clusterBuilder))
                .build();

        server.start();
        System.out.println("ClusterServer started on localhost port: " + serverPort);
        server.awaitTermination();
    }
}
