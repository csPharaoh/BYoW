package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;


import byow.lab12.myWorldGenerator;     // my own import
import byow.lab13.MemoryGame;

import java.io.FileNotFoundException;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 95;
    public static final int HEIGHT = 45;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public static void main(String[] args)  throws FileNotFoundException
    {
        Engine e = new Engine();
        TETile[][] newGeneratedWorld = e.interactWithInputString("N21644673SDDWW:Q");
        newGeneratedWorld = e.interactWithInputString("LAAAA:Q");

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(newGeneratedWorld);
    }

    public void interactWithKeyboard() throws FileNotFoundException
    {
        MemoryGame game = new MemoryGame(90, 48, 10);
        game.startGame();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) throws FileNotFoundException {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        /* ---------------------------- Phase 1 ------------------------------------------------------------------------
        String userSSeed = input.substring(1, input.length() - 1);
        long userIntSeed = Long.parseLong(userSSeed);
        myWorldGenerator wG = new myWorldGenerator(userIntSeed);
        TETile[][] finalWorldFrame = wG.generateWorld();
        --------------------------------------------------------------------------------------------------------------*/

        String seed = "";
        String directions = "";
        myWorldGenerator myWG = null;
        boolean hasS = false;

        for (int i = 0; i < input.length() ; i++)
        {
            if(Character.isDigit(input.charAt(i)))
                seed = seed+input.charAt(i);

            if((input.charAt(i) == 's' || input.charAt(i) == 'S') && !hasS)
            {
                hasS = true;
            }

            else if(input.charAt(i) != 'N' && input.charAt(i) != 'n' && hasS)
                directions = directions + input.charAt(i);
        }

        if(input.charAt(0) == 'L')
            myWG = myWorldGenerator.loadState(null,input.substring(0,input.length()),false);

        else
            myWG = myWorldGenerator.loadState(seed,directions,true);

        return myWG.getTiles();
    }
}
