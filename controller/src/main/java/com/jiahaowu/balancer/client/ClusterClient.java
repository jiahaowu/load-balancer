package com.jiahaowu.balancer.client;

import com.jiahaowu.balancer.protocol.ComputingNode;
import com.jiahaowu.balancer.protocol.ConnectionServiceGrpc;
import com.jiahaowu.balancer.protocol.JoinResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jiahao on 5/1/17.
 */
public class ClusterClient {
    private String serverAddr;
    private String localAddr;
    private Integer serverPort;
    private String hostName;
    private Double performance;

    private ManagedChannel chan;
    private ConnectionServiceGrpc.ConnectionServiceBlockingStub stub;

    public ClusterClient(String serverAddr, Integer port, Double performance) {
        this.serverAddr = serverAddr;
        this.serverPort = port;
        this.performance = performance;
        InetAddress IP;
        try {
            IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }
        this.localAddr = IP.getHostAddress();
        try {
            this.hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        chan = ManagedChannelBuilder.forAddress(serverAddr, serverPort)
                .usePlaintext(true)
                .build();
        stub = ConnectionServiceGrpc.newBlockingStub(chan);
    }

    public void joinCluster() {
        ComputingNode request = ComputingNode.newBuilder()
                .setHostname(hostName)
                .setPerformance(performance)
                .setIpAddr(localAddr)
                .build();

        System.out.println("Sending request to join cluster");
        JoinResponse response = stub.joinCluster(request);
    }

    public void shutdown() {
        chan.shutdown();
    }
}
