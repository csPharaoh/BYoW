package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.FileNotFoundException;

public class ThreadForAvatar extends Thread {
    static final int WIDTH = 90;
    static final int HEIGHT = 48;
    static String avatarName = "";
    static myWorldGenerator wG;
    static TETile[][] generatedWorld;
    static Point avatarPos;
    static Point enemyPos;
    static Point exitDoor;
    static String quit = "";
    static char key = ' ';
    static String history = "";
    TERenderer ter = new TERenderer();

    @Override
    public void run()
    {
        try {
            ter.initialize(WIDTH, HEIGHT);
            while (true) {
                ter.renderFrame(generatedWorld);
                wG.setCurrState(generatedWorld, new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY()), exitDoor, avatarName);

                if (StdDraw.hasNextKeyTyped()) {
                    key = StdDraw.nextKeyTyped();
                    history = history + key;

                    if (key == ':' || key == 'Q')
                        quit = quit + key;

                    if (key == 'w') {
                        wG.moveUp(avatarPos, generatedWorld, "AVATAR");
                        wG.moveUp(enemyPos, generatedWorld, "ENEMY");
                        //System.out.println("UP");
                    } else if (key == 'a') {
                        wG.moveLeft(avatarPos, generatedWorld, "AVATAR");
                        wG.moveLeft(enemyPos, generatedWorld, "ENEMY");
                        //System.out.println("LEFT");
                    }

                    if (key == 's') {
                        wG.moveDown(avatarPos, generatedWorld, "AVATAR");
                        wG.moveDown(enemyPos, generatedWorld, "ENEMY");
                        //System.out.println("DOWN");
                    } else if (key == 'd') {
                        wG.moveRight(avatarPos, generatedWorld, "AVATAR");
                        wG.moveRight(enemyPos, generatedWorld, "ENEMY");
                        //System.out.println("RIGHT");
                    } else if (quit.equals(":Q")) {
                        try {
                            wG.saveState(history, wG.SEED);
                            System.exit(0);
                        } catch (FileNotFoundException e) {
                            System.out.println("IO problem in run");
                        }
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("Fuck");
        }

    }
}