package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;

    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        MinPQ<Node> minPQ = new MinPQ<>();
        Node v = new Node(s);
        minPQ.insert(v);
        marked[v.value] = true;
        announce();

        while(minPQ.min().h != 0){
            v = minPQ.delMin();

            for(int w : maze.adj(v.value)){
                if(!marked[w]){
                    marked[w] = true;
                    edgeTo[w] = v.value;
                    distTo[w] = distTo[v.value] + 1;
                    minPQ.insert(new Node(w));
                    announce();
                }
            }
        }

    }

    @Override
    public void solve() {
        astar(s);
    }

    private class Node implements Comparable<Node>{
        private int value;
        private int h;

        public Node(int value){
            this.value = value;
            this.h = h(value);
        }

        public int compareTo(Node n){
            return h - n.h;
        }
    }

}

