package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.Cluster;
import com.jiahaowu.balancer.protocol.ComputingNode;
import com.jiahaowu.balancer.protocol.ConnectionServiceGrpc;
import com.jiahaowu.balancer.protocol.JoinResponse;
import io.grpc.stub.StreamObserver;

/**
 * Created by jiahao on 5/1/17.
 */

public class Connection extends ConnectionServiceGrpc.ConnectionServiceImplBase {
    private Cluster.Builder clusterBuilder;

    public Connection(Cluster.Builder clusterBuilder) {
        this.clusterBuilder = clusterBuilder;
    }

    @Override
    public void joinCluster(ComputingNode request, StreamObserver<JoinResponse> responseObserver) {
        System.out.println("Join request received from " + request.getHostname());
        System.out.println(request);

        boolean isBackup = false;

        synchronized (clusterBuilder) {
            clusterBuilder.addNodeList(request);
            if (clusterBuilder.getNodeListCount() == 0) {
                isBackup = true;
            }
        }


        JoinResponse.Builder responseBuilder = JoinResponse.newBuilder();
        if (clusterBuilder.getNodeListCount() == 0) {
            responseBuilder.setNodeCount(clusterBuilder.getNodeListCount()).setIsBackup(true);
        } else {
            responseBuilder.setNodeCount(clusterBuilder.getNodeListCount()).setIsBackup(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

}
