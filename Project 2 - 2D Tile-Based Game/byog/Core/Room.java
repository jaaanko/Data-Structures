package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

class Room {
    private int width;
    private int height;
    static final int MAXROOMWIDTH = 9;
    static final int MAXROOMHEIGHT = 10;
    //the x and y values mark the location of the top corner of the room
    private int x;
    private int y;
    private TETile[][] world;
    private long seed;

    Room(TETile[][] world, long seed, int width, int height, int x, int y){
        //increment width and height to account for the walls
        this.width = width+2;
        this.height = height+2;
        this.world = world;
        this.x = x;
        this.y = y;
        this.seed = seed;
    }

    void draw(){
        drawWalls();
        drawFloor();
    }

    private void drawWalls(){
        TETile t = Tileset.WALL;
        //draw top wall
        drawRow(x,y,width,t,"right");
        //draw left wall
        drawColumn(x,y-1,height-1,t,"down");
        //draw bottom wall
        drawRow(x+1,y-(height-1),width-1,t,"right");
        //draw right wall
        drawColumn(x+(width-1),y-1,height-1,t,"down");
    }

    private void drawFloor(){
        for(int i=0; i<height-2; i++){
            drawRow(x+1,y-(i+1),width-2, Tileset.FLOOR,"right");
        }
    }

    //draws a row from left to right or right to left, depending on the input
    private void drawRow(int x, int y, int length, TETile t, String direction){
        int startPos = x;
        if(direction.equals("right")) {
            for (int i = 0; i < length; i++) {
                world[startPos][y] = t;
                startPos++;
            }
        }
        else{
            for (int i = 0; i < length; i++) {
                world[startPos][y] = t;
                startPos--;
            }
        }
    }
    //draws a column downwards or upwards, depending on the input
    private void drawColumn(int x, int y, int length, TETile t, String direction){
        int startPos = y;
        if(direction.equals("down")) {
            for (int i = 0; i < length; i++) {
                world[x][startPos] = t;
                startPos--;
            }
        }
        else{
            for (int i = 0; i < length; i++) {
                world[x][startPos] = t;
                startPos++;
            }
        }
    }

    private void drawVerticalHallway(int x, int y, int length, String direction){
        length = Math.abs(length);
        //drawColumn(x+1,y,length,Tileset.WALL,direction);
        drawColumn(x,y,length,Tileset.FLOOR,direction);
        //drawColumn(x-1,y,length,Tileset.WALL,direction);
    }

    private void drawHorizontalHallway(int x, int y, int length, String direction){
        length = Math.abs(length);
        //drawRow(x,y+1,length,Tileset.WALL,direction);
        drawRow(x,y,length,Tileset.FLOOR,direction);
        //drawRow(x,y-1,length,Tileset.WALL,direction);
    }
    //draws a hallway that connects this room to the given room
    void connectTo(Room r){
        Random rand = new Random(seed);
        int end;
        int length1,length2;
        int yPos1 = rand.nextInt((y-(y-(height-2))))+(y-(height-2));
        int yPos2 = rand.nextInt((r.y-(r.y-(r.height-2))))+(r.y-(r.height-2));
        if(x+width < r.x || r.x+r.width < x){
            if(x-r.x < 0){
                if((r.x) - (x+width+1) <= 0){
                    drawHorizontalHallway(r.x,yPos2,3,"left");
                }
                else {
                    end = rand.nextInt((r.x) - (x + width + 1)) + (x + width + 1);
                    length1 = end - (x + width)+1;
                    length2 = end - r.x;
                    if(yPos1 > yPos2) {
                        drawVerticalHallway(x + width + length1, yPos1-1,yPos1-yPos2,"down");
                    }
                    else{
                        drawVerticalHallway(x + width + length1, yPos1+1,yPos2-yPos1,"up");
                    }
                    drawHorizontalHallway(x + width - 1, yPos1, length1+2, "right");
                    drawHorizontalHallway(r.x, yPos2, length2+1, "left");
                }
            }
            else{
                if((x)-(r.x+r.width+1) <= 0){
                    drawHorizontalHallway(x,yPos1,3,"left");
                }
                else{
                    end = rand.nextInt((x)-(r.x+r.width+1)) + (r.x+r.width+1);
                    length1 = end-(r.x+r.width)+1;
                    length2 = end - x;
                    if(yPos2 > yPos1) {
                        drawVerticalHallway(r.x + r.width + length1, yPos2-1,yPos2-yPos1,"down");
                    }
                    else{
                        drawVerticalHallway(r.x + r.width + length1, yPos2+1,yPos1-yPos2,"up");
                    }
                    drawHorizontalHallway(x,yPos1,length2+1,"left");
                    drawHorizontalHallway(r.x+r.width-1,yPos2,length1+2, "right");
                }
            }
        }
    }
    //checks if current room overlaps with the given room
    boolean isOverlap(Room r){
        int tempY = y;
        int tempX;
        for(int i=0; i<height; i++){
            tempX = x;
            for(int j=0; j<width; j++){
                if((r.x <= tempX && tempX <= (r.x + r.width)) && (r.y >= tempY && tempY >= (r.y - r.height))){
                    return true;
                }
                tempX ++;
            }
            tempY --;
        }
        return false;
    }
}
