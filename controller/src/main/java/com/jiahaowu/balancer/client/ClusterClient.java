package com.jiahaowu.balancer.client;

import com.jiahaowu.balancer.protocol.ConnectionServiceGrpc;
import com.jiahaowu.balancer.protocol.JoinRequest;
import com.jiahaowu.balancer.protocol.JoinResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Created by jiahao on 5/1/17.
 */
public class ClusterClient {
    private String serverAddr;
    private Integer serverPort;

    private ManagedChannel chan;
    private ConnectionServiceGrpc.ConnectionServiceBlockingStub stub;

    public ClusterClient(String serverAddr, Integer port) {
        this.serverAddr = serverAddr;
        this.serverPort = port;

        chan = ManagedChannelBuilder.forAddress(serverAddr, serverPort)
                .usePlaintext(true)
                .build();
        stub = ConnectionServiceGrpc.newBlockingStub(chan);
    }

    public void joinCluster() {
        JoinRequest request = JoinRequest.newBuilder()
                .setHostname("NODE01")
                .setPerformance(6.000)
                .build();

        for (int i = 0; i < 2; ++i) {
            System.out.println("Sending request to join cluster");
            JoinResponse response = stub.joinCluster(request);
            System.out.println("Obtaine cluster id = " + response.getId());
        }
    }

    public void shutdown() {
        chan.shutdown();
    }
}
