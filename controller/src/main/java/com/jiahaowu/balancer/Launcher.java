package com.jiahaowu.balancer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.jiahaowu.balancer.client.ClusterClient;
import com.jiahaowu.balancer.server.ClusterServer;

import java.io.IOException;

/**
 * Created by jiahao on 4/28/17.
 * <p>
 * Entry point for load balance manager
 */
public class Launcher {
    private static final String[] STATS = {"SERVER", "CLIENT", "BACKUP"};

    @Parameter(names = {"-h", "--help"}, help = true, description = "Display help information")
    private boolean help = false;

    @Parameter(names = {"-m", "--mode"}, description = "Mode of this controller instance")
    private String state = STATS[1];

    @Parameter(names = {"-s", "--clusterServer"}, description = "IP / Domain of the ClusterServer Node / Coordinator")
    private String serverAddr = "127.0.0.1";

    @Parameter(names = {"-p", "--port"}, description = "Port number the clusterServer listens on")
    private Integer port = 8800;

    @Parameter(names = {"-n", "--num"}, description = "Number of Simulations")
    private Integer simulationNum = 1000000;

    @Parameter(names = {"-e","--performance"}, description = "Override client performance parameter")
    private Double performance = -1.0;

    @Parameter(names = {"-b", "--batch"}, description = "Set batch size")
    private Integer batchSize = -1;

    private ClusterClient clusterClient;
    private ClusterServer clusterServer;

    public static void main(String[] args) {
        Launcher main = new Launcher();
        JCommander jc = new JCommander(main, args);

        if (main.help) {
            jc.usage();
            return;
        }

        if (main.state.equals(STATS[0])) {
            // Launcher starts the clusterServer mode
            main.clusterServer = new ClusterServer(main.port);
            main.clusterServer.setSimulationNumber(main.simulationNum);
            main.clusterServer.checkSavedState();
            if(main.batchSize != -1) {
                ClusterServer.setBatchSize(main.batchSize);
            }

            try {
                main.clusterServer.start();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        } else if (main.state.equals(STATS[1])) {
            // Launcher starts the clusterClient mode
            if(main.performance < 0) {
                main.clusterClient = new ClusterClient(main.serverAddr, main.port);
            } else {
                main.clusterClient = new ClusterClient(main.serverAddr, main.port, main.performance);
            }
            main.clusterClient.start();
            main.clusterClient.shutdown();
        }
    }
}
