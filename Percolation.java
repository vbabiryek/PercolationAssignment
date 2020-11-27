/* *****************************************************************************
 *  Name:              Vivian Kulumba
 *  Coursera User ID:  123456
 *  Last modified:     11/24/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Algorithm logic: From an entirely blocked matrix, random visited sites should open until
// the matrix proves to percolate by identifying two virtual sites in the same set.
public class Percolation {
    private boolean[][] matrix;
    private final WeightedQuickUnionUF forwardWeightedQuickUnionUF;
    private final WeightedQuickUnionUF backwashWeightedQuickUnionUF;
    private final int virtualTop;
    private final int virtualBottom;
    private int openSites;
    private final int n;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        matrix = new boolean[n][n];
        forwardWeightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 1);
        backwashWeightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        virtualTop = 0;
        virtualBottom = n * n + 1;
        openSites = 0;
        this.n = n;
    }

    public void open(int i, int j) {
        matrix[i - 1][j - 1] = true;
        openSites++;

        if (i == 1) {
            forwardWeightedQuickUnionUF.union(convertToOneDimension(i, j), virtualTop);
            backwashWeightedQuickUnionUF.union(convertToOneDimension(i, j), virtualTop);
        }

        if (i == n) {
            backwashWeightedQuickUnionUF.union(convertToOneDimension(i, j), virtualBottom);
        }

        neighborCheck(convertToOneDimension(i, j), i, j - 1);
        neighborCheck(convertToOneDimension(i, j), i - 1, j);
        neighborCheck(convertToOneDimension(i, j), i + 1, j);
        neighborCheck(convertToOneDimension(i, j), i, j + 1);
    }

    public boolean isOpen(int i, int j) {
        return matrix[i - 1][j - 1];
    }

    public boolean isFull(int row, int col) {
        return forwardWeightedQuickUnionUF.find(virtualTop) == forwardWeightedQuickUnionUF
                .find(convertToOneDimension(row, col));
    }

    // If both i and j are valid and open sites, we merge the converted 1D integer into our set
    private void neighborCheck(int w, int i, int j) {
        if (isValid(i) && isValid(j)) {
            if (isOpen(i, j)) {
                forwardWeightedQuickUnionUF.union(w, convertToOneDimension(i, j));
                backwashWeightedQuickUnionUF.union(w, convertToOneDimension(i, j));
            }
        }
    }

    // IndexOutOfBounds check
    private boolean isValid(int i) {
        int a = matrix.length;
        return (i >= 1 && i <= a);
    }

    // Converts our 2D integer to a 1D integer
    private int convertToOneDimension(int x, int y) {
        int a = matrix.length;
        return (x - 1) * a + y;
    }

    // keep track of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // If both virtualTop and virtualBottom are
    // in the same set (weightedQuickUnionUF), then return true.
    public boolean percolates() {
        return backwashWeightedQuickUnionUF.find(virtualTop) == backwashWeightedQuickUnionUF
                .find(virtualBottom);
    }
}
