package ca.uwaterloo.ece358.thunder.session;

import ca.uwaterloo.ece358.thunder.ThunderLab1;
import ca.uwaterloo.ece358.thunder.data.LinkBuffer;
import ca.uwaterloo.ece358.thunder.data.QueueSimulationReport;

import java.util.ArrayList;
import java.util.List;

public class MD1QueueSession {
    protected int timeLength;
    protected int processTime;
    protected int packetSize;
    protected int bufferSize;
    protected double serviceTime;

    public MD1QueueSession(int bufferSize, int ticks, int packetSize, double service) {
        this.bufferSize = bufferSize;
        this.timeLength = ticks;
        this.packetSize = packetSize;
        this.serviceTime = service;
        this.processTime = (int) (packetSize / service * ThunderLab1.MILLION);
    }

    public MD1QueueSession(int ticks, int packetSize, double service) {
        this(-1, ticks, packetSize, service);
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

    protected double[] extractLambdas() {
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
        QueueSimulationReport qReport = new QueueSimulationReport(bufferSize, packetSize, serviceTime, lambda, timeLength);

        LinkBuffer queue = new LinkBuffer(bufferSize, lambda);

        int nextPacket = (int) (queue.getRandom() * ThunderLab1.MILLION);
        int last = 0;

        for (int time = 0; time < timeLength * ThunderLab1.MILLION; time++) {
            if (queue.length == 0) {
                qReport.idle++;
            }

            //Make sure current packet is greater than previous packet
            if (time == last + nextPacket) {
                qReport.sentPackets++;
                if (!queue.append(time)) {
                    qReport.packetLoss++;
                }

                nextPacket = (int) (queue.getRandom() * ThunderLab1.MILLION);
                last = time;
            }

            qReport.cumulativeTime += queue.check(time, processTime);
            qReport.packetBuffer += queue.length;
        }

        qReport.cumulativeTime += queue.length * processTime;

        if (qReport.sentPackets > lambda * timeLength - 200 && qReport.sentPackets < lambda * timeLength + 200) {
            return qReport;
        }

        return null;
    }
}
