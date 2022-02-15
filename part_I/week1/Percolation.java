package part_I.week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private final boolean[][] lattice;
    private final WeightedQuickUnionUF unionFind;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0.");
        }
        size = n;
        unionFind = new WeightedQuickUnionUF(n * n + 2);
        lattice = new boolean[n][n];
    }

    private int convertZeroBased(int index) {
        if (index <= 0 || index > size) {
            throw new IllegalArgumentException("Illegal index " + index + ", n = " + size);
        }
        return index - 1;
    }

    private int convert2D1D(int row, int col) {
        return row * size + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        row = convertZeroBased(row);
        col = convertZeroBased(col);
        lattice[row][col] = true;
        // apply union on the neighbours
        int pos = convert2D1D(row, col);
        if (row > 0 && lattice[row - 1][col])
            unionFind.union(pos, convert2D1D(row - 1, col));
        if (row < lattice.length - 1 && lattice[row + 1][col])
            unionFind.union(pos, convert2D1D(row + 1, col));
        if (col > 0 && lattice[row][col - 1])
            unionFind.union(pos, convert2D1D(row, col - 1));
        if (col < lattice.length - 1 && lattice[row][col + 1])
            unionFind.union(pos, convert2D1D(row, col + 1));
        // apply union to virtual top/bottom if applies
        if (row == 0) {
            unionFind.union(size * size, pos);
        }
        if (row == size - 1) {
            unionFind.union(size * size + 1, pos);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        row = convertZeroBased(row);
        col = convertZeroBased(col);
        return lattice[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        row = convertZeroBased(row);
        col = convertZeroBased(col);
        return unionFind.find(convert2D1D(row, col)) == unionFind.find(size * size);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int numOpen = 0;
        for (boolean[] booleans : lattice) {
            for (int j = 0; j < lattice.length; ++j) {
                if (booleans[j]) {
                    numOpen += 1;
                }
            }
        }
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFind.find(size * size) == unionFind.find(size * size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 10;
        Percolation p = new Percolation(n);

        System.out.println("isFull with 0 opens: " + p.isFull(1, 1));
        p.open(1, 1);
        System.out.println("isFull with 1 opens: " + p.isFull(1, 1));

        int numOpen = StdRandom.uniform(n * n);
        for (int i = 1; i < numOpen; ++i) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            p.open(row, col);
            System.out.println("row = " + row + " col = " + col + " open = " + p.isOpen(row, col));
        }
        System.out.println("Number of opened sites: " + p.numberOfOpenSites());
        System.out.println(p.percolates());

    }
}
