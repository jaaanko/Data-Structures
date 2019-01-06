package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

class Room {
    private int width;
    private int height;
    static final int MAXROOMWIDTH = 9;
    static final int MAXROOMHEIGHT = 10;
    //the x and y values mark the location of the top corner of the room
    private int x;
    private int y;
    private TETile[][] world;

    Room(TETile[][] world, int width, int height, int x, int y){
        //increment width and height to account for the walls
        this.width = width+2;
        this.height = height+2;
        this.world = world;
        this.x = x;
        this.y = y;
    }

    void draw(){
        drawWalls();
        //draw the floor
        for(int i=0; i<height-2; i++){
            drawRow(x+1,y-(i+1),width-2, Tileset.FLOOR);
        }
    }

    private void drawWalls(){
        TETile t = Tileset.WALL;
        //draw top wall
        drawRow(x,y,width,t);
        //draw left wall
        drawColumn(x,y-1,height-1,t);
        //draw bottom wall
        drawRow(x+1,y-(height-1),width-1,t);
        //draw right wall
        drawColumn(x+(width-1),y-1,height-1,t);
    }

    //draws a row from left to right
    private void drawRow(int x, int y, int length, TETile t){
        int startPos = x;
        for(int i=0; i<length; i++){
            world[startPos][y] = t;
            startPos ++;
        }
    }
    //draws a column from top to bottom
    private void drawColumn(int x, int y, int length, TETile t){
        int startPos = y;
        for(int i=0; i<length; i++){
            world[x][startPos] = t;
            startPos --;
        }
    }


}
