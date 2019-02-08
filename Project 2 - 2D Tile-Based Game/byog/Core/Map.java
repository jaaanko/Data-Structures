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
        spawnFood(15);
        spawnPlayer();
        spawnPortal();
        spawnExit();

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
    /*Spawns the player in a random room.
        If the player xy positions passed to the constructor are negative, generate
        random positions for the player, since a new game is being started.
        Otherwise, spawn the player in the given non-negative positions.
     */
    private void spawnPlayer(){
        //if(playerX < 0) {
            Room r = rooms.get(rand.nextInt(rooms.size()));
            while(true) {
                playerX = rand.nextInt((r.x + (r.width - 1)) - (r.x + 1)) + (r.x + 1);
                playerY = rand.nextInt(r.y - (r.y - (r.height - 2))) + (r.y - (r.height - 2));
                if (world[playerX][playerY] != Tileset.MOUNTAIN && world[playerX][playerY] != Tileset.FLOWER && world[playerX][playerY] != Tileset.GRASS) {
                    break;
                }
            }
        //}
        world[playerX][playerY] = Tileset.PLAYER;
    }

    private void spawnPortal(){
        Random rand = new Random(seed%5);
        Room r = rooms.get(rand.nextInt(rooms.size()));
        while(true) {
            int portalX = rand.nextInt((r.x + (r.width - 1)) - (r.x + 1)) + (r.x + 1);
            int portalY = rand.nextInt(r.y - (r.y - (r.height - 2))) + (r.y - (r.height - 2));
            if (world[portalX][portalY] != Tileset.PLAYER && world[portalX][portalY] != Tileset.FLOWER && world[portalX][portalY] != Tileset.GRASS) {
                world[portalX][portalY] = Tileset.MOUNTAIN;
                break;
            }
        }
    }

    private void spawnExit(){
        Random rand = new Random(seed%3);
        Room r = rooms.get(rand.nextInt(rooms.size()));
        while(true) {
            int exitX = rand.nextInt((r.x + (r.width - 1)) - (r.x + 1)) + (r.x + 1);
            int exitY = rand.nextInt(r.y - (r.y - (r.height - 2))) + (r.y - (r.height - 2));
            if (world[exitX][exitY] != Tileset.PLAYER && world[exitX][exitY] != Tileset.MOUNTAIN && world[exitX][exitY] != Tileset.GRASS) {
                world[exitX][exitY] = Tileset.FLOWER;
                break;
            }
        }
    }

    private void spawnFood(int n){
        Random rand = new Random(seed);
        int x;
        int y;
        for(int i=0; i<n; i++){
            while(true) {
                x = rand.nextInt(width);
                y = rand.nextInt(height);
                if (world[x][y] != Tileset.WALL && world[x][y] != Tileset.NOTHING) {
                    world[x][y] = Tileset.GRASS;
                    break;
                }
            }
        }
    }

}
