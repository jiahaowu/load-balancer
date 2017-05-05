package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.Cluster;
import com.jiahaowu.balancer.protocol.ComputingNode;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiahao on 5/1/17.
 */
public class ClusterServer {
    private Integer serverPort;
    private static Cluster.Builder clusterBuilder;

    public static void setClusterBuilder(Cluster.Builder clusterBuilder) {
        ClusterServer.clusterBuilder = clusterBuilder;
    }

    public static void setClientTimeout(Map<String, Integer> clientTimeout) {
        ClusterServer.clientTimeout = clientTimeout;
    }

    public static void setComputingPower(Double computingPower) {
        ClusterServer.computingPower = computingPower;
    }

    private static Map<String, Integer> clientTimeout;

    public static Cluster.Builder getClusterBuilder() {
        return clusterBuilder;
    }

    public static Map<String, Integer> getClientTimeout() {
        return clientTimeout;
    }

    public static Double getComputingPower() {
        return computingPower;
    }

    private static Double computingPower;

    public ClusterServer(Integer port) {
        serverPort = port;
        clusterBuilder = Cluster.newBuilder();
        clientTimeout = new HashMap<>();
        computingPower = 0.0;
    }

    public void start() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(serverPort)
                .addService(new Connection())
                .build();

        server.start();
        System.out.println("ClusterServer started on localhost port: " + serverPort);
        long start = System.currentTimeMillis();
        while (true) {
            long end = System.currentTimeMillis();
            System.out.println("Started " + ((end - start) / 1000) + " seconds");
            System.out.println(getClientList());
            System.out.println("Computing Power: " + computingPower);

            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        server.awaitTermination();
    }

    private List<String> getClientList() {
        List<ComputingNode> list = clusterBuilder.getNodeListList();
        List<String> ips = new LinkedList<>();
        for (ComputingNode n : list) {
            ips.add(n.getIpAddr());
        }
        return ips;
    }
}
