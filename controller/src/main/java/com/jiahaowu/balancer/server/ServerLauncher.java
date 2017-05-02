package com.jiahaowu.balancer.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Created by jiahao on 5/1/17.
 *
 */

public class ServerLauncher {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        ClusterServer cserv = new ClusterServer(SERVER_PORT);
        cserv.start();
    }
}
