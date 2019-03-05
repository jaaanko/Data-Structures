package hw3.hash;

import edu.princeton.cs.algs4.In;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int bucketNum;
        int length = oomages.size();
        int[] buckets = new int[M];

        for(Oomage o : oomages){
            bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNum] += 1;
        }
        for(int i=0;i<M;i++){
            if(buckets[i] < length/50.0 || buckets[i] > length/2.5){
                return false;
            }
        }
        return true;
    }
}
