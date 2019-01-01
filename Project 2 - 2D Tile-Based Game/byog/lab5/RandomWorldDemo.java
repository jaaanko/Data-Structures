package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world that contains RANDOM tiles.
 */
public class RandomWorldDemo {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     * @param tiles
     */
    public static void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = randomTile();
            }
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.NOTHING;
            default: return Tileset.NOTHING;
        }
    }

    public static void addHexagon(TETile[][] world, int x, int y, TETile tile, int size){
        int[] rowLengths = getRowLengths(size);
        drawHexagonRows(world,x,y,tile,rowLengths);
    }

    private static void drawHexagonRows(TETile[][] world, int x, int y, TETile tile, int[] rowLengths){
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

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        //TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        //fillWithRandomTiles(randomTiles);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWorld(world);
        addHexagon(world,20,20,Tileset.GRASS,2);
        ter.renderFrame(world);
    }

}
