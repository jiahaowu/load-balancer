package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.ConnectionServiceGrpc;
import com.jiahaowu.balancer.protocol.JoinRequest;
import com.jiahaowu.balancer.protocol.JoinResponse;
import io.grpc.stub.StreamObserver;

/**
 * Created by jiahao on 5/1/17.
 *
 */

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
