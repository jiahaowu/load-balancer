package com.jiahaowu.balancer.client;

import com.jiahaowu.balancer.protocol.*;
import com.jiahaowu.balancer.task.TaskBench;
import com.jiahaowu.balancer.task.TaskPi;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by jiahao on 5/1/17.
 */
public class ClusterClient {
    private String serverAddr;
    private String localAddr;
    private Integer serverPort;
    private String hostName;
    private Double performance;
    private Cluster cluster;
    private TaskPi taskPi;
    private static boolean isTerminated = false;
    private ManagedChannel chan;
    private ConnectionServiceGrpc.ConnectionServiceBlockingStub stub;

    public ClusterClient(String serverAddr, Integer port) {
        this.serverAddr = serverAddr;
        this.serverPort = port;
        TaskBench bench = new TaskBench();
        this.performance = bench.benchmark();
        String ip = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    //System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        System.out.println("IP = "+ip);
        this.localAddr = ip; //IP.getHostAddress();

        try {
            this.hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        chan = ManagedChannelBuilder.forAddress(serverAddr, serverPort)
                .usePlaintext(true)
                .build();
        stub = ConnectionServiceGrpc.newBlockingStub(chan);
        taskPi = new TaskPi();
    }

    public static boolean isTerminated() {
        return isTerminated;
    }

    public void start() {
        joinCluster();

        Liveness live = new Liveness(stub, localAddr);
        Thread t = new Thread(live, "liveness");
        t.start();

        while (!isTerminated()) {
            processBlock();
        }
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void joinCluster() {
        ComputingNode request = ComputingNode.newBuilder()
                .setHostname(hostName)
                .setPerformance(performance)
                .setIpAddr(localAddr)
                .build();

        System.out.println("Sending request to join cluster");
        JoinResponse response = stub.joinCluster(request);

//        if (response.getIsBackup()) {
//            backupServer();
//        }
    }

    private void backupServer() {
        BackupRequest request = BackupRequest.newBuilder()
                .setClusterInfo(true).build();

        System.out.println("Starting backup server request");
        BackupResponse response = stub.backupServer(request);
        cluster = response.getClusterInfo();
    }

    public boolean isBackupServer() {
        return cluster != null;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void shutdown() {
        chan.shutdown();
    }

    public void processBlock() {
        TaskRequest request = TaskRequest.newBuilder().setPower(performance).build();
        TaskResponse response = stub.requestTask(request);
        if (response.getDone()) {
            isTerminated = true;
            return;
        }
        if (response.getPause()) {
            return;
        }
        int result = taskPi.simulation(response.getNumRand(), response.getNumRepeat());
        //int result = (int)(response.getNumRand() * 3.14);
        if(response.getNumRand() != 0) {
            System.out.println("Current Batch: " + (4 * (double) result) / (double) response.getNumRand());
        }
        CommitRequest commit = CommitRequest.newBuilder().setCount(result).setTotal(response.getNumRand()).build();
        CommitResponse commitResponse = stub.commitTask(commit);
    }
}
