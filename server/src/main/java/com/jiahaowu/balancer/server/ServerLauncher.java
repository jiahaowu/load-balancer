package com.jiahaowu.balancer.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class ServerLauncher {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new JoinService())
                .build();

        server.start();
        System.out.println("Server started on localhost port: " + SERVER_PORT);
        server.awaitTermination();
    }
}
