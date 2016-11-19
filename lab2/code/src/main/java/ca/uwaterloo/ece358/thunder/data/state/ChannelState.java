package ca.uwaterloo.ece358.thunder.data.state;

public enum ChannelState {
    IDLE,
    SENSING,
    SLOT_WAIT,
    RANDOM_WAIT,
    TRANSMITTING,
    JAMMING,
    BACKOFF
}
