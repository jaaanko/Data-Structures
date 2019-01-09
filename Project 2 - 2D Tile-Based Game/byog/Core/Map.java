package byog.Core;
import byog.TileEngine.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Map {
    private int width;
    private int height;
    private TETile[][] world;
    private Random rand;
    private int roomCount = 15;
    static List<Room> rooms = new ArrayList<>();

    Map(int width, int height, long seed){
        this.width = width;
        this.height = height;
        world = new TETile[width][height];
        rand = new Random(seed);
    }

    TETile[][] generate(){
        fill();
        createRooms(roomCount);
        return world;
    }

    private void fill(){
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void createRooms(int count){
        int rX;
        int rY;
        int rWidth;
        int rHeight;

        for(int i=0; i<count; i++) {
            rX = rand.nextInt(width - Room.MAXROOMWIDTH);
            rY = rand.nextInt(height - Room.MAXROOMHEIGHT) + Room.MAXROOMHEIGHT;
            rWidth = rand.nextInt(Room.MAXROOMWIDTH - 2) + 2;
            rHeight = rand.nextInt(Room.MAXROOMHEIGHT - 2) + 2;

            Room r = new Room(world, rWidth, rHeight, rX, rY);

            for(int j=0; j<rooms.size(); j++) {
                while (r.isOverlap(rooms.get(j))){
                    rX = rand.nextInt(width - Room.MAXROOMWIDTH);
                    rY = rand.nextInt(height - Room.MAXROOMHEIGHT) + Room.MAXROOMHEIGHT;
                    r = new Room(world, rWidth, rHeight, rX, rY);
                    j = 0;
                }
            }
            rooms.add(r);
            r.draw();
        }
    }
}