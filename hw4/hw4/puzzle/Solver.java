package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    public MinPQ<SearchNode> minpq = new MinPQ<>();
    public List<WorldState> result = new ArrayList<>();

    public Solver(WorldState initial){
        minpq.insert(new SearchNode(initial,0,null));
        SearchNode x;

        while(!minpq.min().w.isGoal()){
            x = minpq.delMin();
            for(WorldState w : x.w.neighbors()){
                if(x.prev != null) {
                    if (!w.equals(x.prev.w)) {
                        minpq.insert(new SearchNode(w, x.moves + 1, x));
                    }
                }
                else{
                    minpq.insert(new SearchNode(w, x.moves + 1, x));
                }
            }
        }
        SearchNode goal = minpq.delMin();
        while(goal != null){
            result.add(goal.w);
            goal = goal.prev;
        }
        Collections.reverse(result);
    }

    public int moves(){
        return result.size() - 1;
    }

    public Iterable<WorldState> solution(){
        return result;
    }

    private class SearchNode implements Comparable<SearchNode>{
        private WorldState w;
        private int moves;
        private SearchNode prev;
        private int estimatedCost;

        public SearchNode(WorldState w, int moves, SearchNode prev){
            this.w = w;
            this.moves = moves;
            this.prev = prev;
            this.estimatedCost = moves + w.estimatedDistanceToGoal();
        }

        public int compareTo(SearchNode s){
            return estimatedCost - s.estimatedCost;
        }
    }
}
