package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.*;
import io.grpc.stub.StreamObserver;

/**
 * Created by jiahao on 5/1/17.
 */

public class Connection extends ConnectionServiceGrpc.ConnectionServiceImplBase {

    public Connection() {
    }

    @Override
    public void joinCluster(ComputingNode request, StreamObserver<JoinResponse> responseObserver) {
        System.out.println("Join request received from " + request.getHostname());
        System.out.println(request);

        ClusterServer.setComputingPower(ClusterServer.getComputingPower() + request.getPerformance());
        ClusterServer.getClientTimeout().put(request.getIpAddr(), 2100);
        boolean isBackup = false;

        synchronized (ClusterServer.getClusterBuilder()) {
            ClusterServer.getClusterBuilder().addNodeList(request);
            if (ClusterServer.getClusterBuilder().getNodeListCount() == 0) {
                isBackup = true;
            }
        }

        JoinResponse.Builder responseBuilder = JoinResponse.newBuilder();
        if (ClusterServer.getClusterBuilder().getNodeListCount() == 1) {
            responseBuilder.setNodeCount(ClusterServer.getClusterBuilder().getNodeListCount()).setIsBackup(true);
        } else {
            responseBuilder.setNodeCount(ClusterServer.getClusterBuilder().getNodeListCount()).setIsBackup(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void backupServer(BackupRequest request, StreamObserver<BackupResponse> responseObserver) {
        BackupResponse.Builder backupBuilder = BackupResponse.newBuilder();
        if (request.getClusterInfo()) {
            backupBuilder.setClusterInfo(ClusterServer.getClusterBuilder().build());
        }

        responseObserver.onNext(backupBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void alive(Ping request, StreamObserver<Pong> responseObserver) {
        String ip = request.getIp();
        synchronized (ClusterServer.getClientTimeout()) {
            ClusterServer.getClientTimeout().put(ip, 2100);
        }
        Pong.Builder response = Pong.newBuilder();
        response.setFlag(true);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
