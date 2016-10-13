package ca.uwaterloo.ece358.thunder.data;

public class QueueSimulationReport {
    public int bufferSize;
    public int packetSize;
    public double serviceTime;
    public double lambda;
    public int timeLength;

    public int idle = 0;
    public int packetLoss = 0;
    public int cumulativeTime = 0;
    public int sentPackets = 0;
    public int packetBuffer = 0;

    public QueueSimulationReport(int bufferSize, int packetSize, double serviceTime, double lambda, int timeLength) {
        this.bufferSize = bufferSize;
        this.packetSize = packetSize;
        this.serviceTime = serviceTime;
        this.lambda = lambda;
        this.timeLength = timeLength;
    }

    public String toCSV() {
        double averagePacket = (double) sentPackets / timeLength;
        double idleTime = ((double) idle / 1000000);
        double sojournTime = ((double) cumulativeTime / ((sentPackets - packetLoss) * 1000));
        double averagePacketsInBuffer = ((double) packetBuffer / (timeLength * 1000000));

        StringBuilder sb = new StringBuilder();
        sb.append(bufferSize + ",");
        sb.append(packetSize + ",");
        sb.append(serviceTime + ",");
        sb.append(lambda + ",");
        sb.append(timeLength + ",");
        sb.append(averagePacket + ",");
        sb.append(packetLoss + ",");
        sb.append(idleTime + ",");
        sb.append(sojournTime + ",");
        sb.append(averagePacketsInBuffer);
        return sb.toString();
    }

    public static String getCSVHeader() {
        return "buffersize,packetsize,servicetime,lambda,timelength,averagepacket,packetloss,idletime,sojourn,packetsinbuffer";
    }
}
