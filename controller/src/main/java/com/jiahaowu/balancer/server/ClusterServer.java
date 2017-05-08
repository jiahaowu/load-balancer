package com.jiahaowu.balancer.server;

import com.jiahaowu.balancer.protocol.Cluster;
import com.jiahaowu.balancer.protocol.ComputingNode;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.*;
import java.util.*;

/**
 * Created by jiahao on 5/1/17.
 */
public class ClusterServer {
    private static Cluster.Builder clusterBuilder;
    private static Double computingPower;
    private static Map<String, Integer> clientTimeout;
    private static Integer simulationNumber;
    private static Integer validCount;
    private static Integer processedTotal;
    private static Integer pendingNumber;
    private static Integer batchSize;
    public static Integer working = 0;

    public static void setInstrumentationStart(long instrumentationStart) {
        ClusterServer.instrumentationStart = instrumentationStart;
    }

    private static long instrumentationStart;
    private Integer serverPort;

    public ClusterServer(Integer port) {
        serverPort = port;
        clusterBuilder = Cluster.newBuilder();
        clientTimeout = new HashMap<>();
        computingPower = 0.0;
        batchSize = 100000;
        processedTotal = 0;
        validCount = 0;
    }

    public static Integer getProcessedTotal() {
        synchronized (processedTotal) {
            return processedTotal;
        }
    }

    public static void setProcessedTotal(Integer processedTotal) {
        synchronized (processedTotal) {
            ClusterServer.processedTotal = processedTotal;
        }
    }

    public static Integer getBatchSize() {
        return batchSize;
    }

    public static void setBatchSize(Integer batchSize) {
        ClusterServer.batchSize = batchSize;
    }

    public static Integer getPendingNumber() {
        synchronized (pendingNumber) {
            return pendingNumber;
        }
    }

    public static void setPendingNumber(Integer pendingNumber) {
        synchronized (pendingNumber) {
            ClusterServer.pendingNumber = pendingNumber;
        }
    }

    public static Integer getValidCount() {
        synchronized (validCount) {
            return validCount;
        }
    }

    public static void setValidCount(Integer validCount) {
        synchronized (validCount) {
            ClusterServer.validCount = validCount;
        }
    }

    public static Integer getSimulationNumber() {
        return simulationNumber;
    }

    public void setSimulationNumber(Integer simulationNumber) {
        ClusterServer.simulationNumber = simulationNumber;
    }

    public static Cluster.Builder getClusterBuilder() {
        return clusterBuilder;
    }

    public static void setClusterBuilder(Cluster.Builder clusterBuilder) {
        ClusterServer.clusterBuilder = clusterBuilder;
    }

    public static Map<String, Integer> getClientTimeout() {
        synchronized (clientTimeout) {
            return clientTimeout;
        }
    }

    public static void setClientTimeout(Map<String, Integer> clientTimeout) {
        synchronized (clientTimeout) {
            ClusterServer.clientTimeout = clientTimeout;
        }
    }

    public static Double getComputingPower() {
        synchronized (computingPower) {
            return computingPower;
        }
    }

    public static void setComputingPower(Double computingPower) {
        synchronized (computingPower) {
            ClusterServer.computingPower = computingPower;
        }
    }

    public void start() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(serverPort)
                .addService(new Connection())
                .build();
        StringBuilder sb = new StringBuilder();
        pendingNumber = simulationNumber;

        server.start();
        System.out.println("ClusterServer started on localhost port: " + serverPort);
        long start = System.currentTimeMillis();
        while (true) {
            long end = System.currentTimeMillis();
            System.out.println("Time " + ((end - start) / 1000) + " seconds, Computing Power = " + (int) (100 * computingPower));
            System.out.println(getClientList());

            if (processedTotal != 0) {
                System.out.println("Current pi: " + (4 * (double) validCount) / (double) processedTotal);
            }


            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
            // write log file
            sb.setLength(0);
            sb.append(getSimulationNumber()).append('\n')
                    .append(getValidCount()).append('\n')
                    .append(getProcessedTotal()).append('\n');
            writeLog(sb.toString());

            for (String ip : clientTimeout.keySet()) {
                clientTimeout.put(ip, clientTimeout.get(ip) - 1000);
                if (clientTimeout.get(ip) < 0) {
                    removeClient(ip);
                }
            }
            if (0 == pendingNumber) {
                System.out.println("Computation Complete");
                break;
            }
        }
        while(working != 0) {
            Thread.sleep(1000);
            System.out.println("Waiting for worker complete");
        }
        if (processedTotal != 0) {
            System.out.println("==== DEBUG ====");
            System.out.println(ClusterServer.getValidCount());
            System.out.println(ClusterServer.getProcessedTotal());
            System.out.println("Result pi = " + (4 * (double) validCount) / (double) processedTotal);
            long end = System.currentTimeMillis();
            System.out.println("Runtime = " + (end - instrumentationStart)/1000.0 + " s");
        }
        sb.setLength(0);
        sb.append(-1).append('\n');
        writeLog(sb.toString());
        server.awaitTermination();
    }

    private void writeLog(String log) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("server.log"));
        writer.write(log);
        writer.close();

    }

    private void removeClient(String ip) {
        for (int i = 0; i < clusterBuilder.getNodeListCount(); ++i) {
            if (clusterBuilder.getNodeList(i).getIpAddr().equals(ip)) {
                computingPower -= clusterBuilder.getNodeList(i).getPerformance();
                clusterBuilder.removeNodeList(i);
                break;
            }
        }
    }

    private List<String> getClientList() {
        List<ComputingNode> list = clusterBuilder.getNodeListList();
        List<String> ips = new LinkedList<>();
        for (ComputingNode n : list) {
            ips.add(n.getIpAddr());
        }
        return ips;
    }

    public void checkSavedState() {
        try {
            Scanner scanner = new Scanner(new FileReader("server.log"));
            int state = -1;
            if (scanner.hasNext()) {
                state = scanner.nextInt();
            }
            if(state <= 0) {
                System.out.println("Starts from the beginning");
            } else {
                System.out.println("Total number = " + state);
                int processed = -1;
                int valid = -1;
                if (scanner.hasNext()) {
                    valid = scanner.nextInt();
                }
                if (scanner.hasNext()) {
                    processed = scanner.nextInt();
                }
                if(processed == -1 || valid == -1) {
                    System.out.println("Log file is NOT complete, starts from the beginning");
                } else {
                    setValidCount(valid);
                    setProcessedTotal(processed);
                    setPendingNumber(state - processed);
                    setSimulationNumber(state);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Log file not found, starts from the beginning");
        }

    }
}
