package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final Random RANDOM = new Random();

    private static void fillWorld(TETile[][] world){
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
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

    public static void addHexagons(TETile[][] world, int x, int y, int size){
        for(int i =0; i<3; i++) {
            drawRowOfHexes(world, x, y, size,5);
            y += size *2;
        }
        drawRowOfHexes(world, x, y, size,3);
        y += size *2;
        drawRowOfHexes(world, x, y, size,1);
    }

    private static int getRightXPos(int x, int size){
        int rightXPos;
        rightXPos = x + size + 2;
        return rightXPos;
    }

    private static int getRightYPos(int y, int size){
        int rightYPos;
        rightYPos = y + size;
        return rightYPos;
    }

    private static int getLeftYPos(int y, int size){
        int getLeftYPos;
        getLeftYPos = y + size;
        return getLeftYPos;
    }

    private static int getLeftXPos(int x, int size){
        int getLeftXPos;
        getLeftXPos = x - size - 2;
        return getLeftXPos;
    }

    private static void drawRowOfHexes(TETile[][] world, int x, int y, int size, int length){
        addHexagon(world,x,y,randomTile(),size);
        length -= 1;
        int tempX = x;
        int tempY = y;
        for(int i =0; i<length/2; i++) {
            tempX = getRightXPos(tempX, size);
            tempY = getRightYPos(tempY, size);
            addHexagon(world, tempX, tempY, randomTile(), size);
        }
        tempX = x;
        tempY = y;
        for(int i= 0;i<length/2;i++){
            tempX = getLeftXPos(tempX, size);
            tempY = getLeftYPos(tempY, size);
            addHexagon(world, tempX, tempY, randomTile(), size);
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.WATER;
            case 3: return Tileset.SAND;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }
    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWorld(world);
        //addHexagon(world,20,20,Tileset.GRASS,2);
        addHexagons(world,30,5,3);
        ter.renderFrame(world);
    }

}
