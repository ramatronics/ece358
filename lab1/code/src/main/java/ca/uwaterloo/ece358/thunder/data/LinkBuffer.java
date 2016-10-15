package ca.uwaterloo.ece358.thunder.data;

import java.util.Random;

public class LinkBuffer {
    public int length;
    private LinkNode head;
    private LinkNode tail;
    private int max;
    private int last;

    private double lambda;
    private Random random;

    public LinkBuffer(int size, double lambda) {
        this.last = 0;
        this.length = 0;
        this.max = size;
        this.lambda = lambda;
        this.random = new Random();
    }

    public boolean append(int data) {
        //Packet loss can never happen in infinite queue
        if (max == -1 || length + 1 <= max) {
            LinkNode n = new LinkNode(data);

            //If empty, assign to all tracers
            if (length == 0) {
                head = n;
                tail = n;
                head.time = data;
                last = data;
            } else {
                //Append to LinkedBuffer
                tail.next = n;
                tail = n;
                last = tail.data;
            }

            length++;
            return true;
        }

        last = data;
        return false;
    }

    public int check(int referenceTime, int processTime) {
        if (length == 0) {
            return 0;
        }

        // Add head time to process time and see if packet x can be processed
        if (head.time + processTime == referenceTime) {
            LinkNode n;
            if (length == 1) {
                n = head;
                head = null;
            } else {
                n = head;
                head = head.next;

                //Update the current head time
                head.time = referenceTime + 1;
            }

            length--;
            return referenceTime - n.data;
        }

        return 0;
    }

    public double getRandom() {
        //To get more distribution of randoms
        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            //Do nothing
        }

        return (-1 / lambda) * Math.log(1 - random.nextDouble());
    }

}

