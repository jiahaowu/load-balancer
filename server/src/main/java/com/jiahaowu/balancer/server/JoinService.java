package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.server.comm.ConnectionServiceGrpc;
import com.jiahaowu.balancer.server.comm.JoinRequest;
import com.jiahaowu.balancer.server.comm.JoinResponse;
import io.grpc.stub.StreamObserver;

public class JoinService extends ConnectionServiceGrpc.ConnectionServiceImplBase {
    @Override
    public void joinCluster(JoinRequest request, StreamObserver<JoinResponse> responseObserver) {
        System.out.println("Join request received from " + request.getHostname());
        JoinResponse response = JoinResponse.newBuilder()
                .setId(0).setNodeCount(0).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
