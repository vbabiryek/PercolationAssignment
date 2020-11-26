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
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private final int virtualTop;
    private final int virtualBottom;
    private int openSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        matrix = new boolean[n][n];
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        virtualTop = 0;
        virtualBottom = n * n + 1;
        openSites = 0;

        if (n > 1) {
            for (int k = 1; k <= n; k++) {
                weightedQuickUnionUF.union(convertToOneDimension(1, k),
                                           virtualTop); // connect virtual top to matrix
                weightedQuickUnionUF.union(convertToOneDimension(n, k),
                                           virtualBottom); // connect virtual bottom to matrix
            }
        }
    }

    public void open(int i, int j) {
        matrix[i - 1][j - 1] = true;

        if (matrix.length == 1) {
            weightedQuickUnionUF.union(convertToOneDimension(i, j), virtualTop);
            weightedQuickUnionUF.union(convertToOneDimension(i, j), virtualBottom);
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
        if (isOpen(row, col) && weightedQuickUnionUF.find(virtualTop) == weightedQuickUnionUF
                .find(virtualBottom)) {
            return true;
        }
        else {
            return false;
        }
    }

    // If both i and j are valid and open sites, we merge the converted 1D integer into our set
    private void neighborCheck(int w, int i, int j) {
        if (isValid(i) && isValid(j)) {
            if (isOpen(i, j)) {
                weightedQuickUnionUF.union(w, convertToOneDimension(i, j));
                openSites++;
            }
        }
    }

    // IndexOutOfBounds check
    private boolean isValid(int i) {
        int n = matrix.length;
        return (i >= 1 && i <= n);
    }

    // Converts our 2D integer to a 1D integer
    private int convertToOneDimension(int x, int y) {
        int n = matrix.length;
        return (x - 1) * n + y;
    }

    // keep track of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // If both virtualTop and virtualBottom are
    // in the same set (weightedQuickUnionUF), then return true.
    public boolean percolates() {
        return weightedQuickUnionUF.find(virtualTop) == weightedQuickUnionUF.find(virtualBottom);
    }
}
