package part_II.week2;

import edu.princeton.cs.algs4.Stack;

public class Seam {
    private final int[] edgeTo;
    private final double[] distTo;
    private final Iterable<Integer> seam;
    private final double energy;

    public Seam(GridDAG G) {
        edgeTo = new int[G.size()];
        distTo = new double[G.size()];

        for (int v = 0; v < G.size(); v++) {
            edgeTo[v] = -1;
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        // source is the front virtual vertex
        distTo[G.getVirtualFrontVertex()] = 0.0;

        ReversePostOrder topological = new ReversePostOrder(G);
        for (int v : topological.order())
            for (int w : G.adj(v))
                relax(G, v, w);

        energy = distTo[G.getVirtualBackVertex()];
        seam = pathTo(G.getVirtualBackVertex());
    }

    private void relax(GridDAG G, int v, int w) {
        if (distTo[w] > distTo[v] + G.weight(w)) {
            distTo[w] = distTo[v] + G.weight(w);
            edgeTo[w] = v;
        }
    }

    private Iterable<Integer> pathTo(int v) {
        Stack<Integer> path = new Stack<>();
        // if edgeTo[v] is negative == path over
        for (int vFrom = edgeTo[v]; vFrom >= 0; vFrom = edgeTo[vFrom])
            path.push(vFrom);
        // remove front virtual vertices (fist vertices), the last vertex wasn't added because it is virtual as well
        if (!path.isEmpty()) path.pop();
        return path;
    }

    public double getEnergy() {
        return energy;
    }

    public Iterable<Integer> getSeam() {
        return seam;
    }
}
