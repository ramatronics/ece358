package ca.uwaterloo.ece358.thunder;

import ca.uwaterloo.ece358.thunder.data.LinkBuffer;

import java.util.Random;

public class ThunderLab1 {
    public static void main(String[] args) {
        for (int y = 0; y < 5; ) {
            double lamda = 700;
            int packetsize = 2000;
            double service = 1000000;
            int buffersize = 50;
            int timelength = 10;
            int idle = 0;
            int ploss = 0;
            int avtime = 0;
            int sentpack = 0;
            int packetbuffer = 0;
            LinkBuffer infinite;

            if (buffersize != -1) {
                infinite = new LinkBuffer(buffersize);
            } else {
                infinite = new LinkBuffer();
            }

            int processtime = (int) (packetsize / service * 1000000);
            int nextpacket = (int) (RandNum(lamda) * 1000000);
            int last = 0;

            for (int x = 0; x < timelength * 1000000; x++) {
                if (infinite.length == 0) {
                    idle++;
                }

                if (x == last + nextpacket)
                {
                    sentpack++;
                    if (!infinite.Append(x)) {
                        ploss++;
                    }
                    nextpacket = (int) (RandNum(lamda) * 1000000);
                    last = x;
                }

                avtime += infinite.check(x, processtime);
                packetbuffer += infinite.length;
            }

            avtime += infinite.length * processtime;

            if (sentpack > lamda * timelength - 200 && sentpack < lamda * timelength + 200) {
                System.out.println(sentpack);
                System.out.println("The Average Packet: " + (double) sentpack / timelength);
                System.out.println("The Packet Loss is: " + ploss);
                System.out.println("The Service Idle time is: " + ((double) idle / 1000000) + " seconds");
                System.out.println("The sojourn time is: " + ((double) avtime / ((sentpack - ploss) * 1000)) + " ms");
                System.out.println("The average amount of packets in buffer is: " + ((double) packetbuffer / (timelength * 1000000)));
                y++;
            }
        }

    }

    public static double RandNum(double lamda) {
        Random rng = new Random();

        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            //Do nothing
        }
        return (-1 / lamda) * Math.log(1 - rng.nextDouble());
    }
}
