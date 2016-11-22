package ca.uwaterloo.ece358.thunder.data.node;

import ca.uwaterloo.ece358.thunder.data.NetworkBus;
import ca.uwaterloo.ece358.thunder.data.state.BusState;
import ca.uwaterloo.ece358.thunder.data.state.NodeState;

public class PPersistentNetworkNode extends NetworkNode {
    public PPersistentNetworkNode(NetworkBus bus, long propagationDelay, long lambda, int packetLength, double P) {
        super(bus, propagationDelay, lambda, packetLength, P);
    }

    private void randomPersistenceSensing(NodeState randomMissState, long randomMissTime) {
        final double rVariable = Math.random();
        if (rVariable <= this.persistence) {
            this.bus.sense();
            this.setStateAndTime(NodeState.TRANSMITTING, this.propagationDelay + this.packetLength);
        } else {
            this.setStateAndTime(randomMissState, randomMissTime);
        }
    }

    protected void sense() {
        if (!preSense()) {
            randomPersistenceSensing(NodeState.SLOT_WAIT, SENSING_TIME);
        }
    }

    protected void slotWait() {
        if (!bus.getNetworkState().equals(BusState.IDLE)) {
            handleCollision();
        } else {
            if (this.t_current == 0) {
                randomPersistenceSensing(this.state, SENSING_TIME);
            }
        }
    }

    protected void jam() {
        if (this.t_current == 0) {
            this.bus.push();
            this.handleCollision();
        }
    }

    private void handleCollision() {
        this.backoffCounter = Math.max(backoffCounter + 1, 9);
        this.setStateAndTime(NodeState.BACKOFF, generateRandomBackoff(this.packetRate));
    }
}
