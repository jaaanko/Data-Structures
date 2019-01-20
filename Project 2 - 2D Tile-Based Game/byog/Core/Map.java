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
    private final static int MAXROOMS = 15;
    private final static int MINROOMS = 10;
    private List<Room> rooms = new ArrayList<>();
    private long seed;
    int playerX;
    int playerY;

    Map(int width, int height, long seed){
        this.width = width;
        this.height = height;
        world = new TETile[width][height];
        this.seed = seed;
        rand = new Random(seed);
    }

    TETile[][] generate(){
        fill();
        createRooms(rand.nextInt((MAXROOMS - MINROOMS) + 1) + MINROOMS);
        spawnPlayer();
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
            rWidth = rand.nextInt(Room.MAXROOMWIDTH - 3) + 3;
            rHeight = rand.nextInt(Room.MAXROOMHEIGHT - 3) + 3;

            Room r = new Room(world, seed, rWidth, rHeight, rX, rY);
            /* Check if the new room overlaps with any existing rooms.
               If it does, generate new coordinates for the room and loop through the list of rooms again.
               If there is no overlap, add the new room to the rooms list and draw it on the map
             */
            for(int j=0; j<rooms.size(); j++) {
                while (r.isOverlap(rooms.get(j))){
                    rX = rand.nextInt(width - Room.MAXROOMWIDTH);
                    rY = rand.nextInt(height - Room.MAXROOMHEIGHT) + Room.MAXROOMHEIGHT;
                    r = new Room(world, seed, rWidth, rHeight, rX, rY);
                    j = 0;
                }
            }
            rooms.add(r);
            r.draw();
        }
        for(int i=0; i<rooms.size()-1; i++){
            rooms.get(i).connectTo(rooms.get(i+1));
        }
    }
    //Spawns the player in a random room
    private void spawnPlayer(){
        Room r = rooms.get(rand.nextInt(rooms.size()));
        playerX = rand.nextInt((r.x + (r.width-1)) - (r.x+1)) + (r.x+1);
        playerY = rand.nextInt(r.y - (r.y - (r.height-2))) + (r.y - (r.height-2));
        world[playerX][playerY] = Tileset.PLAYER;
    }
}
