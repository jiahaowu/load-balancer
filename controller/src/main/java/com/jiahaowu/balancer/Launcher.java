package com.jiahaowu.balancer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.jiahaowu.balancer.client.ClusterClient;
import com.jiahaowu.balancer.server.ClusterServer;

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
    private String state = STATS[2];

    @Parameter(names = {"-s", "--clusterServer"}, description = "IP / Domain of the ClusterServer Node / Coordinator")
    private String serverAddr = "127.0.0.1";

    @Parameter(names = {"-p", "--port"}, description = "Port number the clusterServer listens on")
    private Integer port = 8800;

    private ClusterClient clusterClient;
    private ClusterServer clusterServer;

    public static void main(String[] args) {
        Launcher main = new Launcher();
        JCommander jc = new JCommander(main, args);

        if (main.help) {
            jc.usage();
            return;
        }

        if (main.state.equals(STATS[1])) {
            // Launcher starts the clusterServer mode
            main.clusterServer = new ClusterServer(main.port);

        } else if (main.state.equals(STATS[2])) {
            // Launcher starts the clusterClient mode
            main.clusterClient = new ClusterClient(main.serverAddr, main.port);

        }
    }
}
