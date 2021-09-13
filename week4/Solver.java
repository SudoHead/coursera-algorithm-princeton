import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private SearchNode solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Initial board cannot be null");

        MinPQ<SearchNode> pq;
        Comparator<SearchNode> comp = new Comparator<SearchNode>() {
            public int compare(SearchNode o1, SearchNode o2) {
                return o1.priority - o2.priority;
            }
        };
        pq = new MinPQ<>(comp);
        SearchNode initialNode = new SearchNode(0, initial, null);
        pq.insert(initialNode);
        while (true) {
            solution = pq.delMin();
            int moves = solution.moves;
            if (solution.board.isGoal())
                break;
            if (solution.board.hamming() == 2 && solution.board.twin().isGoal()) {
                solution = null;
                break;
            }
            for (Board nb : solution.board.neighbors()) {
                SearchNode node = new SearchNode(moves + 1, nb, solution);
                if (!node.board.equals(solution.board)) pq.insert(node);
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return solution.moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        SearchNode node = solution;
        Stack<Board> sol = new Stack<>();
        sol.push(node.board);
        while (node != null) {
            sol.push(node.board);
            node = node.prev;
        }
        return sol;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private static class SearchNode {
        private final int moves;
        private final Board board;
        private final SearchNode prev;
        private final int priority;

        public SearchNode(int moves, Board board, SearchNode prev) {
            this.moves = moves;
            this.board = board;
            this.prev = prev;
            priority = board.manhattan() + moves;
        }
    }

}
