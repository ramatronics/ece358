package ca.uwaterloo.ece358.thunder;

import ca.uwaterloo.ece358.thunder.data.QueueSimulationReport;
import ca.uwaterloo.ece358.thunder.session.MD1KQueueSession;
import ca.uwaterloo.ece358.thunder.session.MD1QueueSession;

import java.io.PrintWriter;
import java.util.List;

public class ThunderLab1 {
    public static final int MILLION = 1000000;

    //Length of packets in bits
    private static int packetSize = 2000;

    //Service time received by a packet (bits/second) ---
    private static double service = 1 * MILLION;

    //How long to keep queueing for
    private static int timeLength = 10;

    //Run the same simulation 5 times with the same parameters and average the results
    private static int simulationSampleSize = 5;

    public static void main(String[] args) {
        runMD1QueueSimulation();
        runMD1KQueueSimulation();
    }

    private static void runMD1QueueSimulation() {
        List<QueueSimulationReport>[] reports = new List[simulationSampleSize];

        MD1QueueSession md1QueueSession = new MD1QueueSession(timeLength, packetSize, service);
        for (int i = 0; i < reports.length; i++) {
            System.out.println("Running MD1 simulation: " + i);
            reports[i] = md1QueueSession.runSimulations();
        }

        outputReports(reports, "MD1");
    }

    private static void runMD1KQueueSimulation() {
        List<QueueSimulationReport>[] reports = new List[simulationSampleSize];

        int[] bufferSizes = {10, 25, 50};

        for (int i = 0; i < reports.length; i++) {
            for (int j = 0; j < bufferSizes.length; j++) {
                System.out.println("Running MD1K simulation for buffer size " + bufferSizes[j]);
                MD1KQueueSession md1KQueueSession = new MD1KQueueSession(bufferSizes[j], timeLength, packetSize, service);
                if (reports[i] == null) {
                    reports[i] = md1KQueueSession.runSimulations();
                } else {
                    reports[i].addAll(md1KQueueSession.runSimulations());
                }
            }
        }

        outputReports(reports, "MD1K");
    }

    private static void outputReports(List<QueueSimulationReport>[] reports, String fileName) {
        StringBuilder sb = new StringBuilder();
        boolean header = false;
        for (int i = 0; i < reports.length; i++) {
            for (QueueSimulationReport q : reports[i]) {
                if (!header) {
                    sb.append(QueueSimulationReport.getCSVHeader());
                    sb.append("\n");
                    System.out.println(QueueSimulationReport.getCSVHeader());
                    header = true;
                }
                sb.append(q.toCSV());
                sb.append("\n");
                System.out.println(q.toCSV());
            }
        }

        try {
            PrintWriter writer = new PrintWriter(fileName + ".csv", "UTF-8");
            writer.println(sb.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println(sb.toString());
        }
    }
}
