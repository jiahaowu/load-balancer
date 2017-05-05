package com.jiahaowu.balancer.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jiahao on 5/1/17.
 *
 */

public class ClientLauncher {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8800;

    public static void main(String[] args) throws UnknownHostException {
        ClusterClient cc = new ClusterClient(HOST, PORT);
        cc.joinCluster();
        cc.shutdown();
    }
}
