package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

class Game implements Serializable{
    private TERenderer ter = new TERenderer();
    private static final int WIDTH = 70;
    private static final int HEIGHT = 40;
    private static final int MIDHEIGHT = HEIGHT/2;
    private static final int MIDWIDTH = WIDTH/2;
    private TETile[][] world;
    private boolean endGame;
    private int playerX;
    private int playerY;
    private String worldSaveFile = "byog/Core/WorldState.txt";
    private long seed;
    private String inputString = "";
    private int maxHP = 10;
    private int currentHP = 10;
    private int stepCount = 0;
    private int hungerRate = 5;
    private int gameResult = 0;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        //Add 2 to height for HUD space
        StdDraw.setCanvasSize(WIDTH*16, (HEIGHT+2)*16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT+2);
        StdDraw.clear(Color.BLACK);

        StdDraw.enableDoubleBuffering();

        drawMenu();
        char input = readMenuInput();
        inputString += input;

        if(input == 'N'){
            //Get seed and start game
            seed = getSeedInput();
            inputString = inputString + Long.toString(seed) + 'S';
            //System.out.println(seed);
            initialize(seed);
        }
        else if(input == 'L') {
            //Load saved game by reading the saved seed and player position from a text file
            load();
        }
        System.out.println(inputString);
        if(gameResult > 0){
            finishScreen('w');
        }
        else if(gameResult < 0){
            finishScreen('l');
        }
        System.exit(0);
    }


    private void initialize(long seed){
        Map m = new Map(WIDTH,HEIGHT,seed);
        ter.initialize(WIDTH, HEIGHT+2);

        world = m.generate();
        playerX = m.playerX;
        playerY = m.playerY;

        startGame(world);
    }

    private void load(){
        try {
            FileInputStream fi = new FileInputStream(new File(worldSaveFile));
            ObjectInputStream oi = new ObjectInputStream(fi);

            ArrayList<Object> woi = (ArrayList<Object>) oi.readObject();

            Game g = (Game) woi.get(0);
            world = (TETile[][]) woi.get(1);
            this.seed = g.seed;
            this.playerX = g.playerX;
            this.playerY = g.playerY;
            this.currentHP = g.currentHP;

            oi.close();
            fi.close();
            startGame(world);
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void save(){
        ArrayList<Object> woi = new ArrayList<>();
        woi.add(this);
        woi.add(world);
        try {
            FileOutputStream f = new FileOutputStream(new File(worldSaveFile));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(woi);
            o.close();
            f.close();
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
    //This method is called when you want to load an existing save
    private void startGame(TETile[][] world){

        ter.renderFrame(world);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smallFont);

        StdDraw.text(WIDTH-12, HEIGHT + 1, "Every "+ hungerRate +" steps, you will get hungrier. Good luck!");
        StdDraw.text(10, HEIGHT + 1, "Hunger Points:  " + Integer.toString(currentHP) + "/" + Integer.toString(maxHP));
        StdDraw.show();
        double initialX = 0;
        double initialY = 0;
        double mousePosX;
        double mousePosY;

        while(true){
            mousePosX = StdDraw.mouseX();
            mousePosY = StdDraw.mouseY();
            if((mousePosX < WIDTH && mousePosY < HEIGHT) && (mousePosX != initialX || mousePosY != initialY)) {
                TETile t = world[(int)mousePosX][(int)mousePosY];

                StdDraw.pause(50);

                ter.renderFrame(world);

                StdDraw.setPenColor(StdDraw.WHITE);
                updateHUD(t.description());
                StdDraw.show();

                initialX = mousePosX;
                initialY = mousePosY;
            }

            if(StdDraw.hasNextKeyTyped()){
                parseInput(StdDraw.nextKeyTyped());
                if(endGame){
                    return;
                }
                StdDraw.clear(Color.BLACK);
                ter.renderFrame(world);

                mousePosX = StdDraw.mouseX();
                mousePosY = StdDraw.mouseY();
                TETile t;

                if(mousePosX < WIDTH && mousePosY < HEIGHT) {
                    t = world[(int)mousePosX][(int)mousePosY];
                    StdDraw.setPenColor(StdDraw.WHITE);
                    updateHUD(t.description());
                }
                StdDraw.show();
            }
        }
    }

    private void parseInput(char input){
        input = Character.toUpperCase(input);
        inputString += input;

        if(input == 'W'){
            if(!(world[playerX][playerY+1].equals(Tileset.WALL))){
                if(world[playerX][playerY+1].equals(Tileset.GRASS)){
                    if(currentHP < maxHP){
                        currentHP++;
                        stepCount = 0;
                    }
                    world[playerX][playerY + 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerY ++;
                }
                else if(world[playerX][playerY+1].equals(Tileset.MOUNTAIN)) {
                    teleportPlayer();
                }
                else if(world[playerX][playerY+1].equals(Tileset.FLOWER)) {
                    world[playerX][playerY + 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    gameResult = 1;
                    endGame = true;
                    return;
                }
                else{
                    world[playerX][playerY + 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerY ++;
                    stepCount++;
                }
            }
            else{
                return;
            }
        }
        else if(input == 'A'){
            if(!(world[playerX-1][playerY].equals(Tileset.WALL))){
                if(world[playerX-1][playerY].equals(Tileset.GRASS)){
                    if(currentHP < maxHP){
                        currentHP++;
                        stepCount = 0;
                    }
                    world[playerX - 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerX--;
                }
                else if(world[playerX-1][playerY].equals(Tileset.MOUNTAIN)) {
                    teleportPlayer();
                }
                else if(world[playerX-1][playerY].equals(Tileset.FLOWER)) {
                    world[playerX - 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    gameResult = 1;
                    endGame = true;
                    return;
                }
                else {
                    world[playerX - 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerX--;
                    stepCount++;
                }
            }
            else{
                return;
            }
        }
        else if(input == 'S'){
            if(!(world[playerX][playerY-1].equals(Tileset.WALL))){
                if(world[playerX][playerY-1].equals(Tileset.GRASS)){
                    if(currentHP < maxHP){
                        currentHP++;
                        stepCount = 0;
                    }
                    world[playerX][playerY - 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerY--;
                }
                else if(world[playerX][playerY-1].equals(Tileset.MOUNTAIN)) {
                    teleportPlayer();
                }
                else if(world[playerX][playerY-1].equals(Tileset.FLOWER)) {
                    world[playerX][playerY - 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    gameResult = 1;
                    endGame = true;
                    return;
                }
                else {
                    world[playerX][playerY - 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerY--;
                    stepCount++;
                }
            }
            else{
                return;
            }
        }
        else if(input == 'D'){
            if(!(world[playerX+1][playerY].equals(Tileset.WALL))){
                if(world[playerX+1][playerY].equals(Tileset.GRASS)){
                    if(currentHP < maxHP){
                        currentHP++;
                        stepCount = 0;
                    }
                    world[playerX + 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerX++;
                }
                else if(world[playerX+1][playerY].equals(Tileset.MOUNTAIN)) {
                    teleportPlayer();
                }
                else if(world[playerX+1][playerY].equals(Tileset.FLOWER)) {
                    world[playerX + 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    gameResult = 1;
                    endGame = true;
                    return;
                }
                else {
                    world[playerX + 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerX++;
                    stepCount++;
                }
            }
            else{
                return;
            }
        }
        else if(input == 'Q'){
            if(inputString.charAt(inputString.length()-2) == ':'){
                save();
                endGame = true;
                return;
            }
        }
        else{
            return;
        }
        //Reduce hunger points by 1 for every 4 steps taken.
        if(stepCount % hungerRate == 0){
            if(currentHP != 0) {
                currentHP--;
            }
        }
        if(currentHP == 0){
            gameResult = -1;
            endGame = true;
        }
    }

    private void finishScreen(char c){

        StdDraw.setPenColor(StdDraw.WHITE);
        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        Font titleFont = new Font("Monaco", Font.BOLD, 30);

        StdDraw.setFont(titleFont);

        if(c == 'w'){
            StdDraw.clear(Color.BLACK);
            StdDraw.text(MIDWIDTH,MIDHEIGHT,"CONGRATULATIONS, YOU WON!");
            StdDraw.setFont(smallFont);
            StdDraw.text(MIDWIDTH,MIDHEIGHT-10,"YOU MAY PRESS Q TO EXIT THE GAME.");
            StdDraw.show();
            while(true){
                if(StdDraw.hasNextKeyTyped()){
                    if(Character.toUpperCase(StdDraw.nextKeyTyped()) == 'Q'){
                        return;
                    }
                }
            }
        }
        else{
            StdDraw.clear(Color.BLACK);
            StdDraw.text(MIDWIDTH,MIDHEIGHT,"GAME OVER. BETTER LUCK NEXT TIME.");
            StdDraw.setFont(smallFont);
            StdDraw.text(MIDWIDTH,MIDHEIGHT-10,"YOU MAY PRESS Q TO EXIT THE GAME.");
            StdDraw.show();
            while(true){
                if(StdDraw.hasNextKeyTyped()){
                    if(Character.toUpperCase(StdDraw.nextKeyTyped()) == 'Q'){
                        return;
                    }
                }
            }
        }
    }

    private void teleportPlayer(){
        stepCount++;
        world[playerX][playerY] = Tileset.FLOOR;
        Random rand = new Random();
        int x;
        int y;
        while(true) {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
            if (world[x][y] != Tileset.WALL && world[x][y] != Tileset.NOTHING && world[x][y] != Tileset.MOUNTAIN) {
                if(world[x][y] == Tileset.GRASS){
                    if(currentHP < maxHP){
                        currentHP++;
                    }
                }
                world[x][y] = Tileset.PLAYER;
                playerX = x;
                playerY = y;
                break;
            }
        }
    }

    private void updateHUD(String s){
        StdDraw.text(WIDTH-12, HEIGHT + 1, "Every "+ hungerRate +" steps, you will get hungrier. Good luck!");
        StdDraw.text(MIDWIDTH, HEIGHT + 1, s);
        StdDraw.text(10, HEIGHT + 1, "Hunger Points:  " + Integer.toString(currentHP) + "/" + Integer.toString(maxHP));
    }

    private void drawMenu(int x, int y, String s){
        StdDraw.clear(Color.BLACK);
        Font titleFont = new Font("Monaco", Font.BOLD, 30);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);

        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MIDWIDTH,MIDHEIGHT+10,"RNGame");

        StdDraw.setFont(smallFont);
        StdDraw.text(MIDWIDTH,MIDHEIGHT+5,"~Reach the flower before you starve~");
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
        boolean fileExists = true;
        try {
            FileInputStream fi = new FileInputStream(worldSaveFile);
            fi.close();
        }
        catch (FileNotFoundException f){
            fileExists = false;
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (Character.toUpperCase(input) == 'N') {
                    drawMenu(MIDWIDTH, MIDHEIGHT - 10, "Enter Seed: ");
                    return 'N';
                }
                else if (Character.toUpperCase(input) == 'L' && fileExists) {
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

    private long getSeedInput(){
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
        return Long.parseLong(sb.toString());
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
                int stopIndex = getStopIndex(input);
                if(stopIndex > 0) {
                    seed = Integer.parseInt(input.substring(1, stopIndex));
                    Map m = new Map(WIDTH, HEIGHT, seed);
                    world = m.generate();
                    playerX = m.playerX;
                    playerY = m.playerY;

                    for(int i=stopIndex+1; i< input.length(); i++){
                        parseInput(input.charAt(i));
                        if(endGame){
                            break;
                        }
                    }
                    return world;
                }
            }
        }
        else if(Character.toUpperCase(input.charAt(0)) == 'L'){
            load();
            for(int i=1; i< input.length(); i++){
                parseInput(input.charAt(i));
                if(endGame){
                    break;
                }
            }
            return world;
        }
        else if(Character.toUpperCase(input.charAt(0)) == 'Q'){
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
