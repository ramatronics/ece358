package ca.uwaterloo.ece358.thunder;

import ca.uwaterloo.ece358.thunder.data.LinkBuffer;
import ca.uwaterloo.ece358.thunder.data.QueueSimulationReport;

public class ThunderLab1 {
    private static final int MILLION = 1000000;

    public static void main(String[] args) {

        //Set this to -1 for MD1 Queue
        int bufferSize = 50;

        //Average number of packets generated
        double lambda = 700;

        //Length of packets in bits
        int packetSize = 2000;

        //Service time received by a packet (bits/second) ---
        double service = MILLION;

        //How long to keep queueing for
        int timeLength = 10;

        for (int y = 0; y < 5; ) {
            QueueSimulationReport qReport = new QueueSimulationReport(timeLength);

            LinkBuffer queue = new LinkBuffer(bufferSize);

            int processTime = (int) (packetSize / service * MILLION);
            int nextPacket = (int) (queue.getRandom(lambda) * MILLION);
            int last = 0;

            for (int x = 0; x < timeLength * MILLION; x++) {
                if (queue.length == 0) {
                    qReport.idle++;
                }

                //Make sure current packet is greater than previous packet
                if (x == last + nextPacket) {
                    qReport.sentPackets++;
                    if (!queue.append(x)) {
                        qReport.packetLoss++;
                    }

                    nextPacket = (int) (queue.getRandom(lambda) * MILLION);
                    last = x;
                }

                qReport.cumulativeTime += queue.check(x, processTime);
                qReport.packetBuffer += queue.length;
            }

            qReport.cumulativeTime += queue.length * processTime;

            if (qReport.sentPackets > lambda * timeLength - 200 && qReport.sentPackets < lambda * timeLength + 200) {
                qReport.print();
                y++;
            }
        }
    }
}
