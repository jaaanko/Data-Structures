package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private int[][] tiles;
    private static final int BLANK = 0;
    private int N;

    public Board(int[][] tiles){
        this.N = tiles.length;
        this.tiles = new int[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    public int tileAt(int i, int j){
        if(i < 0 || i > N-1 || j < 0 || j > N-1){
            throw new java.lang.IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    public int size(){
        return N;
    }

    // Source: http://joshh.ug/neighbors.html
    public Iterable<WorldState> neighbors(){
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming(){
        int hamming = 0;
        for(int x = 1; x < N*N; x++){
            if(tiles[getCorrectRow(x)][getCorrectCol(x)] != x){
                hamming ++;
            }
        }
        return hamming;
    }

    public int manhattan(){

        int manhattan = 0;
        int actual;
        int correctRow;
        int correctCol;
        int wrongRow;
        int wrongCol;

        for(int x = 1; x < N*N; x++){
            if(tiles[getCorrectRow(x)][getCorrectCol(x)] != x){
                if(tiles[getCorrectRow(x)][getCorrectCol(x)] == BLANK) {
                    wrongRow = N-1;
                    wrongCol = N-1;
                    actual = tiles[N-1][N-1];
                }
                else{
                    wrongRow = getCorrectRow(x);
                    wrongCol = getCorrectCol(x);
                    actual = tiles[getCorrectRow(x)][getCorrectCol(x)];
                }
                correctRow = getCorrectRow(actual);
                correctCol = getCorrectCol(actual);
                manhattan += Math.abs(correctRow - wrongRow) + Math.abs(correctCol - wrongCol);
            }
        }
        return manhattan;
    }

    public int estimatedDistanceToGoal(){
        return manhattan();
    }

    public boolean equals(Object y){
        if(y == this){
            return true;
        }
        if(y == null){
            return false;
        }
        if(getClass() != y.getClass()){
            return false;
        }

        Board other = (Board) y;
        if(size() != other.size()){
            return false;
        }
        for(int i = 0;i < N; i++){
            for(int j = 0;j < N; j++){
                if(tileAt(i,j) != other.tileAt(i,j)){
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode(){
        int hashCode = 0;
        for(int i = 0;i < N; i++){
            for(int j = 0;j < N; j++){
                hashCode += tileAt(i,j) * 17;
            }
        }
        return hashCode;
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

    private int getCorrectCol(int target){
        if(target % N == 0){
            return N-1;
        }
        else{
            return (target % N) - 1;
        }
    }

    private int getCorrectRow(int target){
        if(target % N == 0){
            return (target / N) - 1;
        }
        else if(target < N){
            return 0;
        }
        else{
            return target/N;
        }
    }

}
