package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.*;
import io.grpc.stub.StreamObserver;

import java.util.Map;

/**
 * Created by jiahao on 5/1/17.
 */

public class Connection extends ConnectionServiceGrpc.ConnectionServiceImplBase {
    private Cluster.Builder clusterBuilder;
    private Map<String, Integer> clientTimeout;

    public Connection(Cluster.Builder clusterBuilder, Map<String, Integer> clientTimeout) {
        this.clusterBuilder = clusterBuilder;
        this.clientTimeout = clientTimeout;
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
        if (clusterBuilder.getNodeListCount() == 1) {
            responseBuilder.setNodeCount(clusterBuilder.getNodeListCount()).setIsBackup(true);
        } else {
            responseBuilder.setNodeCount(clusterBuilder.getNodeListCount()).setIsBackup(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void backupServer(BackupRequest request, StreamObserver<BackupResponse> responseObserver) {
        BackupResponse.Builder backupBuilder = BackupResponse.newBuilder();
        if (request.getClusterInfo()) {
            backupBuilder.setClusterInfo(clusterBuilder.build());
        }

        responseObserver.onNext(backupBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void alive(Ping request, StreamObserver<Pong> responseObserver) {
        String ip = request.getIp();
        synchronized (clientTimeout) {
            clientTimeout.put(ip, 2000);
        }
        Pong.Builder response = Pong.newBuilder();
        response.setFlag(true);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
