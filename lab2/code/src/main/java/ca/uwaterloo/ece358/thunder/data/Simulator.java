package ca.uwaterloo.ece358.thunder.data;

import ca.uwaterloo.ece358.thunder.data.node.NetworkNode;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class Simulator <T extends NetworkNode> {
    private List<T> nodes;
    private Graph network;
    private int packetLength;
    private long ticks;
    private double p;
    private int N;
    private long lambda;
    private long networkSpeed;

    public Simulator(long ticks, int N, long lambda, long networkSpeed, int packetLength, double p){
        this.nodes = new ArrayList<T>();
        this.N =N;
        this.lambda = lambda;
        this.networkSpeed = networkSpeed;
        this.packetLength = packetLength * 8;
        this.ticks = ticks;
        this.p = p;

        for(int i =0; i<N; i++){
            this.nodes.add();
        }
    }
}
