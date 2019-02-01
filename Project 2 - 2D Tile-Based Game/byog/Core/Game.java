package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    static final int WIDTH = 70;
    static final int HEIGHT = 40;
    static final int MIDHEIGHT = HEIGHT/2;
    static final int MIDWIDTH = WIDTH/2;
    TETile[][] world;
    boolean gameOver;
    int playerX;
    int playerY;
    String saveFile = "byog/Core/Save.txt";
    long seed;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        //Add 2 to height for HUD space
        StdDraw.setCanvasSize(WIDTH*16, (HEIGHT+2)*16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT+2);

        drawMenu();
        char input = readMenuInput();

        if(input == 'N'){
            //Get seed and start game
            seed = getSeedInput();
            startGame(seed);
        }
        else if(input == 'L') {
            //Load saved game by reading the saved seed and player position from a text file
            try {
                File f = new File(saveFile);
                System.out.println(f.getAbsolutePath());
                FileReader r = new FileReader(f);
                BufferedReader b = new BufferedReader(r);
                seed = Long.parseLong(b.readLine());
                playerX = Integer.parseInt(b.readLine());
                playerY = Integer.parseInt(b.readLine());
                b.close();

                startGame(seed,playerX,playerY);
            }
            catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }
    //This method is called when you want to start a new game
    private void startGame(long seed){
        startGame(seed,-1,-1);
    }
    //This method is called when you want to load an existing save
    private void startGame(long seed, int pX, int pY){
        Map m = new Map(WIDTH,HEIGHT,seed,pX,pY);
        ter.initialize(WIDTH, HEIGHT+2);

        world = m.generate();
        playerX = m.playerX;
        playerY = m.playerY;

        ter.renderFrame(world);

        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smallFont);

        double initialX = 0;
        double initialY = 0;
        double mousePosX;
        double mousePosY;

        while(!gameOver){
            mousePosX = StdDraw.mouseX();
            mousePosY = StdDraw.mouseY();
            if((mousePosX < WIDTH && mousePosY < HEIGHT) && (mousePosX != initialX || mousePosY != initialY)) {

                TETile t = world[(int)mousePosX][(int)mousePosY];

                StdDraw.pause(50);
                ter.renderFrame(world);

                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.textRight(20, HEIGHT + 1, t.description());

                StdDraw.show();

                initialX = mousePosX;
                initialY = mousePosY;
            }
            if(StdDraw.hasNextKeyTyped()){
                parseInput(StdDraw.nextKeyTyped());
            }
        }
    }

    private void parseInput(char input){
        if(input == 'w'){
            if(!(world[playerX][playerY+1].equals(Tileset.WALL))){
                world[playerX][playerY+1] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerY ++;
            }
        }
        else if(input == 'a'){
            if(!(world[playerX-1][playerY].equals(Tileset.WALL))){
                world[playerX-1][playerY] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerX --;
            }
        }
        else if(input == 's'){
            if(!(world[playerX][playerY-1].equals(Tileset.WALL))){
                world[playerX][playerY-1] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerY --;
            }
        }
        else if(input == 'd'){
            if(!(world[playerX+1][playerY].equals(Tileset.WALL))){
                world[playerX+1][playerY] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerX ++;
            }
        }
        else if(input == ':'){
            while(true) {
                if (StdDraw.hasNextKeyTyped()) {
                    if (Character.toUpperCase(StdDraw.nextKeyTyped()) == 'Q') {
                        try {
                            File file = new File(saveFile);
                            FileWriter fw = new FileWriter(file, false);
                            BufferedWriter bw = new BufferedWriter(fw);

                            bw.write(Long.toString(seed));
                            bw.newLine();
                            bw.write(Integer.toString(playerX));
                            bw.newLine();
                            bw.write(Integer.toString(playerY));
                            bw.close();

                            System.exit(0);
                        }
                        catch(IOException e){
                            System.err.println(e.getMessage());
                        }
                    }
                    else{
                        break;
                    }
                }
            }
        }
        ter.renderFrame(world);

        int mousePosX = (int)StdDraw.mouseX();
        int mousePosY = (int)StdDraw.mouseY();
        TETile t;
        if(mousePosX < WIDTH && mousePosY < HEIGHT) {
            t = world[mousePosX][mousePosY];

            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textRight(20, HEIGHT + 1, t.description());
        }
        StdDraw.show();
    }

    private void drawMenu(int x, int y, String s){
        StdDraw.clear(Color.BLACK);
        Font titleFont = new Font("Monaco", Font.BOLD, 30);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);

        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MIDWIDTH,MIDHEIGHT+10,"GAME (Temporary Name)");

        StdDraw.setFont(smallFont);
        StdDraw.text(MIDWIDTH,MIDHEIGHT,"New Game (N)");
        StdDraw.text(MIDWIDTH,MIDHEIGHT-3,"Load Game(L)");
        StdDraw.text(MIDWIDTH,MIDHEIGHT-6,"Quit (Q)");

        StdDraw.text(x,y,s);

        StdDraw.show();
    }

    private void drawMenu(){
        drawMenu(0,0,"");
    }

    private char readMenuInput(){
        In in = new In(saveFile);

        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (Character.toUpperCase(input) == 'N') {
                    drawMenu(MIDWIDTH, MIDHEIGHT - 10, "Enter Seed: ");
                    return 'N';
                }
                else if (Character.toUpperCase(input) == 'L' && !in.isEmpty()) {
                    drawMenu(MIDWIDTH, MIDHEIGHT - 10, "Loading...");
                    StdDraw.pause(400);
                    return 'L';
                }
                else if (Character.toUpperCase(input) == 'Q') {
                    System.exit(0);
                }
            }
        }
    }

    private int getSeedInput(){
        char c;
        StringBuilder sb = new StringBuilder();
        while(true){
            if(StdDraw.hasNextKeyTyped()){
                c = StdDraw.nextKeyTyped();
                if(Character.isDigit(c)){
                    sb.append(c);
                    drawMenu(MIDWIDTH, MIDHEIGHT - 10, "Enter Seed: " + sb);
                }
                else if(Character.toUpperCase(c) == 'S' && sb.length() > 0){
                    break;
                }
            }
        }
        System.out.println(sb);
        return Integer.parseInt(sb.toString());
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        if(Character.toUpperCase(input.charAt(0)) == 'N'){
            if(Character.isDigit(input.charAt(1))){
                if(getStopIndex(input) > 0) {
                    int seed = Integer.parseInt(input.substring(1, getStopIndex(input)));
                    Map m = new Map(WIDTH, HEIGHT, seed,-1,-1);
                    return m.generate();
                }
            }
        }
        else if(Character.toUpperCase(input.charAt(0)) == 'L'){
            System.out.println("LOAD");
        }
        else if(Character.toUpperCase(input.charAt(0)) == 'Q'){
            System.out.println("QUIT");
            System.exit(0);
        }
        return null;
    }

    private int getStopIndex(String input){
        int i;
        boolean found = false;
        for(i=0; i<input.length();i++){
            if(Character.toUpperCase(input.charAt(i)) == 'S'){
                found = true;
                break;
            }
        }
        if(!found){
            return -1;
        }
        return i;
    }

}
