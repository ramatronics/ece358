package ca.uwaterloo.ece358.thunder.data;

import java.util.Random;

public class LinkBuffer {
    public int length;
    private LinkNode head;
    private LinkNode tail;
    private int max;
    private int last;

    public LinkBuffer() {
        this(-1);
    }

    public LinkBuffer(int size) {
        this.last = 0;
        this.length = 0;
        this.max = size;
    }

    public boolean append(int data) {
        //Packet loss can never happen in infinite queue
        if (max == -1 || length + 1 <= max) {
            LinkNode n = new LinkNode(data);

            if (length == 0) {
                head = n;
                tail = n;
                head.time = data;
                last = data;
            } else {
                tail.next = n;
                tail = n;
                last = tail.info;
            }

            length++;
            return true;
        }

        last = data;
        return false;
    }

    public int check(int x, int service) {
        if (length != 0) {
            if (head.time + service == x) {

                LinkNode n;
                if (length == 1) {
                    n = head;
                    head = null;
                } else {
                    n = head;
                    head = head.next;
                    head.time = x + 1;
                }

                length--;
                return x - n.info;
            }
        }
        return 0;
    }

    public double getRandom(double lambda) {
        Random rng = new Random();

        //To get more distribution of randoms
        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            //Do nothing
        }

        return (-1 / lambda) * Math.log(1 - rng.nextDouble());
    }

}

