package ca.uwaterloo.ece358.thunder.data;

import ca.uwaterloo.ece358.thunder.data.node.NetworkNode;
import ca.uwaterloo.ece358.thunder.data.node.PPersistentNetworkNode;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private final List<NetworkNode> nodes;
    private final NetworkBus networkBus;

    private final long ticks;
    private final long packetRate;
    private final int packetLength;
    private final int nodeCount;
    private final double persistence;

    public Simulator(long ticks, int nodeCount, long packetRate, int packetLength, double persistence) {
        this.nodes = new ArrayList<NetworkNode>();

        this.ticks = ticks;

        this.packetRate = packetRate;
        this.packetLength = packetLength * 8;

        this.nodeCount = nodeCount;
        this.persistence = persistence;

        this.networkBus = new NetworkBus();
    }

    public void run() {
        this.nodes.clear();

        for (int i = 0; i < nodeCount; i++) {
            this.nodes.add(generateNode(i, packetRate, persistence));
        }

        for (int i = 0; i < this.ticks; i++) {
            for (NetworkNode n : this.nodes) {
                n.process();
            }
            this.networkBus.cycle();
        }
    }

    public void printMetrics() {
        int totalRequestsCompleted = 0;
        long totalDelayTime = 0;

        for (NetworkNode n : nodes) {
            totalRequestsCompleted += n.getPacketsReceived();
            totalDelayTime += n.getDelay();
        }

        final double throughput = (double) totalRequestsCompleted / (double) ticks;
        final double averageDelay = (double) totalDelayTime / (double) totalRequestsCompleted;

        StringBuilder sb = new StringBuilder();
        sb.append(this.nodeCount);
        sb.append(",");
        sb.append(this.packetRate);
        sb.append(",");
        sb.append(this.persistence);
        sb.append(",");
        sb.append(totalRequestsCompleted);
        sb.append(",");
        sb.append(ticks);
        sb.append(",");
        sb.append(throughput);
        sb.append(",");
        sb.append(averageDelay);

        System.out.println(sb.toString());
    }

    private NetworkNode generateNode(int i, long lambda, double p) {
        final long propagationDelay = (long) ((((double) i * 10.0) / 2E8) * 1E6);

        if (p == 0.0 || p == 1.0) {
            return new NetworkNode(this.networkBus, propagationDelay, lambda, this.packetLength, p);
        } else {
            return new PPersistentNetworkNode(this.networkBus, propagationDelay, lambda, this.packetLength, p);
        }
    }
}
