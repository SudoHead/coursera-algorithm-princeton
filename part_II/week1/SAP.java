package part_II.week1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;

public class SAP {

    private static class BFS {
        private final boolean[] marked;
        private final int[] edgeTo;
        private final int[] distTo;

        public BFS(Digraph G, Iterable<Integer> s) {
            marked = new boolean[G.V()];
            edgeTo = new int[G.V()];
            distTo = new int[G.V()];
            bfs(G, s);
        }

        public BFS(Digraph G, int s) {
            this(G, Collections.singletonList(s));
        }

        private void bfs(Digraph G, Iterable<Integer> s) {
            Queue<Integer> q = new Queue<>();
            for (int si : s) {
                q.enqueue(si);
                distTo[si] = 0;
            }
            while (!q.isEmpty()) {
                int v = q.dequeue();
                if (!marked[v]) {
                    marked[v] = true;
                    for (int vi : G.adj(v)) {
                        if (!marked[vi]) {
                            q.enqueue(vi);
                            edgeTo[vi] = v;
                            distTo[vi] = distTo[v] + 1;
                        }
                    }
                }
            }
        }
    }

    private static class LeastCommonDenominator {
        private int length = Integer.MAX_VALUE;
        private int lcd = -1;

        public LeastCommonDenominator(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
            BFS bfsV = new BFS(G, v);
            BFS bfsW = new BFS(G, w);
            for (int i = 0; i < G.V(); i++) {
                if (bfsV.marked[i] && bfsW.marked[i]) {
                    int pathLength = bfsV.distTo[i] + bfsW.distTo[i];
                    if (pathLength < length) {
                        length = pathLength;
                        lcd = i;
                    }
                }
            }
        }

        public LeastCommonDenominator(Digraph G, int v, int w) {
            this(G, Collections.singletonList(v), Collections.singletonList(w));
        }

        public int getLength() {
            if (length == Integer.MAX_VALUE)
                return -1;
            return length;
        }

        public int getLcd() {
            return lcd;
        }
    }

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    private void checkValidInput(Integer v) {
        if (v == null || v < 0 || v >= G.V())
            throw new IllegalArgumentException("Invalid argument");
    }

    private void checkValidInput(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("Invalid argument");
        for (int i : v)
            checkValidInput(i);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkValidInput(v);
        checkValidInput(w);
        return new LeastCommonDenominator(G, v, w).getLength();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkValidInput(v);
        checkValidInput(w);
        return new LeastCommonDenominator(G, v, w).getLcd();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkValidInput(v);
        checkValidInput(w);
        return new LeastCommonDenominator(G, v, w).getLength();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkValidInput(v);
        checkValidInput(w);
        return new LeastCommonDenominator(G, v, w).getLcd();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
