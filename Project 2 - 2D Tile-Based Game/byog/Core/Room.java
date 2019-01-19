package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

class Room {
    private int width;
    private int height;
    static final int MAXROOMWIDTH = 9;
    static final int MAXROOMHEIGHT = 10;
    //The x and y values mark the location of the top corner of the room
    private int x;
    private int y;
    private TETile[][] world;
    private long seed;

    Room(TETile[][] world, long seed, int width, int height, int x, int y){
        //Increment width and height to account for the walls
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
        //Draws top wall
        drawRow(x,y,width,t,"right");
        //Draws left wall
        drawColumn(x,y-1,height-1,t,"down");
        //Draws bottom wall
        drawRow(x+1,y-(height-1),width-1,t,"right");
        //Draws right wall
        drawColumn(x+(width-1),y-1,height-1,t,"down");
    }
    //Draws the floor by drawing multiple rows
    private void drawFloor(){
        for(int i=0; i<height-2; i++){
            drawRow(x+1,y-(i+1),width-2, Tileset.FLOOR,"right");
        }
    }
    //Draws a row from left to right or right to left, depending on the input
    private void drawRow(int x, int y, int length, TETile t, String direction){
        int startPos = x;
        if(direction.equals("right")) {
            for (int i = 0; i < length; i++) {
                /* This check (and also the one below) is mainly to ensure that the walls generated during hallway generation
                will not overlap a floor tile at any point.
                 */
                if(!(world[startPos][y].equals(Tileset.FLOOR))) {
                    world[startPos][y] = t;
                }
                startPos++;
            }
        }
        else{
            for (int i = 0; i < length; i++) {
                if(!(world[startPos][y].equals(Tileset.FLOOR))) {
                    world[startPos][y] = t;
                }
                startPos--;
            }
        }
    }
    //Draws a column downwards or upwards, depending on the input
    private void drawColumn(int x, int y, int length, TETile t, String direction){
        int startPos = y;
        if(direction.equals("down")) {
            for (int i = 0; i < length; i++) {
                /* This check (and also the one below) is mainly to ensure that the walls generated during hallway generation
                will not overlap a floor tile at any point.
                 */
                if(!(world[x][startPos].equals(Tileset.FLOOR))) {
                    world[x][startPos] = t;
                }
                startPos--;
            }
        }
        else{
            for (int i = 0; i < length; i++) {
                if(!(world[x][startPos].equals(Tileset.FLOOR))) {
                    world[x][startPos] = t;
                }
                startPos++;
            }
        }
    }
    //Draws a vertical hallway by calling the drawColumn function 3 times
    private void drawVerticalHallway(int x, int y, int length, String direction){
        length = Math.abs(length);
        drawColumn(x+1,y,length+1,Tileset.WALL,direction);
        drawColumn(x,y,length,Tileset.FLOOR,direction);
        drawColumn(x-1,y,length+1,Tileset.WALL,direction);
        //Draws corners
        if(direction.equals("down")) {
            if(!(world[x][y-length].equals(Tileset.FLOOR))) {
                world[x][y - length] = Tileset.WALL;
            }
        }
        else{
            if(!(world[x][y+length].equals(Tileset.FLOOR))) {
                world[x][y + length] = Tileset.WALL;
            }
        }
    }
    //Draws a horizontal hallway by calling the drawRow function 3 times
    private void drawHorizontalHallway(int x, int y, int length, String direction){
        length = Math.abs(length);
        drawRow(x,y+1,length+1,Tileset.WALL,direction);
        drawRow(x,y,length,Tileset.FLOOR,direction);
        drawRow(x,y-1,length+1,Tileset.WALL,direction);
        //Draws corners
        if(direction.equals("left")) {
            if(!(world[x-length][y].equals(Tileset.FLOOR))){
                world[x - length][y] = Tileset.WALL;
            }
        }
        else{
            if(!(world[x+length][y].equals(Tileset.FLOOR))){
                world[x + length][y] = Tileset.WALL;
            }
        }
    }
    /*Draws a hallway that connects this room to the given room
    --More comments coming soon--
     */
    void connectTo(Room r){
        Random rand = new Random(seed);
        int end;
        int length1,length2;
        int yPos1 = rand.nextInt((y-(y-(height-2))))+(y-(height-2));
        int yPos2 = rand.nextInt((r.y-(r.y-(r.height-2))))+(r.y-(r.height-2));
        int xPos1 = rand.nextInt(((x+width-1)-(x)))+(x);
        int xPos2 = rand.nextInt(((r.x+r.width-1)-(r.x)))+(r.x);

        if(x+width < r.x || r.x+r.width < x){
            //If room 1 is to the left of room 2
            if(x-r.x < 0){
                if((r.x) - (x+width+1) <= 0){
                    drawHorizontalHallway(r.x,yPos2,3,"left");
                }
                else {
                    end = rand.nextInt((r.x) - (x + width + 1)) + (x + width + 1);
                    length1 = end - (x + width)+1;
                    length2 = end - r.x;
                    //Draws the vertical connection hallway
                    if(yPos1 > yPos2) {
                        drawVerticalHallway(x + width + length1, yPos1-1,yPos1-yPos2,"down");
                    }
                    else{
                        drawVerticalHallway(x + width + length1, yPos1+1,yPos2-yPos1,"up");
                    }
                    //Draws the hallways that branches off rooms 1 and 2
                    drawHorizontalHallway(x + (width - 1), yPos1, length1+2, "right");
                    drawHorizontalHallway(r.x, yPos2, length2+1, "left");
                }
            }
            //If room 2 is to the left of room 1
            else{
                if((x)-(r.x+r.width+1) <= 0){
                    drawHorizontalHallway(x,yPos1,3,"left");
                }
                else{
                    end = rand.nextInt((x)-(r.x+r.width+1)) + (r.x+r.width+1);
                    length1 = end-(r.x+r.width)+1;
                    length2 = end - x;
                    //Draws the vertical connection hallway
                    if(yPos2 > yPos1) {
                        drawVerticalHallway(r.x + r.width + length1, yPos2-1,yPos2-yPos1,"down");
                    }
                    else{
                        drawVerticalHallway(r.x + r.width + length1, yPos2+1,yPos1-yPos2,"up");
                    }
                    //Draws the hallways that branches off rooms 1 and 2
                    drawHorizontalHallway(x,yPos1,length2+1,"left");
                    drawHorizontalHallway(r.x+(r.width-1),yPos2,length1+2, "right");
                }
            }
        }
        //If one of the rooms is below/above the other one
        else {
            //If room 1 is below room 2
            if (y - r.y < 0) {
                if((r.y-height-1) - (y) <= 0){
                    drawVerticalHallway(xPos2,r.y,3,"up");
                }
                else{
                    end = rand.nextInt((r.y- (r.height - 1)) - (y)) + (y);
                    length1 = end - y;
                    length2 = (r.y - (r.height -1)) - end;
                    //Draws the horizontal connection hallway
                    if(xPos1 > xPos2) {
                        drawHorizontalHallway(xPos1-1,y+length1,xPos1-xPos2,"left");
                    }
                    else{
                        drawHorizontalHallway(xPos1+1,y+length1,xPos2-xPos1,"right");
                    }
                    //Draws the hallways that branches off rooms 1 and 2
                    drawVerticalHallway(xPos1,y,length1+1,"up");
                    drawVerticalHallway(xPos2, r.y - (r.height - 1), length2+2, "down");
                }
            }
            //If room 2 is below room 1
            else {
                if((y-height-1)-(r.y) <= 0){
                    drawVerticalHallway(xPos1,y,3,"up");
                }
                else{
                    end = rand.nextInt((y-(height-1))-(r.y)) + (r.y);
                    length1 = end - r.y;
                    length2 = (y-(height-1))-end;
                    //Draws the horizontal connection hallway
                    if(xPos2 > xPos1) {
                        drawHorizontalHallway(xPos2-1, r.y + length1,xPos2-xPos1,"left");
                    }
                    else{
                        drawHorizontalHallway(xPos2+1, r.y + length1,xPos1-xPos2,"right");
                    }
                    //Draws the hallways that branches off rooms 1 and 2
                    drawVerticalHallway(xPos1,y-(height-1),length2+2,"down");
                    drawVerticalHallway(xPos2,r.y,length1+1, "up");
                }
            }
        }
    }
    //Checks if current room overlaps with the given room
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
