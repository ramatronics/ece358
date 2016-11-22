package ca.uwaterloo.ece358.thunder;

import ca.uwaterloo.ece358.thunder.data.Simulator;

public class ThunderLab2 {
    private static final int NUM_TICKS = 100000000;
    private static final int PACKET_LENGTH = 1500;

    public static void main(String[] args) {
        int[] n = {5, 10, 15, 20, 40, 60, 80, 100};
        int[] lambdas = {6, 20};

        for (int i = 0; i < lambdas.length; i++) {
            for (int j = 0; j < n.length; j++) {
                Simulator s = new Simulator(NUM_TICKS, n[j], lambdas[i], PACKET_LENGTH, 0);
                s.run();
                s.printMetrics();
            }
        }

        System.out.println("=============================================");

        double[] p = {0.01, 0.1, 1};
        int[] lambdas2 = {1, 3, 5, 7, 10};
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < lambdas2.length; j++) {
                Simulator s = new Simulator(NUM_TICKS, 30, lambdas2[j], PACKET_LENGTH, p[i]);
                s.run();
                s.printMetrics();
            }
        }
    }
}
