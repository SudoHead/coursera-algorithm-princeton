import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int[][] tiles;
    private int blankX;
    private int blankY;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        int size = tiles.length;
        StringBuilder sb = new StringBuilder();
        sb.append(size).append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(String.format("%2d ", tiles[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
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
                if (expectedValue != tile && tile != 0) hdist++;
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
                if (tile != 0) {
                    int expRow = ((tile - 1) / n);
                    int expCol = (tile - 1) % n;
                    int manDist = abs(i - expRow) + abs(j - expCol);
                    mdist += manDist;
//                    System.out.println("value: " + tile + " | ExpRow = " + expRow + ", ExpCol = " + expCol + ", dist: " + manDist);
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
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board other = (Board) y;
        if (this.blankX != other.blankX)
            return false;
        if (this.blankY != other.blankY)
            return false;
        if (this.tiles.length != other.tiles.length)
            return false;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] != other.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    private void swap(int ax, int ay, int bx, int by) {
        int temp = tiles[ax][ay];
        tiles[ax][ay] = tiles[bx][by];
        tiles[bx][by] = temp;
        if (tiles[ax][ay] == 0) {
            blankX = ax;
            blankY = ay;
        }
        if (tiles[bx][by] == 0) {
            blankX = bx;
            blankY = by;
        }
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
                        Board b = new Board(tiles);
                        b.swap(i, j, i - 1, j);
                        neighborsList.add(b);
                    }
                    if (i < n - 1) { // south
                        Board b = new Board(tiles);
                        b.swap(i, j, i + 1, j);
                        neighborsList.add(b);
                    }
                    if (j > 0) { // west
                        Board b = new Board(tiles);
                        b.swap(i, j, i, j - 1);
                        neighborsList.add(b);
                    }
                    if (j < n - 1) { // east
                        Board b = new Board(tiles);
                        b.swap(i, j, i, j + 1);
                        neighborsList.add(b);
                    }
                }
            }
        }
        return neighborsList;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(tiles);
        if (twin.blankX != 0) {
            twin.swap(0, 0, 0, 1);
        } else {
            twin.swap(1, 0, 1, 1);
        }
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
                {
                        8, 1, 3
                },
                {
                        4, 0, 2
                },
                {
                        7, 6, 5
                }
        };
        Board slider = new Board(tiles);
        System.out.println(slider);
        System.out.println("Twin");
        System.out.println(slider.twin());
        System.out.println("Neighbours");
        for (Board b : slider.neighbors()) {
            System.out.println(b);
        }

        System.out.println("Dimension: " + slider.dimension());
        System.out.println("Hamming: " + slider.hamming());
        System.out.println("Manhattan: " + slider.manhattan());
        System.out.println("IsGoal: " + slider.isGoal());
        System.out.println("Is equal: " + slider.equals(slider));
    }
}
