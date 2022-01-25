package part_II.week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    // Find all possible words starting from a specific cell
    private static class BoggleSingleCellSolver {
        private final Set<String> validWords;
        private final int rows, cols;

        public BoggleSingleCellSolver(BoggleBoard board, BoggleTrie bt, int i, int j) {
            validWords = new HashSet<>();
            rows = board.rows();
            cols = board.cols();
            boolean[] path = new boolean[rows * cols];
            StringBuilder startingKey = new StringBuilder().append(board.getLetter(i, j));
            BoggleTrie.Node root = bt.get(startingKey.toString());
            solve(board, i, j, path, root);
        }

        public Set<String> getValidWords() {
            return validWords;
        }

        private void solve(BoggleBoard board, int i, int j, boolean[] path, BoggleTrie.Node node) {
            if (board.getLetter(i, j) == 'Q')
                node = node.getNext('U');
            if (node == null) return;

            // Check current word is valid
            if (node.isString() && node.getWord().length() >= 3)
                validWords.add(node.getWord());

            boolean[] currentPath = path.clone();
            currentPath[to1D(i, j)] = true;

            // run solve with neighbour cells
            int startRow = Math.max(0, i - 1);
            int endRow = Math.min(board.rows() - 1, i + 1);
            int startCol = Math.max(0, j - 1);
            int endCol = Math.min(board.cols() - 1, j + 1);

            for (int r = startRow; r <= endRow; r++) {
                for (int c = startCol; c <= endCol; c++) {
                    char letter = board.getLetter(r, c);
                    BoggleTrie.Node nextNode = node.getNext(letter);
                    if (!currentPath[to1D(r, c)] && nextNode != null) {
                        solve(board, r, c, currentPath, nextNode);
                    }
                }
            }
        }

        private int to1D(int i, int j) {
            return i * cols + j;
        }
    }

    private final BoggleTrie bt;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        bt = new BoggleTrie();
        for (String s : dictionary)
            bt.add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> allValidWords = new HashSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                BoggleSingleCellSolver scs = new BoggleSingleCellSolver(board, bt, i, j);
                allValidWords.addAll(scs.getValidWords());
            }
        }
        return allValidWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int pointValue = 0;
        int length = word.length();
        if (bt.contains(word) && length >= 3) {
            if (length < 5) pointValue = 1;
            else if (length == 5) pointValue = 2;
            else if (length == 6) pointValue = 3;
            else if (length == 7) pointValue = 5;
            else pointValue = 11;
        }
        return pointValue;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int size = 0;
        for (String word : solver.getAllValidWords(board)) {
            score += solver.scoreOf(word);
            StdOut.println(word);
            size++;
        }
        StdOut.println("Number of entries: " + size);
        StdOut.println("Score = " + score);

        int n = 50000;
        StdOut.println("Benchmark " + n + " Hasbro boards (4x4)...");
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            BoggleBoard b = new BoggleBoard();
            solver.getAllValidWords(b);
        }
        double took = Math.max(System.currentTimeMillis() - start, 1) / 1000.0;
        StdOut.println("Took: " + took + " s");
        StdOut.println("Solving speed: " + n / took + " board/s");
    }
}
