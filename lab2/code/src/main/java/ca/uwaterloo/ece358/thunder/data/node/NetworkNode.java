package ca.uwaterloo.ece358.thunder.data.node;

import ca.uwaterloo.ece358.thunder.data.Matrix;
import ca.uwaterloo.ece358.thunder.data.state.ChannelState;
import ca.uwaterloo.ece358.thunder.data.state.NetworkState;

public class NetworkNode {
    protected static final Integer T_JAMMING = 48;
    protected static final Integer T_SENSING = T_JAMMING * 2;

    protected long propogationDelay;
    private long requestsCompleted;
    protected long t;
    private long lambda;
    private long delayTime;

    protected ChannelState state;
    protected Matrix network;

    protected int packetLength;
    protected int backoffCounter;

    protected double p;

    public NetworkNode(Matrix network, long propagationDelay, long lambda, int packetLength, double p) {
        this.propogationDelay = propagationDelay;
        this.lambda = lambda;
        this.packetLength = packetLength;
        this.p = p;
        this.network = network;

        this.backoffCounter = 0;
        this.t = randomVariable();
        this.requestsCompleted = 0;
        this.delayTime = 0;
        this.state = ChannelState.IDLE;
        this.p = p;

    }

    public long getRequestsCompleted() {
        return this.requestsCompleted;
    }

    public long getDelayTime() {
        return this.delayTime;
    }

    public void process() {
        this.t--;

        if (state != ChannelState.IDLE) {
            this.delayTime++;
        }

        switch (state) {
            case IDLE:
                this.idle();
                break;
            case SENSING:
                this.sensing();
                break;
            case RANDOM_WAIT:
                this.randomWait();
                break;
            case SLOT_WAIT:
                this.slotWait();
                break;
            case TRANSMITTING:
                this.transmitting();
                break;
            case JAMMING:
                this.jamming();
                break;
            case BACKOFF:
                this.backoff();
                break;
        }
    }

    protected void idle() {
        if (this.t == 0) {
            this.state = ChannelState.SENSING;
            this.t = T_SENSING;
        }
    }

    protected void sensing() {
        if (!this.network.getState().equals(NetworkState.IDLE)) {
            if (this.p == 0.0) {
                this.t = backoffRandom();
                this.state = ChannelState.RANDOM_WAIT;
            }
        } else {
            if (this.t == 0) {
                this.state = ChannelState.TRANSMITTING;
                this.network.incoming();
                this.t = this.propogationDelay + this.packetLength;
            }
        }
    }

    protected void randomWait() {
        if (this.t == 0) {
            this.t = T_SENSING;
            this.state = ChannelState.SENSING;
        }
    }

    protected void transmitting() {
        if (this.network.getState().equals(NetworkState.COLLISION)) {
            this.state = ChannelState.JAMMING;
            this.t = T_JAMMING;
        }

        if (this.t == 0) {
            this.state = ChannelState.IDLE;
            this.network.outgoing();

            this.t = randomVariable();
            this.requestsCompleted++;
            this.backoffCounter = 0;
        }
    }

    protected void slotWait() {

    }

    protected void jamming() {
        if (this.t > 0) {
            return;
        }

        this.state = ChannelState.BACKOFF;
        this.network.outgoing();

        if (this.backoffCounter < 10) {
            this.backoffCounter++;
        }

        this.t = backoffRandom();
    }

    protected void backoff() {
        if (t == 0) {
            this.state = ChannelState.SENSING;
            this.t = T_SENSING;
        }
    }

    protected long randomVariable() {
        double uniformRandomVariable = Math.random();
        return (long) ((Math.log(1 - uniformRandomVariable) / (-this.lambda)) * 1000000.0);
    }

    protected long backoffRandom() {
        return (long) ((Math.random() * (Math.pow(2.0, this.backoffCounter) - 1.0)) * 512.0);
    }
}
