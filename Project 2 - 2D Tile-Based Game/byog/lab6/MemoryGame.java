package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n; i++){
            sb.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        Font font;
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        if(!gameOver){
            font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.line(0,height-2,width,height-2);
            StdDraw.text(3,height-1,"Round: " + round);
            if(!playerTurn) {
                StdDraw.text(width/2, height - 1, "Watch!");
            }
            else{
                StdDraw.text(width/2, height - 1, "Type!");
            }
            Random randmsg = new Random(round);
            StdDraw.textRight(width-1,height-1,ENCOURAGEMENT[randmsg.nextInt(ENCOURAGEMENT.length)]);
        }

        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(width/2,height/2,s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        char[] chars = letters.toCharArray();
        for(char c : chars) {
            drawFrame(Character.toString(c));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder input = new StringBuilder("");
        int count = 0;
        while(count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                input.append(key);
                drawFrame(input.toString());
                count++;
            }
        }
        return input.toString();
    }

    public void startGame() {
        round = 1;
        gameOver = false;
        playerTurn = false;
        String randomString;
        while(!gameOver) {
            drawFrame("Round: " + round);
            StdDraw.pause(1000);
            randomString = generateRandomString(round);
            flashSequence(randomString);
            playerTurn = true;
            if(solicitNCharsInput(round).equals(randomString)){
                StdDraw.pause(1000);
                round++;
            }
            else{
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
                StdDraw.pause(1000);
            }
            playerTurn = false;
        }
    }

}
