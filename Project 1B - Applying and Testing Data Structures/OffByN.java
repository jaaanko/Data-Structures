public class OffByN implements CharacterComparator{
    int N;
    public OffByN(int N){
        this.N = Math.abs(N);
    }
    public boolean equalChars(char x, char y){
        return Math.abs(x - y) == N;
    }
}
