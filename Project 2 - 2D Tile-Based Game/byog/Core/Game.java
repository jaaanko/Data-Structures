package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 40;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
                    TERenderer ter = new TERenderer();
                    ter.initialize(WIDTH, HEIGHT);
                    int seed = Integer.parseInt(input.substring(1, getStopIndex(input)));
                    Map m = new Map(WIDTH, HEIGHT, seed);
                    TETile[][] world = m.generate();
                    ter.renderFrame(world);
                    return world;
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
