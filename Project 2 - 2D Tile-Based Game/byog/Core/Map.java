package byog.Core;
import byog.TileEngine.*;

class Map {

    private int width;
    private int height;
    private TETile[][] world;

    Map(int width, int height){
        this.width = width;
        this.height = height;
        world = new TETile[width][height];
    }

    TETile[][] generate(){
        fill();
        createRooms();
        return world;
    }

    private void fill(){
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void createRooms(){
        Room r = new Room(world,8,7,20,15);
        r.draw();
    }
}
