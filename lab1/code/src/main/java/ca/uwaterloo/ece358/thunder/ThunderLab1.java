package ca.uwaterloo.ece358.thunder;

import ca.uwaterloo.ece358.thunder.data.LinkBuffer;
import ca.uwaterloo.ece358.thunder.data.QueueSimulationReport;

import java.util.List;

public class ThunderLab1 {
    public static final int MILLION = 1000000;

    public static void main(String[] args) {

        //Set this to -1 for MD1 Queue
        int bufferSize = 50;

        //Average number of packets generated
        double lambda = 700;

        //Length of packets in bits
        int packetSize = 2000;

        //Service time received by a packet (bits/second) ---
        double service = 1 * MILLION;

        //How long to keep queueing for
        int timeLength = 10;

        MD1QueueSession md1QueueSession = new MD1QueueSession(timeLength, packetSize, service);

        boolean header = false;
        for (int i = 0; i < 5; i++) {
            List<QueueSimulationReport> tmp = md1QueueSession.runSimulations();
            for (QueueSimulationReport q : tmp) {
                if (!header) {
                    System.out.println(QueueSimulationReport.getCSVHeader());
                    header = true;
                }
                System.out.println(q.toCSV());
            }
        }


        header = false;

        int[] bufferSizes = {10, 25, 50};

        for (int j = 0; j < bufferSizes.length; j++) {
            MD1KQueueSession md1KQueueSession = new MD1KQueueSession(bufferSizes[j], timeLength, packetSize, service);
            for (int i = 0; i < 5; i++) {
                List<QueueSimulationReport> tmp = md1KQueueSession.runSimulations();
                for (QueueSimulationReport q : tmp) {
                    if (!header) {
                        System.out.println(QueueSimulationReport.getCSVHeader());
                        header = true;
                    }
                    System.out.println(q.toCSV());
                }
            }
        }
    }
}
