public class OffByN implements CharacterComparator{
    int N;
    public OffByN(int N){
        this.N = Math.abs(N);
    }
    public boolean equalChars(char x, char y){
        if(Math.abs(x - y) == N){
            return true;
        }
        return false;
    }
}
