package ca.uwaterloo.ece358.thunder.data;

public class QueueSimulationReport {
    public int idle = 0;
    public int packetLoss = 0;
    public int cumulativeTime = 0;
    public int sentPackets = 0;
    public int packetBuffer = 0;
    public int timeLength = 0;

    public QueueSimulationReport(int timeLength) {
        this.timeLength = timeLength;
    }

    public void print() {
        System.out.println(sentPackets);
        System.out.println("The Average Packet: " + (double) sentPackets / timeLength);
        System.out.println("The Packet Loss is: " + packetLoss);
        System.out.println("The Service Idle time is: " + ((double) idle / 1000000) + " seconds");
        System.out.println("The Sojourn time is: " + ((double) cumulativeTime / ((sentPackets - packetLoss) * 1000)) + " ms");
        System.out.println("The average amount of packets in buffer is: " + ((double) packetBuffer / (timeLength * 1000000)));
    }
}
