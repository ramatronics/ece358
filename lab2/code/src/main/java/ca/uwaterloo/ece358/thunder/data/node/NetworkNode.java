package ca.uwaterloo.ece358.thunder.data.node;

import ca.uwaterloo.ece358.thunder.data.NetworkBus;
import ca.uwaterloo.ece358.thunder.data.state.BusState;
import ca.uwaterloo.ece358.thunder.data.state.NodeState;

public class NetworkNode {
    protected static final int JAMMING_TIME = 48;
    protected static final int SENSING_TIME = 96;

    protected NetworkBus bus;
    protected NodeState state;

    protected long propagationDelay;

    protected long t_current;
    protected long t_delay;

    protected int packetLength;
    protected long packetRate;

    protected int backoffCounter;
    protected long packetsReceived;

    protected double persistence;

    public NetworkNode(NetworkBus networkBus, long propagationDelay, long packetRate, int packetLength, double persistence) {
        this.bus = networkBus;
        this.state = NodeState.IDLE;

        this.propagationDelay = propagationDelay;

        this.t_current = generateRandom(packetRate);
        this.t_delay = 0;

        this.packetLength = packetLength;
        this.packetRate = packetRate;

        this.backoffCounter = 0;
        this.packetsReceived = 0;

        this.persistence = persistence;
    }

    protected static long generateRandom(double a) {
        final double uniRandom = Math.random();
        return (long) ((Math.log(1 - uniRandom) / ((-1) * (a))) * 1000000);
    }

    protected static long generateRandomBackoff(double beb) {
        return (long) ((Math.random() * (Math.pow(2, beb) - 1)) * 1024);
    }

    public void process() {
        this.t_current--;

        if (this.state != NodeState.IDLE) {
            this.t_delay++;
        }

        switch (this.state) {
            case IDLE:
                this.idle();
                break;
            case SENSING:
                this.sense();
                break;
            case RANDOM_WAIT:
                this.randomWait();
                break;
            case SLOT_WAIT:
                this.slotWait();
                break;
            case TRANSMITTING:
                this.transmit();
                break;
            case JAMMING:
                this.jam();
                break;
            case BACKOFF:
                this.backoff();
                break;
        }
    }

    protected void slotWait() {
    }

    public long getPacketsReceived() {
        return this.packetsReceived;
    }

    public long getDelay() {
        return this.t_delay;
    }

    protected void idle() {
        if (t_current == 0) {
            this.setStateAndTime(NodeState.SENSING, SENSING_TIME);
        }
    }

    protected boolean preSense() {
        if (!bus.getNetworkState().equals(BusState.IDLE)) {
            this.setStateAndTime(NodeState.RANDOM_WAIT, generateRandomBackoff(this.backoffCounter));
            return true;
        } else {
            return this.t_current != 0;
        }
    }

    protected void sense() {
        if (!preSense()) {
            this.setStateAndTime(NodeState.TRANSMITTING, this.propagationDelay + this.packetLength);
            this.bus.sense();
        }
    }

    protected void randomWait() {
        if (this.t_current == 0) {
            this.setStateAndTime(NodeState.SENSING, SENSING_TIME);
        }
    }

    protected void transmit() {
        if (this.bus.getNetworkState().equals(BusState.COLLISION)) {
            this.setStateAndTime(NodeState.JAMMING, JAMMING_TIME);
        } else {
            if (t_current == 0) {
                this.setStateAndTime(NodeState.IDLE, generateRandom(this.packetRate));
                this.bus.push();

                this.packetsReceived++;
                this.backoffCounter = 0;
            }
        }
    }

    protected void jam() {
        if (t_current == 0) {
            this.bus.push();
            this.backoffCounter = Math.max(backoffCounter + 1, 9);
            this.setStateAndTime(NodeState.BACKOFF, generateRandomBackoff(this.backoffCounter));
        }
    }

    protected void backoff() {
        if (t_current == 0) {
            this.setStateAndTime(NodeState.SENSING, SENSING_TIME);
        }
    }

    protected void setStateAndTime(NodeState toSet, long time) {
        this.state = toSet;
        this.t_current = time;
    }
}
