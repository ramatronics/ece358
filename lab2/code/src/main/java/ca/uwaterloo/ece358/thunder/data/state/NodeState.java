package ca.uwaterloo.ece358.thunder.data.state;

public enum NodeState {
    IDLE,
    SENSING,
    SLOT_WAIT,
    RANDOM_WAIT,
    TRANSMITTING,
    JAMMING,
    BACKOFF
}
