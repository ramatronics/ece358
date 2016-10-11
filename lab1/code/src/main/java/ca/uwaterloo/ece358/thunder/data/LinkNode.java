package ca.uwaterloo.ece358.thunder.data;

public class LinkNode {
    public int info;
    public int time;
    public LinkNode next;

    public LinkNode(int info) {
        this.info = info;
        this.time = -1;
    }
}
