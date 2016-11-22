package ca.uwaterloo.ece358.thunder.data;

import ca.uwaterloo.ece358.thunder.data.state.BusState;

import java.util.ArrayList;
import java.util.List;

public class NetworkBus {
    private List<Boolean> queue;
    private int endOfCycleSize;

    public NetworkBus() {
        this.queue = new ArrayList<Boolean>();
        this.endOfCycleSize = 0;
    }

    public BusState getNetworkState() {
        if (endOfCycleSize > 1)
            return BusState.COLLISION;

        if (endOfCycleSize == 1) {
            return BusState.BUSY;
        }

        return BusState.IDLE;
    }

    public void sense() {
        queue.add(true);
    }

    public void push() {
        if (!queue.isEmpty()) {
            queue.remove(0);
        }
    }

    public void cycle() {
        endOfCycleSize = queue.size();
    }
}
