package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld
{
    //private static final int WIDTH = 60;
    //private static final int HEIGHT = 30;

    public static void main(String[] args)
    {

        int hexSize = 15;
        TERenderer ter = new TERenderer();
        ter.initialize(60, 60);
        ter.renderFrame(addHexagon(hexSize));
        //addHexagon(4);
    }

    private static int getHeight(int size)
    {
        return  2*size;
    }

     private static int getWidth(int size)
     {
         return  2*(size - 1)+ size;
     }

     private static TETile[][] addHexagon(int len)
     {
         int max = 2*(len - 1)+ len;
         int height = 2*len;
         int width = max;
         int varLen = len;

         TETile[][] world = new TETile[height][width];

         for(int j = 0; j < height; j++)
         {
             for (int i = 0; i < max; i++)
             {
                 if (i < (max - varLen) / 2 || i >= ((max - varLen) / 2) + varLen)
                 {
                     world[j][i] = Tileset.NOTHING;
                     System.out.print(" ");
                 }
                 else
                 {
                     world[j][i] = Tileset.FLOWER;
                     System.out.print("*");
                 }
             }
             System.out.println();

             if (j == height/2 -1)
             {

             }
             else if(j < height/2)
                varLen += 2;
             else
                 varLen -= 2;
         }
            return world;
     }

}
