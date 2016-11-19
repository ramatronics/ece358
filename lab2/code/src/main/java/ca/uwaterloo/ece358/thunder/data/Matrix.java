package ca.uwaterloo.ece358.thunder.data;

import ca.uwaterloo.ece358.thunder.data.state.NetworkState;

public class Matrix {
    private int busy;
    private int pending;

    public Matrix() {
        this.busy = 0;
        this.pending = 0;
    }

    public NetworkState getState() {
        if (busy > 1)
            return NetworkState.COLLISION;

        if (busy == 1)
            return NetworkState.BUSY;

        return NetworkState.IDLE;
    }

    public void incoming() {
        this.pending++;
    }

    public void outgoing() {
        this.pending--;
    }

    public void reset() {
        this.busy += pending;
        this.pending = 0;
    }
}
