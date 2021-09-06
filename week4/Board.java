import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        int size = tiles.length;
        String s = "" + size + "\n";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s += tiles[i][j];
                if (j < size - 1)
                    s += " ";
            }
            if (i < size - 1)
                s += "\n";
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hdist = 0;
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                int expectedValue = i * n + j + 1;
                if (i == n - 1 && j == i) expectedValue = 0;
                if (expectedValue != tile) hdist++;
            }
        }
        return hdist;
    }

    private static int abs(int number) {
        return (number < 0) ? -number : number;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int mdist = 0;
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                int expectedValue = i * n + j + 1;
                if (i == n - 1 && j == i) expectedValue = 0;
                if (expectedValue == tile) {
                    // sum of the vertical and horizontal distance
                    int numRow = (int)((tile - 1) / n);
                    int numCol = (tile - 1) % n;
                    mdist += abs(i - numRow) + abs(j - numCol);
                }
            }
        }
        return mdist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                int expectedValue = i * n + j + 1;
                if (i == n - 1 && j == i) expectedValue = 0;
                if (expectedValue != tile) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // neighbouring cells of the blank square (0)
        List<Board> neighborsList = new ArrayList<>();
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    if (i > 0) { // north

                    }
                    if (i < n - 1) { // south

                    }
                    if (j > 0) { // west

                    }
                    if (j < n - 1) { // east

                    }
                }
            }
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
