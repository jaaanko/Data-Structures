package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

import java.util.Arrays;

public class PercolationStats {
    private double[] percolationThreshold;
    private int T;
    private static final double CONFIDENCE_LEVEL_95 = 1.96;

    public PercolationStats(int N, int T, PercolationFactory pf){
        if(N <= 0 || T <= 0){
            throw new java.lang.IllegalArgumentException();
        }
        this.T = T;
        this.percolationThreshold = new double[T];

        int i = 0;
        int row;
        int col;
        Percolation p;

        while(i < T){
            p = pf.make(N);
            while(!p.percolates()) {
                row = StdRandom.uniform(N);
                col = StdRandom.uniform(N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                }
            }
            percolationThreshold[i] = (double)p.numberOfOpenSites()/(N*N);
            i++;
        }
    }

    public double mean(){
        return StdStats.mean(percolationThreshold);
    }

    public double stddev(){
        return StdStats.stddev(percolationThreshold);
    }

    public double confidenceLow(){
        return mean() - ((CONFIDENCE_LEVEL_95 * stddev())/Math.sqrt(T));
    }

    public double confidenceHigh(){
        return mean() + ((CONFIDENCE_LEVEL_95 * stddev())/Math.sqrt(T));
    }

    //Main method for testing purposes
    public static void main(String[] args){
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),Integer.parseInt(args[1]),pf);
        double[] confidence = new double[]{ps.confidenceLow(),ps.confidenceHigh()};
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidence interval = " + Arrays.toString(confidence));
    }

}
