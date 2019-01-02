package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    public static void addHexagon(TETile[][] world, int x, int y, TETile tile, int size){
        int[] rowLengths = getRowLengths(size);
        //draws the lower half of the hexagon
        for(int i = 0; i < rowLengths.length / 2; i++) {
            for(int j = 0; j < rowLengths[i]; j++){
                world[x + j][y] = tile;
            }
            x --;
            y ++;
        }
        x ++;
        //draws the upper half of the hexagon
        for(int i = rowLengths.length / 2; i < rowLengths.length; i++){
            for(int j = 0; j < rowLengths[i]; j++){
                world[x + j][y] = tile;
            }
            x ++;
            y ++;
        }
    }

    private static int[] getRowLengths(int size){
        int[] rowLengths = new int[size*2];
        rowLengths[0] = size;
        //generates the lengths of the lower half rows of the hexagon
        for (int i = 1;i < size; i++){
            rowLengths[i] = rowLengths[i - 1] + 2;
        }
        rowLengths[size] = rowLengths[size - 1];
        //generates the lengths of the upper half rows of the hexagon
        for(int j = size + 1; j < rowLengths.length; j++){
            rowLengths[j] = rowLengths[j - 1] - 2;
        }
        return rowLengths;
    }

    private static void fillWorld(TETile[][] world){
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWorld(world);
        addHexagon(world,20,20,Tileset.GRASS,2);
        addHexagon(world,10,10,Tileset.WATER,4);
        ter.renderFrame(world);
    }

}
