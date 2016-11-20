package ca.uwaterloo.ece358.thunder.data;

import ca.uwaterloo.ece358.thunder.data.node.NetworkNode;
import ca.uwaterloo.ece358.thunder.data.node.PPersistentNetworkNode;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private List<NetworkNode> nodes;
    private Matrix network;
    private int packetLength;
    private long ticks;
    private double p;
    private int N;
    private long lambda;

    public Simulator(long ticks, int N, long lambda, int packetLength, double p) {
        this.nodes = new ArrayList<NetworkNode>();
        this.N = N;
        this.lambda = lambda;
        this.packetLength = packetLength * 8;
        this.ticks = ticks;
        this.p = p;
        this.network = new Matrix();


    }

    public void run() {
        this.nodes.clear();

        for (int i = 0; i < N; i++) {
            this.nodes.add(generateNode(i, lambda, p));
        }

        for (int i = 0; i < this.ticks; i++) {
            for (NetworkNode n : this.nodes) {
                n.process();
            }
            this.network.reset();
        }
    }

    public void printMetrics(){
        int totalSent = 0;
        long delayTime = 0;
        for (NetworkNode n : nodes) {
            totalSent += n.getRequestsCompleted();
            delayTime += n.getDelayTime();
        }

        double throughput = (double)totalSent / (double)ticks;
        double averageDelay = (double)delayTime / (double)totalSent;

        StringBuilder sb = new StringBuilder();
        sb.append(this.N);
        sb.append(",");
        sb.append(this.lambda);
        sb.append(",");
        sb.append(this.p);
        sb.append(",");
        sb.append(totalSent);
        sb.append(",");
        sb.append(ticks);
        sb.append(",");
        sb.append(throughput);
        sb.append(",");
        sb.append(averageDelay);

        System.out.println(sb.toString());
    }

    private NetworkNode generateNode(int i, long lambda, double p) {
        if (p == 0) {
            return new NetworkNode(this.network, computePropogation(i), lambda, this.packetLength, p);
        } else {
            return new PPersistentNetworkNode(this.network, computePropogation(i), lambda, this.packetLength, p);
        }
    }

    private static long computePropogation(int index) {
        double distance = (double) index * 10.0;
        double delaySeconds = distance / 2E8;
        return (long) (delaySeconds * 1E6);
    }
}
