package part_II.week2;

import edu.princeton.cs.algs4.Stack;

public class ReversePostOrder {
    private final boolean[] marked;
    private final Stack<Integer> reversePost;

    public ReversePostOrder(GridDAG G) {
        marked = new boolean[G.size()];
        reversePost = new Stack<>();
        for (int v = 0; v < G.size(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    private void dfs(GridDAG G, int v) {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
        reversePost.push(v);
    }

    public Iterable<Integer> order() {
        return reversePost;
    }
}
