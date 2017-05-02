package com.jiahaowu.balancer.client;

/**
 * Created by jiahao on 5/1/17.
 *
 */

public class ClientLauncher {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        ClusterClient cc = new ClusterClient(HOST, PORT);
        cc.joinCluster();
        cc.shutdown();
    }

}
