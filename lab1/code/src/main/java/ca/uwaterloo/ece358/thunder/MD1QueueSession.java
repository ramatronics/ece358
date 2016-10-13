package ca.uwaterloo.ece358.thunder;

import ca.uwaterloo.ece358.thunder.data.LinkBuffer;
import ca.uwaterloo.ece358.thunder.data.QueueSimulationReport;

import java.util.ArrayList;
import java.util.List;

public class MD1QueueSession {
    private int timeLength;
    private int processTime;
    private int packetSize;
    private int bufferSize = -1;
    private double serviceTime;

    public MD1QueueSession(int ticks, int packetSize, double service) {
        this.timeLength = ticks;
        this.packetSize = packetSize;
        this.serviceTime = service;
        this.processTime = (int) (packetSize / service * ThunderLab1.MILLION);
    }

    public List<QueueSimulationReport> runSimulations() {
        List<QueueSimulationReport> rtnReports = new ArrayList<QueueSimulationReport>();

        double[] lambdas = extractLambdas();
        for (int i = 0; i < lambdas.length; i++) {
            QueueSimulationReport report = simulate(lambdas[i]);
            if (report != null) {
                rtnReports.add(report);
            } else {
                i--;
            }
        }

        return rtnReports;
    }

    private double[] extractLambdas() {
        List<Double> lambdas = new ArrayList<Double>();
        for (double i = 0.3; i < 0.75; i += 0.1) {
            lambdas.add((i * processTime) / timeLength);
        }

        double[] rtnLambdas = new double[lambdas.size()];
        for (int i = 0; i < lambdas.size(); i++) {
            rtnLambdas[i] = lambdas.get(i);
        }

        return rtnLambdas;
    }

    private QueueSimulationReport simulate(double lambda) {
        QueueSimulationReport qReport = new QueueSimulationReport(packetSize, serviceTime, lambda, timeLength);

        LinkBuffer queue = new LinkBuffer(bufferSize, lambda);

        int nextPacket = (int) (queue.getRandom() * ThunderLab1.MILLION);
        int last = 0;

        for (int x = 0; x < timeLength * ThunderLab1.MILLION; x++) {
            if (queue.length == 0) {
                qReport.idle++;
            }

            //Make sure current packet is greater than previous packet
            if (x == last + nextPacket) {
                qReport.sentPackets++;
                if (!queue.append(x)) {
                    qReport.packetLoss++;
                }

                nextPacket = (int) (queue.getRandom() * ThunderLab1.MILLION);
                last = x;
            }

            qReport.cumulativeTime += queue.check(x, processTime);
            qReport.packetBuffer += queue.length;
        }

        qReport.cumulativeTime += queue.length * processTime;

        if (qReport.sentPackets > lambda * timeLength - 200 && qReport.sentPackets < lambda * timeLength + 200) {
            return qReport;
        }

        return null;
    }
}
