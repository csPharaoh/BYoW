package byow.lab13;

import byow.lab12.myWorldGenerator;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.util.Random;

public class MemoryGame {
    private String decision = "";
    private String avatarName = "";
    private String seed = "";
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

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(80, 45, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
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

        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n

        char[] randString = new char[n];
        for (int i = 0; i < n; i++)
        {
            randString[i] = CHARACTERS[rand.nextInt(CHARACTERS.length)];
        }
        return new String(randString);
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen

        StdDraw.clear(Color.black);

        Font font1 = new Font("Arial", Font.BOLD, 60);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(40,43, "CS 61B : The Game");

        Font font2 = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font2);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(42,12, "Watch last game  : (R)");
        StdDraw.text(42,9, "New Game  : (N)");
        StdDraw.text(42,6, "Load Game : (L)");
        StdDraw.text(42,3, "Quit Game : (Q)");
        StdDraw.show();

    }

    public void flashSequence(String letters) throws FileNotFoundException {
        //TODO: Display each character in letters, making sure to blank the screen between letters

        int i = 48;
        int j = 52;

        while(true)
        {
            if(StdDraw.hasNextKeyTyped())
            {
                char typed = StdDraw.nextKeyTyped();

                if (typed == 'N')
                {
                    Font font = new Font("Arial", Font.BOLD, 30);
                    StdDraw.setFont(font);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(42,17, "Enter Seed :");
                    StdDraw.text(42,19, "Enter Avatar Name :");
                    StdDraw.pause(20);
                    StdDraw.show();
                }

                else if (typed == 'Q')
                {
                    System.exit(0);
                }

                else if (typed == 'L')
                {
                    myWorldGenerator.loadGame(0, "L", avatarName);
                }

                else if (typed == 'R')
                {
                    myWorldGenerator.replayGame();
                }

                if(!Character.isDigit(typed) && typed != 'N' && typed != 'Q' && typed != 'S')
                {
                    avatarName = avatarName +typed;

                    StdDraw.text(j, 19, ""+typed);
                    StdDraw.pause(20);
                    StdDraw.show();
                    j = j + 1;
                }

                else if(Character.isDigit(typed))
                {
                    seed = seed + typed;
                    StdDraw.text(i, 17, "" + typed);
                    StdDraw.pause(20);
                    StdDraw.show();
                    i = i + 1;
                }
                else if (typed == 'S')
                {
                    myWorldGenerator.loadGame(Long.parseLong(seed), "S", avatarName);
                }


            }
        }

    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        // my seed is 21644673
        return decision;
    }

    private String getSeed()
    {
        System.out.println(seed);
        return seed;
    }

    public void startGame() throws FileNotFoundException {
        //TODO: Set any relevant variables before the game starts
        String randString = generateRandomString(5);
        drawFrame(randString);
        flashSequence(randString);


        //TODO: Establish Engine loop
    }

}
