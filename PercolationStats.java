/* *****************************************************************************
 *  Name:              Vivian Kulumba
 *  Coursera User ID:  123456
 *  Last modified:     11/24/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE = 1.96;
    private final double[] statResults;
    private final int numOfTrials;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        numOfTrials = trials;

        statResults = new double[numOfTrials];
        for (int i = 0; i < numOfTrials; i++) {
            Percolation percolation = new Percolation(n);
            this.statResults[i] = simulation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }
        }
    }

    private double simulation(int n) {
        int i, j, count = 0;
        double p;

        Percolation percolation = new Percolation(n);

        do {
            i = StdRandom.uniform(1, n + 1);
            j = StdRandom.uniform(1, n + 1);
            if (!percolation.isOpen(i, j)) {
                percolation.open(i, j);
                count++;
            }
        } while (!percolation.percolates());

        p = count * 1.0 / (n * n);
        return p;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(statResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(statResults);
    }

    // low endpoint of 95% confidence interval == "normal score" for the 97.5 percentile point
    public double confidenceLo() {
        return mean() - (CONFIDENCE * stddev() / Math.sqrt(numOfTrials));
    }

    // high endpoint of 95% confidence interval == "normal score" for the 97.5 percentile point
    public double confidenceHi() {
        return mean() + (CONFIDENCE * stddev() / Math.sqrt(numOfTrials));
    }

    public static void main(String[] args) {
        int v = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(v, k);
        String interval = percolationStats.confidenceLo() + ", " + percolationStats
                .confidenceHi();
        StdOut.println("the mean here is: " + percolationStats.mean());
        StdOut.println("the standard deviation here is: " + percolationStats.stddev());
        StdOut.println("95% confidence interval here is: " + interval);
    }
}
