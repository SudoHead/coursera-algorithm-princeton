package part_II.week2;

import edu.princeton.cs.algs4.Bag;

import java.util.ArrayList;

public class GridDAG {
    /*
     * Acyclic digraph, where weights are on the vertices instead of the edges.
     * It features two virtual vertices, one in the front and the other in the back.
     *
     *  O = virtual vertex, 0 = pixel vertex
     *
     *         0 - 0 - 0
     *       /   X   X   \
     *     O - 0 - 0 - 0 - O
     *       \   X   X   /
     *         0 - 0 - 0
     */

    private final double[] weights;
    private final int width;
    private final int height;

    private final ArrayList<Bag<Integer>> adj;

    public GridDAG(int width, int height, double[] weights) {
        this.width = width;
        this.height = height;
        this.weights = new double[size()];
        adj = new ArrayList<>(size());
        for (int v = 0; v < size(); v++) {
            if (v < weights.length) this.weights[v] = weights[v];
            adj.add(new Bag<>());
        }
    }

    public void addEdge(int v, int w) {
        adj.get(v).add(w);
    }

    public Iterable<Integer> adj(int v) {
        return adj.get(v);
    }

    public int getVirtualFrontVertex() {
        return size() - 2;
    }

    public int getVirtualBackVertex() {
        return size() - 1;
    }

    public int size() {
        return width * height + 2;
    }

    public double weight(int v) {
        return weights[v];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
