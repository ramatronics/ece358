package ca.uwaterloo.ece358.thunder.session;

import java.util.ArrayList;
import java.util.List;

public class MD1KQueueSession extends MD1QueueSession {
    public MD1KQueueSession(int bufferSize, int ticks, int packetSize, double service) {
        super(bufferSize, ticks, packetSize, service);
    }

    protected double[] extractLambdas() {
        List<Double> lambdas = new ArrayList<Double>();
        for (double i = 0.5; i < 1.5; i += 0.1) {
            lambdas.add((i * processTime) / timeLength);
        }

        double[] rtnLambdas = new double[lambdas.size()];
        for (int i = 0; i < lambdas.size(); i++) {
            rtnLambdas[i] = lambdas.get(i);
        }

        return rtnLambdas;
    }
}
