package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

/**
 * Created by jiahao on 5/1/17.
 *
 */

public class JoinService extends ConnectionServiceGrpc.ConnectionServiceImplBase {
    private Cluster.Builder clusterBuilder;

    public JoinService(Cluster.Builder clusterBuilder) {
        this.clusterBuilder = clusterBuilder;
    }

    @Override
    public void joinCluster(ComputingNode request, StreamObserver<JoinResponse> responseObserver) {
        System.out.println("Join request received from " + request.getHostname());
        // retrieve node info

        clusterBuilder.addNodeList(request);

        JoinResponse response = JoinResponse.newBuilder()
                .setNodeCount(clusterBuilder.getNodeListCount()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
