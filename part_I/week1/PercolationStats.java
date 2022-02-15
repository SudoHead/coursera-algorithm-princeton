package part_I.week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_LEVEL_Z_95 = 1.960;
    private final double[] percolateThresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Illegal args: n = " + n + ", trials = " + trials);
        }
        percolateThresholds = new double[trials];
        for (int i = 0; i < trials; ++i) {
            // initialize all sites
            Percolation model = new Percolation(n);
            // Repeat the following until the system percolates:
            while (!model.percolates()) {
                // Choose a site uniformly at random among all blocked sites.
                int p = StdRandom.uniform(n) + 1;
                int q = StdRandom.uniform(n) + 1;
                while (model.isOpen(p, q)) {
                    p = StdRandom.uniform(n) + 1;
                    q = StdRandom.uniform(n) + 1;
                }
                // Open the site.
                model.open(p, q);
            }
            percolateThresholds[i] = (double) model.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolateThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolateThresholds);
    }

    private double stderr() {
        return this.stddev() / Math.sqrt(percolateThresholds.length);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - CONFIDENCE_LEVEL_Z_95 * this.stderr();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + CONFIDENCE_LEVEL_Z_95 * this.stderr();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            int n = Integer.parseInt(args[0]);
            int trials = Integer.parseInt(args[1]);
            PercolationStats stats = new PercolationStats(n, trials);
            System.out.println("mean\t\t\t= " + stats.mean());
            System.out.println("stddev\t\t\t= " + stats.stddev());
            System.out.println("95% confidence interval\t= [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
        }
    }
}
