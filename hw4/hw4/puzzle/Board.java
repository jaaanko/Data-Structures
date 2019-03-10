package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    public int[][] tiles;

    public Board(int[][] tiles){
        this.tiles = tiles;
    }
    public int tileAt(int i, int j){
        return tiles[i][j];
    }
    public int size(){
        return 0;
    }
    public Iterable<WorldState> neighbors(){
        return null;
    }
    public int hamming(){
        return 0;
    }
    public int manhattan(){
        return 0;
    }
    public int estimatedDistanceToGoal(){
        return 0;
    }
    public boolean equals(Object y){
        return false;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
