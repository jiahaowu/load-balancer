package com.jiahaowu.balancer.client;

import com.jiahaowu.balancer.protocol.ConnectionServiceGrpc;
import com.jiahaowu.balancer.protocol.JoinRequest;
import com.jiahaowu.balancer.protocol.JoinResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


/**
 * Created by jiahao on 5/1/17.
 *
 */

public class ClientLauncher {
    private static final String HOST = "192.168.1.240";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        ManagedChannel chan = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        ConnectionServiceGrpc.ConnectionServiceBlockingStub stub = ConnectionServiceGrpc.newBlockingStub(chan);

        JoinRequest request = JoinRequest.newBuilder()
                .setHostname("NODE01")
                .setPerformance(6.000)
                .build();

        for(int i = 0; i<2000; ++i) {
            System.out.println("Sending request to join cluster");
            JoinResponse response = stub.joinCluster(request);

            System.out.println("Obtaine cluster id = " + response.getId());
        }
        chan.shutdown();
    }

}
