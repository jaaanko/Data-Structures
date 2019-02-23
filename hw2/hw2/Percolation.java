package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int N;
    private WeightedQuickUnionUF wqu;
    private int openCount;
    private int top;
    private int bottom;

    Percolation(int N){
        this.N = N;
        if(N <=0){
            throw new java.lang.IllegalArgumentException();
        }
        wqu = new WeightedQuickUnionUF((N*N)+2);
        top = N*N;
        bottom = (N*N)+1;

        openCount = 0;
        grid = new boolean[N][N];
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                grid[i][j] = false;
            }
        }
    }

    public void open(int row, int col){
        if(row >= N && col >= N){
            throw new java.lang.IndexOutOfBoundsException();
        }
        if(!isOpen(row,col)) {

            grid[row][col] = true;
            openCount++;
            if(row == 0){
                wqu.union(convert(row,col),top);
            }
            else if(row == N-1){
                wqu.union(convert(row,col),bottom);
            }
            if (row + 1 < N) {
                if (isOpen(row + 1, col)) {
                    wqu.union(convert(row, col), convert(row + 1, col));
                }
            }
            if (row - 1 >= 0) {
                if (isOpen(row - 1, col)) {
                    wqu.union(convert(row, col), convert(row - 1, col));
                }
            }
            if (col + 1 < N) {
                if (isOpen(row, col + 1)) {
                    wqu.union(convert(row, col), convert(row, col + 1));
                }
            }
            if (col - 1 >= 0) {
                if (isOpen(row, col - 1)) {
                    wqu.union(convert(row, col), convert(row, col - 1));
                }
            }
        }
    }

    public int numberOfOpenSites(){
        return openCount;
    }

    public boolean isOpen(int row, int col){
        if(row >= N && col >= N){
            throw new java.lang.IndexOutOfBoundsException();
        }
        return grid[row][col];
    }

    public boolean isFull(int row, int col){
        return wqu.connected(convert(row,col),top);
    }

    public boolean percolates(){
        return wqu.connected(top,bottom);
    }

    public int convert(int row, int col){
        return col + (row*N);
    }
}
