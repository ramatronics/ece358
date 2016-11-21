package ca.uwaterloo.ece358.thunder.data.node;

import ca.uwaterloo.ece358.thunder.data.Matrix;
import ca.uwaterloo.ece358.thunder.data.state.ChannelState;
import ca.uwaterloo.ece358.thunder.data.state.NetworkState;

public class PPersistentNetworkNode extends NetworkNode {
    public PPersistentNetworkNode(Matrix network, long propagationDelay, long lambda, int packetLength, double p) {
        super(network, propagationDelay, lambda, packetLength, p);
    }

    protected void sensing() {
        if (!this.network.getState().equals(ChannelState.IDLE)) {
            this.state = ChannelState.RANDOM_WAIT;
            this.t = backoffRandom();
        }

        if (this.t <= 0) {
            if (Math.random() <= this.p) {
                this.state = ChannelState.TRANSMITTING;
                this.network.incoming();
                this.t = this.propogationDelay + this.packetLength;
            } else {
                this.state = ChannelState.SLOT_WAIT;
                this.t = T_SENSING;
            }
        }
    }

    protected void slotWait() {
        if (!this.network.getState().equals(NetworkState.IDLE)) {
            handleCollision();
        } else {
            if (this.t == 0) {
                if (Math.random() <= this.p) {
                    this.state = ChannelState.TRANSMITTING;
                    this.network.incoming();
                    this.t = this.propogationDelay + this.packetLength;
                } else {
                    this.t = T_SENSING;
                }
            }
        }
    }

    protected void jamming() {
        if (this.t <= 0) {
            this.network.outgoing();
            this.handleCollision();
        }
    }

    private void handleCollision() {
        this.state = ChannelState.BACKOFF;

        if (this.backoffCounter < 10) {
            this.backoffCounter++;
        }

        this.t = backoffRandom();
    }
}
