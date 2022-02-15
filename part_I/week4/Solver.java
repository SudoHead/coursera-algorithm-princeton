package part_I.week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Initial board cannot be null");

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        SearchNode initialNode = new SearchNode(0, initial, null);
        pq.insert(initialNode);

        SearchNode initialTwinNode = new SearchNode(0, initial.twin(), null);
        twinPQ.insert(initialTwinNode);
        while (true) {
            solution = pq.delMin();
            SearchNode twin = twinPQ.delMin();
            if (solution.board.isGoal())
                break;
            else if (twin.board.isGoal()) {
                solution = null;
                break;
            }

            int moves = solution.moves;
            Board prevBoard = moves > 0 ? solution.prev.board : null;
            for (Board nb : solution.board.neighbors()) {
                if (nb.equals(prevBoard)) continue;
                pq.insert(new SearchNode(moves + 1, nb, solution));
            }

            int movesTwin = solution.moves;
            Board prevBoardTwin = movesTwin > 0 ? twin.prev.board : null;
            for (Board nb : twin.board.neighbors()) {
                if (nb.equals(prevBoardTwin)) continue;
                twinPQ.insert(new SearchNode(movesTwin + 1, nb, twin));
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

    private static class SearchNode implements Comparable<SearchNode> {
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

        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }

}
