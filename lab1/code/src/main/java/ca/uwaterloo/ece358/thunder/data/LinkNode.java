package ca.uwaterloo.ece358.thunder.data;

public class LinkNode {
    public int time;
    public int data;
    public LinkNode next;

    public LinkNode(int data) {
        this.time = -1;
        this.data = data;
    }
}
