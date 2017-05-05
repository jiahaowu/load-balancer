package com.jiahaowu.balancer.client;

import com.jiahaowu.balancer.protocol.*;
import com.jiahaowu.balancer.task.TaskBench;
import com.jiahaowu.balancer.task.TaskPi;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

    public boolean isTerminated() {
        return isTerminated;
    }

    private boolean isTerminated = false;

    private ManagedChannel chan;
    private ConnectionServiceGrpc.ConnectionServiceBlockingStub stub;

    public ClusterClient(String serverAddr, Integer port) {
        this.serverAddr = serverAddr;
        this.serverPort = port;
        TaskBench bench = new TaskBench();
        this.performance = bench.benchmark();
        InetAddress IP;
        try {
            IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }
        this.localAddr = IP.getHostAddress();
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

    public void joinCluster() {
        ComputingNode request = ComputingNode.newBuilder()
                .setHostname(hostName)
                .setPerformance(performance)
                .setIpAddr(localAddr)
                .build();

        System.out.println("Sending request to join cluster");
        JoinResponse response = stub.joinCluster(request);

        if (response.getIsBackup()) {
            backupServer();
        }
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
        if(response.getDone()) {
            isTerminated = true;
            return;
        }
        if(response.getPause()) {
            return;
        }
        int result = taskPi.simulation(response.getNumRand(), response.getNumRepeat());
        CommitRequest commit = CommitRequest.newBuilder().setCount(result).build();
        CommitResponse commitResponse = stub.commitTask(commit);
    }
}
