package byow.lab12;

import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class myWorldGenerator {
    private static final int WIDTH = 90;
    private static final int HEIGHT = 48;

    long SEED;
    private Point avatar;
    private Point enemy;
    private Point exitDoor;
    private TETile[][] myTiles;
    private Random RANDOM;
    private HashMap<Integer, Room> iDToRoom;
    private HashMap<Integer, Corridor> iDToCorridor;
    private HashMap<Integer, Integer> closestRoom;


    public myWorldGenerator(long userSeed) {
        SEED = userSeed;
        RANDOM = new Random(SEED);
    }

    private void createBG(TETile[][] worldTiles) {
        int height = worldTiles[0].length;
        int width = worldTiles.length;

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                worldTiles[x][y] = Tileset.NOTHING;
            }
        }

    }

    public TETile[][] generateWorld() {
        //long mySEED = userSeed;
        TETile[][] worldTiles = new TETile[WIDTH][HEIGHT];

        closestRoom = new HashMap<>();
        iDToRoom = new HashMap<>();
        iDToCorridor = new HashMap<>();
        createBG(worldTiles);
        int roomNum = 7;


        for (int i = 0; i < roomNum; i++) {
            createRoom(worldTiles);
        }

        for (int i = 1; i < roomNum; i++) {
            createCorridor(worldTiles, iDToRoom.get(i), iDToRoom.get(i + 1));
        }

        paintFloor(worldTiles);

        return worldTiles;
    }

    private void createCorridor(TETile[][] worldTiles, Room r1, Room r2) {
        int leftX, rightX, topY, botY = 0;                  //Corridor1 box
        int leftX2, rightX2, topY2, botY2 = 0;
        Passage sourceExit = null;
        Corridor corr1;
        Corridor corr2;
        String direction1 = "";
        String direction2 = "";
        String orientation1 = "";
        String orientation2 = "";
        Room source;
        Room target;

        if (r1.botRight.x <= r2.topLeft.x) {
            source = r1;
            target = r2;
        } else {
            source = r2;
            target = r1;
        }

        if (source.topLeft.y <= target.botRight.y) {
            //System.out.println("Case 1");
            sourceExit = createPassage(worldTiles, "vCorr", source.topLeft.x, source.botRight.x, source.topLeft.y, source.topLeft.y);
        } else if (source.botRight.y >= target.topLeft.y) {
            //System.out.println("Case 2");
            sourceExit = createPassage(worldTiles, "vCorr", source.topLeft.x, source.botRight.x, source.botRight.y, source.botRight.y);
        } else if (source.botRight.y >= target.botRight.y && source.topLeft.y <= target.topLeft.y) {
            //System.out.println("Case 3");
            sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, source.topLeft.y, source.botRight.y);
        } else if (source.topLeft.y > target.botRight.y && source.botRight.y < target.botRight.y) {
            //System.out.println("Case 4");

            if (source.topLeft.y > target.topLeft.y) {                 // Case a
                //System.out.println("a");
                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, target.topLeft.y, target.botRight.y);
            } else if ((source.topLeft.y - target.botRight.y) <= 2) {   // Case b
                //System.out.println("b");
                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, target.botRight.y, source.botRight.y);
            } else if ((target.botRight.y - source.botRight.y) <= 2) {  // Case c
                //System.out.println("c");
                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, source.topLeft.y, target.botRight.y);
            } else if ((target.topLeft.y - source.topLeft.y) <= 2) {  // Case d
                //System.out.println("d");
                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, source.topLeft.y, target.botRight.y);
            } else {
                //System.out.println("e");
                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, source.topLeft.y, target.botRight.y);
            }
        } else if (source.botRight.y >= target.botRight.y && source.botRight.y < target.topLeft.y) {  //Case #5                                                                            //Case #5


            if (source.topLeft.y - target.topLeft.y <= 2) {

                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, target.topLeft.y, source.botRight.y);
            } else if (target.topLeft.y - source.botRight.y <= 2) {

                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, source.topLeft.y, target.topLeft.y);
            } else if (source.botRight.y - target.botRight.y <= 2) {

                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, target.topLeft.y, source.botRight.y);
            } else {

                sourceExit = createPassage(worldTiles, "hCorr", source.botRight.x, source.botRight.x, target.topLeft.y, source.botRight.y);
            }
        }

        int sourceHoleX = sourceExit.hole.x;
        int sourceHoleY = sourceExit.hole.y;

        worldTiles[sourceHoleX][sourceHoleY] = Tileset.NOTHING;

        if (sourceHoleY == source.topLeft.y) {
            direction1 = "UP";
            orientation1 = "vertical";
            leftX = sourceHoleX - 1;
            rightX = sourceHoleX + 1;
            botY = sourceHoleY + 1;
            topY = target.center.y;
        } else if (sourceHoleY == source.botRight.y) {
            direction1 = "DOWN";
            orientation1 = "vertical";
            leftX = sourceHoleX - 1;
            rightX = sourceHoleX + 1;
            topY = sourceHoleY - 1;
            botY = target.center.y;
        } else if (sourceHoleX == source.topLeft.x) {
            direction1 = "LEFT";
            orientation1 = "horizontal";
            rightX = sourceHoleX - 1;
            topY = sourceHoleY + 1;
            botY = sourceHoleY - 1;
            leftX = target.center.x;
        } else {
            direction1 = "RIGHT";
            orientation1 = "horizontal";
            topY = sourceHoleY + 1;
            botY = sourceHoleY - 1;
            leftX = sourceHoleX + 1;
            rightX = target.center.x;
        }
        sourceExit.direction = direction1;

        for (int x = leftX; x <= rightX; x += 1) {
            for (int y = botY; y <= topY; y += 1) {
                if ((y == topY)) {
                    if (worldTiles[x][y] == Tileset.WALL && orientation1.equals("vertical")) {
                        worldTiles[x][y] = Tileset.NOTHING;
                    } else {
                        worldTiles[x][y] = Tileset.WALL;
                    }
                } else if ((y == botY)) {
                    if (worldTiles[x][y] == Tileset.WALL && orientation1.equals("vertical"))
                        worldTiles[x][y] = Tileset.NOTHING;
                    else
                        worldTiles[x][y] = Tileset.WALL;
                } else if ((x == leftX)) {
                    if (worldTiles[x][y] == Tileset.WALL && orientation1.equals("horizontal"))
                        worldTiles[x][y] = Tileset.NOTHING;
                    else
                        worldTiles[x][y] = Tileset.WALL;
                } else if ((x == rightX)) {
                    if (worldTiles[x][y] == Tileset.WALL && orientation1.equals("horizontal"))
                        worldTiles[x][y] = Tileset.NOTHING;
                    else
                        worldTiles[x][y] = Tileset.WALL;
                } else
                    worldTiles[x][y] = Tileset.NOTHING;
            }

        }

        if (direction1.equals("UP")) {
            worldTiles[sourceHoleX][botY] = Tileset.NOTHING;
        } else if (direction1.equals("DOWN")) {
            worldTiles[sourceHoleX][topY] = Tileset.NOTHING;
        } else if (direction1.equals("LEFT")) {
            worldTiles[rightX][sourceHoleY] = Tileset.NOTHING;
        } else if (direction1.equals("RIGHT")) {
            worldTiles[leftX][sourceHoleY] = Tileset.NOTHING;
        }

        corr1 = new Corridor(new Point(leftX, topY), new Point(rightX, botY));
        corr1.orientation = orientation1;

        corr1.ID = iDToCorridor.size() + 1;
        iDToCorridor.put(corr1.ID, corr1);

        if (direction1.equals("UP") || direction1.equals("DOWN")) {
            if (direction1.equals("UP")) {
                if (corr1.center.x < target.center.x) {
                    direction2 = "RIGHT";
                    orientation2 = "horizontal";
                    leftX2 = leftX;
                    rightX2 = target.topLeft.x;
                    topY2 = topY;
                    botY2 = topY2 - 2;
                } else {
                    direction2 = "LEFT";
                    orientation2 = "horizontal";
                    rightX2 = rightX;
                    leftX2 = target.botRight.x;
                    topY2 = topY;
                    botY2 = topY2 - 2;
                }
            } else {
                if (corr1.center.x < target.center.x) {
                    direction2 = "RIGHT";
                    orientation2 = "horizontal";
                    leftX2 = leftX;
                    rightX2 = target.topLeft.x;
                    botY2 = botY;
                    topY2 = botY2 + 2;

                } else {
                    direction2 = "LEFT";
                    orientation2 = "horizontal";
                    rightX2 = rightX;
                    leftX2 = target.botRight.x;
                    botY2 = topY;
                    topY2 = botY2 + 2;
                }
            }
        } else {
            if (corr1.center.y < target.center.y) {
                direction2 = "UP";
                orientation2 = "vertical";
                rightX2 = rightX;
                leftX2 = rightX2 - 2;
                topY2 = target.botRight.y;
                botY2 = botY;

            } else {
                direction2 = "DOWN";
                orientation2 = "vertical";
                rightX2 = rightX;
                leftX2 = rightX2 - 2;
                topY2 = topY;
                botY2 = target.topLeft.y;
            }
        }

        for (int x = leftX2; x <= rightX2; x += 1) {
            for (int y = botY2; y <= topY2; y += 1) {
                if ((y == topY2)) {
//                    if (worldTiles[x][y] == Tileset.WALL && orientation2.equals("vertical"))
//                        worldTiles[x][y] = Tileset.NOTHING;
//                    else
                        worldTiles[x][y] = Tileset.WALL;
                } else if ((y == botY2)) {
//                    if (worldTiles[x][y] == Tileset.WALL && orientation2.equals("vertical"))
//                        worldTiles[x][y] = Tileset.NOTHING;
//                    else
                        worldTiles[x][y] = Tileset.WALL;
                } else if ((x == leftX2)) {
//                    if (worldTiles[x][y] == Tileset.WALL && orientation2.equals("horizontal") && direction2.equals("LEFT"))
//                        worldTiles[x][y] = Tileset.NOTHING;
//                    else
                        worldTiles[x][y] = Tileset.WALL;

                } else if ((x == rightX2)) {
//                    if (worldTiles[x][y] == Tileset.WALL && orientation2.equals("horizontal") && direction2.equals("RIGHT"))
//                        worldTiles[x][y] = Tileset.NOTHING;
//                    else
                        worldTiles[x][y] = Tileset.WALL;
                } else
                    worldTiles[x][y] = Tileset.NOTHING;
            }
        }


        corr2 = new Corridor(new Point(leftX2, topY2), new Point(rightX2, botY2));
        corr2.orientation = orientation2;

        corr2.ID = iDToCorridor.size() + 1;
        iDToCorridor.put(corr2.ID, corr2);

    }


    private Passage createPassage(TETile[][] worldTiles, String type, int leftX, int rightX, int topY, int botY) {
        Passage pass = null;
        Point hole;
        String orientation = "";

        int xHole = 0;
        int yHole = 0;


        if (type.equals("vCorr")) {

            xHole = RANDOM.nextInt(rightX - (leftX + 1)) + (leftX + 1);
            yHole = topY;
        } else if (type.equals("hCorr")) {


            yHole = RANDOM.nextInt(topY - (botY + 1)) + (botY + 1);
            xHole = leftX;
        } else if (type.equals("room")) {

            int choice = RANDOM.nextInt(4);

            if (choice == 0) // 0 = left, 1 = right, 2 = top, 3 = bottom
            {
                yHole = RANDOM.nextInt(topY - (botY + 1)) + (botY + 1);
                xHole = leftX;

            } else if (choice == 1) {
                yHole = RANDOM.nextInt(topY - (botY + 1)) + (botY + 1);
                xHole = rightX;

            } else if (choice == 2) {
                xHole = RANDOM.nextInt(rightX - (leftX + 1)) + (leftX + 1);
                yHole = topY;

            } else {
                xHole = RANDOM.nextInt(rightX - (leftX + 1)) + (leftX + 1);
                yHole = botY;
            }
        }

        hole = new Point(xHole, yHole);


        if (topY == yHole)
            orientation = "horizontal";
        else
            orientation = "vertical";


        if (orientation.equals("horizontal")) {
            Point left = new Point(hole.x - 1, hole.y);
            Point right = new Point(hole.x + 1, hole.y);
            pass = new Passage(hole, left, right, orientation);
        } else if (orientation.equals("vertical")) {
            Point top = new Point(hole.x, hole.y + 1);
            Point bottom = new Point(hole.x, hole.y - 1);
            pass = new Passage(hole, top, bottom, orientation);
        }

        return pass;

    }

    private boolean isRegionBusy(TETile[][] worldTiles, Point randomPoint, int width, int height) {

        int leftX = randomPoint.x;
        int rightX = randomPoint.x + width;
        int topY = randomPoint.y;
        int botY = randomPoint.y - height;
        boolean busy = false;

//        System.out.println();
//        System.out.print("Current checking random point has X = "+leftX+", Y = "+topY);
//        System.out.println(", with leftX = "+leftX+" , rightX = "+rightX+" , topY = "+topY+" , botY = "+ botY);

        if (rightX + 5 >= WIDTH || botY <= 5 || topY + 5 >= HEIGHT || leftX - 5 <= 0) {
            busy = true;
            return busy;
        } else {
            for (int x = leftX - 5; x <= rightX + 5; x += 1) {
                for (int y = botY - 5; y <= topY + 5; y += 1) {
                    if (worldTiles[x][y] == Tileset.WALL) {
                        busy = true;
                        return busy;
                    }
                }
            }
            return busy;
        }
    }

    private Room createRoom(TETile[][] worldTiles) {

        Point topLeft;
        Point botRight;

        int roomWidth = roomWidth();
        int roomHeight = roomHeight();

        Point randStarPoint = pickRandPoint();

        while (isRegionBusy(worldTiles, randStarPoint, roomWidth, roomHeight)) {
            System.out.println("Searching for space");
            randStarPoint = pickRandPoint();
        }


        topLeft = randStarPoint;
        botRight = new Point(topLeft.x + roomWidth, topLeft.y - roomHeight);


        for (int x = topLeft.x; x <= botRight.x; x += 1) {
            for (int y = botRight.y; y <= topLeft.y; y += 1) {
                if ((y == topLeft.y)) {
                    worldTiles[x][y] = Tileset.WALL;
                } else if ((y == botRight.y)) {
                    worldTiles[x][y] = Tileset.WALL;
                } else if ((x == topLeft.x)) {
                    worldTiles[x][y] = Tileset.WALL;
                } else if ((x == botRight.x)) {
                    worldTiles[x][y] = Tileset.WALL;
                } else
                    worldTiles[x][y] = Tileset.NOTHING;
            }

        }


        Room r = new Room(roomWidth, roomHeight, topLeft, botRight);


        r.ID = iDToRoom.size() + 1;
        iDToRoom.put(r.ID, r);
        return r;
    }



    private void eatFloor(TETile[][] worldTiles)
    {
        List<Integer> roomIDs = new ArrayList<>(iDToRoom.keySet());
        List<Integer> corrIDs = new ArrayList<>(iDToCorridor.keySet());

        for (int id : roomIDs)
        {
            Room currRoom = iDToRoom.get(id);

            for (int x = currRoom.topLeft.x + 1; x < currRoom.botRight.x; x += 1)
            {
                for (int y = currRoom.botRight.y + 1; y < currRoom.topLeft.y; y += 1)
                {
                    worldTiles[x][y] = Tileset.NOTHING;
                }
            }
        }

        for (int id : corrIDs)
        {
            Corridor currCorr = iDToCorridor.get(id);
            if (currCorr.orientation.equals("horizontal"))
            {
                for (int x = currCorr.topLeft.x + 1 ; x <= currCorr.botRight.x; x += 1)
                {
                    for (int y = currCorr.botRight.y + 1; y < currCorr.topLeft.y; y += 1)
                    {
                        worldTiles[x][y] = Tileset.NOTHING;
                    }
                }
            } else
            {
                for (int x = currCorr.topLeft.x + 1; x < currCorr.botRight.x; x += 1)
                {
                    for (int y = currCorr.botRight.y + 1; y < currCorr.topLeft.y; y += 1)
                    {
                        worldTiles[x][y] = Tileset.NOTHING;
                    }
                }
            }
        }

    }


    private void paintFloor(TETile[][] worldTiles) {
        List<Integer> roomIDs = new ArrayList<>(iDToRoom.keySet());
        List<Integer> corrIDs = new ArrayList<>(iDToCorridor.keySet());

        for (int id : roomIDs)
        {
            Room currRoom = iDToRoom.get(id);

            for (int x = currRoom.topLeft.x + 1; x < currRoom.botRight.x; x += 1)
            {
                for (int y = currRoom.botRight.y + 1; y < currRoom.topLeft.y; y += 1)
                    worldTiles[x][y] = Tileset.FLOOR;

            }
        }

        for (int id : corrIDs)
        {
            Corridor currCorr = iDToCorridor.get(id);
            if (currCorr.orientation.equals("horizontal"))
            {
                for (int x = currCorr.topLeft.x + 1 ; x <= currCorr.botRight.x; x += 1)
                {
                    for (int y = currCorr.botRight.y + 1; y < currCorr.topLeft.y; y += 1)
                    {
                        worldTiles[x][y] = Tileset.FLOOR;
                    }
                }
            } else
                {
                for (int x = currCorr.topLeft.x + 1; x < currCorr.botRight.x; x += 1)
                {
                    for (int y = currCorr.botRight.y + 1; y < currCorr.topLeft.y; y += 1)
                    {
                        worldTiles[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    private void stitch(TETile[][] worldTiles) {
        int height = worldTiles[0].length;
        int width = worldTiles.length;

        for (int x = 0; x < width - 1; x += 1) {
            for (int y = 0; y < height - 1; y += 1) {
                if ((worldTiles[x][y] == Tileset.FLOOR) && (worldTiles[x][y + 1] == Tileset.NOTHING || worldTiles[x][y - 1] == Tileset.NOTHING || worldTiles[x + 1][y] == Tileset.NOTHING || worldTiles[x - 1][y] == Tileset.NOTHING))
                    worldTiles[x][y] = Tileset.WALL;
            }
        }
    }

    private int roomWidth() {
        return RANDOM.nextInt(10 - 4) + 4;
    }

    private int roomHeight() {
        return RANDOM.nextInt(10 - 4) + 4;
    }

    private Point pickRandPoint() {
        return new Point(randomXPos(), randomYPos());
    }

    private int randomYPos() {
        return RANDOM.nextInt(HEIGHT - 2) + 2;
    }

    private int randomXPos() {
        return RANDOM.nextInt(WIDTH - 2) + 2;
    }

    public TETile[][] getTiles()
    {
        return myTiles;
    }


    public void moveUp(Point avatarLocation, TETile[][] worldTiles, String face)
    {
        TETile character = null;
        if (face.equals("AVATAR"))
            character = Tileset.AVATAR;
        else if (face.equals("ENEMY"))
            character = Tileset.TREE;


        worldTiles[avatarLocation.x][avatarLocation.y] = Tileset.FLOOR;
        if(worldTiles[avatarLocation.x][avatarLocation.y + 1] == Tileset.WALL)
        {
            worldTiles[avatarLocation.x][avatarLocation.y] = character;
        }
        else
        {
            worldTiles[avatarLocation.x][avatarLocation.y + 1] = character;
            avatarLocation.y = avatarLocation.y + 1;
        }
    }

    public void moveDown(Point avatarLocation, TETile[][] worldTiles, String face)
    {
        TETile character = null;
        if (face.equals("AVATAR"))
            character = Tileset.AVATAR;
        else if (face.equals("ENEMY"))
            character = Tileset.TREE;


        worldTiles[avatarLocation.x][avatarLocation.y] = Tileset.FLOOR;
        if(worldTiles[avatarLocation.x][avatarLocation.y - 1] == Tileset.WALL)
        {
            worldTiles[avatarLocation.x][avatarLocation.y] = character;
        }
        else {
            worldTiles[avatarLocation.x][avatarLocation.y - 1] = character;
            avatarLocation.y = avatarLocation.y - 1;
        }
    }

    public void moveRight(Point avatarLocation, TETile[][] worldTiles, String face)
    {
        TETile character = null;
        if (face.equals("AVATAR"))
            character = Tileset.AVATAR;
        else if (face.equals("ENEMY"))
            character = Tileset.TREE;


        worldTiles[avatarLocation.x][avatarLocation.y] = Tileset.FLOOR;
        if(worldTiles[avatarLocation.x + 1][avatarLocation.y] == Tileset.WALL)
        {
            worldTiles[avatarLocation.x][avatarLocation.y] = character;
        }
        else {
            worldTiles[avatarLocation.x + 1][avatarLocation.y] = character;
            avatarLocation.x = avatarLocation.x + 1;
        }
    }

    public void moveLeft(Point avatarLocation, TETile[][] worldTiles, String face)
    {
        TETile character = null;
        if (face.equals("AVATAR"))
            character = Tileset.AVATAR;
        else if (face.equals("ENEMY"))
            character = Tileset.TREE;


        worldTiles[avatarLocation.x][avatarLocation.y] = Tileset.FLOOR;
        if(worldTiles[avatarLocation.x - 1][avatarLocation.y] == Tileset.WALL)
        {
            worldTiles[avatarLocation.x][avatarLocation.y] = character;
        }
        else {
            worldTiles[avatarLocation.x - 1][avatarLocation.y] = character;
            avatarLocation.x = avatarLocation.x - 1;
        }
    }

    private Point placeExitDoor(TETile[][] worldTiles)
    {
        Point initPos =pickRandPoint();
        while(worldTiles[initPos.x][initPos.y] != Tileset.WALL)
        {
            initPos = pickRandPoint();
            System.out.println("Finding a shithole for the door");
        }
        exitDoor = initPos;
        worldTiles[initPos.x][initPos.y] = Tileset.LOCKED_DOOR;
        System.out.println("Door position is ("+initPos.x+","+initPos.y+")");
        return initPos;
    }

    private Point placeEnemy (TETile[][] worldTiles)
    {
        Point initPos =pickRandPoint();
        while(worldTiles[initPos.x][initPos.y] != Tileset.FLOOR)
        {
            initPos = pickRandPoint();
            System.out.println("Finding a shithole for avatar");
        }
        avatar = initPos;
        worldTiles[initPos.x][initPos.y] = Tileset.TREE;
        System.out.println("Avatar Intial position is ("+initPos.x+","+initPos.y+")");
        return initPos;
    }

    private Point placeAvatar(TETile[][] worldTiles)
    {
        Point initPos =pickRandPoint();
        while(worldTiles[initPos.x][initPos.y] != Tileset.FLOOR)
        {
            initPos = pickRandPoint();
            System.out.println("Finding a shithole for avatar");
        }
        avatar = initPos;
        worldTiles[initPos.x][initPos.y] = Tileset.AVATAR;
        System.out.println("Avatar Intial position is ("+initPos.x+","+initPos.y+")");
        return initPos;
    }

    public void setCurrState(TETile[][] worldTiles, Point mouseLoc, Point exitDoor, String avatarName)
    {
        String mouseLocation = "";
        if((mouseLoc.x < WIDTH && mouseLoc.y < HEIGHT) && worldTiles[mouseLoc.x][mouseLoc.y] == Tileset.WALL)
            mouseLocation = "Wall";

        else if((mouseLoc.x < WIDTH && mouseLoc.y < HEIGHT) && worldTiles[mouseLoc.x][mouseLoc.y] == Tileset.FLOOR)
            mouseLocation = "Floor";

        else if((mouseLoc.x < WIDTH && mouseLoc.y < HEIGHT) && (mouseLoc.x == exitDoor.x && mouseLoc.y == exitDoor.y))
            mouseLocation = "Locked Door";

        StdDraw.setFont();
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(3, 46, ""+ mouseLocation);
        StdDraw.text(4, 44, "Player : "+ avatarName);
        StdDraw.show();

    }

    public static myWorldGenerator loadState(String engineSeed, String engineDirections, boolean cameFromEngine) throws FileNotFoundException
    {
        myWorldGenerator loadedWorld = null;

        File file = new File("gameState.txt");
        Scanner sc = new Scanner(file);
        String state = null;
        String seed = null;

        while (sc.hasNextLine())
        {
            if(cameFromEngine)
            {
                seed = engineSeed;
                state = engineDirections;

                try (PrintWriter out = new PrintWriter("gameState.txt"))
                {
                    out.println();

                }
                catch (FileNotFoundException e)
                {
                    System.out.println("IO problem in load state");
                }

                loadedWorld = new myWorldGenerator(Integer.parseInt(seed));
                break;
            }

            if (seed == null)
            {
                seed = sc.nextLine();
                loadedWorld = new myWorldGenerator(Integer.parseInt(seed));
            }
            if(state == null)
            {
                state = sc.nextLine();
            }
        }

        System.out.println(loadedWorld.SEED);
        System.out.println(state);

        if(state.charAt(0) == 'L')
        {
                state = state + engineDirections.substring(1,engineDirections.length());
        }

        TETile[][] generatedLoadedWorld = loadedWorld.generateWorld();
        Point enemyLoc = loadedWorld.placeEnemy(generatedLoadedWorld);
        Point avatarLoc = loadedWorld.placeAvatar(generatedLoadedWorld);
        Point exitDoor = loadedWorld.placeExitDoor(generatedLoadedWorld);

        for (int i = 0; i <state.length() ; i++)
        {
            if (state.charAt(i) == 'w' || state.charAt(i) == 'W')
                loadedWorld.moveUp(avatarLoc, generatedLoadedWorld, "AVATAR");

            else if (state.charAt(i) == 's' || state.charAt(i) == 'S')
                loadedWorld.moveDown(avatarLoc, generatedLoadedWorld,"AVATAR");

            else if (state.charAt(i) == 'd' || state.charAt(i) == 'D')
                loadedWorld.moveRight(avatarLoc, generatedLoadedWorld,"AVATAR");

            else if (state.charAt(i) == 'a' || state.charAt(i) == 'A')
                loadedWorld.moveLeft(avatarLoc, generatedLoadedWorld,"AVATAR");

            else if (state.charAt(i) == ':' && state.charAt(i + 1) == 'Q')
            {
                //System.out.println("Saving file");

                state = state.substring(0, state.length() - 2);

                if(state.charAt(0) == 'L')
                    loadedWorld.saveState(state.substring(1,state.length() - 2),Long.parseLong(seed));
                else if(engineDirections == null)
                {

                }
                else
                    loadedWorld.saveState(state,Long.parseLong(seed));
                //System.exit(0);
            }

        }

        loadedWorld.myTiles = generatedLoadedWorld;

        return loadedWorld;

    }


    public static void replayGame() throws FileNotFoundException
    {

        myWorldGenerator loadedWorld = null;

        File file = new File("gameState.txt");
        Scanner sc = new Scanner(file);
        String state = null;
        String seed = null;

        while (sc.hasNextLine())
        {
            if (seed == null)
            {
                seed = sc.nextLine();
                loadedWorld = new myWorldGenerator(Integer.parseInt(seed));
            }
            else if(state == null)
            {
                state = sc.nextLine();
            }
        }
//        System.out.println(loadedWorld.SEED);
//        System.out.println(state);

        TETile[][] generatedLoadedWorld = loadedWorld.generateWorld();
        Point enemyLoc = loadedWorld.placeEnemy(generatedLoadedWorld);
        Point avatarLoc = loadedWorld.placeAvatar(generatedLoadedWorld);
        Point exitDoor = loadedWorld.placeExitDoor(generatedLoadedWorld);

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        for (int i = 0; i <state.length() - 1 ; i++)
        {
            try
            {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ter.renderFrame(generatedLoadedWorld);
            if (state.charAt(i) == 'w')
                loadedWorld.moveUp(avatarLoc, generatedLoadedWorld, "AVATAR");

            else if (state.charAt(i) == 's')
                loadedWorld.moveDown(avatarLoc, generatedLoadedWorld,"AVATAR");

            else if (state.charAt(i) == 'd')
                loadedWorld.moveRight(avatarLoc, generatedLoadedWorld,"AVATAR");

            else if (state.charAt(i) == 'a')
                loadedWorld.moveLeft(avatarLoc, generatedLoadedWorld,"AVATAR");
        }

    }


    public void saveState(String history, long seedy) throws FileNotFoundException
    {
        File file = new File("gameState.txt");
        Scanner sc = new Scanner(file);
        String oldState = "";

        if (sc.hasNext())
        {
            if(Long.parseLong(sc.nextLine()) == seedy)
            {
                oldState = sc.nextLine();
            }

        }

        try (PrintWriter out = new PrintWriter("gameState.txt"))
        {
            out.println(seedy);
            out.println(oldState+history);

        }
        catch (FileNotFoundException e)
        {
            //System.out.println("IO problem");
        }

    }


    public static void loadGame(long seed, String decision, String avatarName) throws FileNotFoundException
    {

        myWorldGenerator wG = null;
        TETile[][] generatedWorld = null;
        Point avatarPos = null;
        Point enemyPos = null;
        Point exitDoor = null;

        if (decision.equals("N"))
        {
            try (PrintWriter out = new PrintWriter("gameState.txt"))
            {
                out.println();

            }
            catch (FileNotFoundException e)
            {
                System.out.println("IO problem in load Game");
            }
        }

        else if (decision.equals("L"))
        {
            wG = myWorldGenerator.loadState(null,null,false);
            generatedWorld = wG.myTiles;
            avatarPos = wG.avatar;
            exitDoor = wG.exitDoor;

        }

        else if (decision.equals("R"))
        {
            replayGame();
        }

        else if (decision.equals("S"))
        {
            wG = new myWorldGenerator(seed);
            generatedWorld = wG.generateWorld();
            long seedy = wG.SEED;
            avatarPos = wG.placeAvatar(generatedWorld);
            enemyPos = wG.placeEnemy(generatedWorld);
            exitDoor = wG.placeExitDoor(generatedWorld);
        }

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        String quit= "";
        char key = ' ';
        String history="";




        ThreadForAvatar.avatarName = avatarName;
        ThreadForAvatar.wG = wG;
        ThreadForAvatar.generatedWorld = generatedWorld;
        ThreadForAvatar.avatarPos = avatarPos;
        ThreadForAvatar.exitDoor = exitDoor;
        ThreadForAvatar.enemyPos = enemyPos;

        ThreadForCreatures.iDToRoom = wG.iDToRoom;
        ThreadForCreatures.iDToCorridor = wG.iDToCorridor;
        ThreadForCreatures.worldTiles = generatedWorld;

        ThreadForAvatar tfa = new ThreadForAvatar();
        ThreadForCreatures tfc = new ThreadForCreatures();


        tfa.run();
        tfc.run();

        



        /*
        while(true)
        {
            ter.renderFrame(generatedWorld);
            wG.setCurrState(generatedWorld,new Point((int)StdDraw.mouseX(),(int)StdDraw.mouseY()), exitDoor, avatarName);

            if(StdDraw.hasNextKeyTyped())
            {
                key = StdDraw.nextKeyTyped();
                history = history+key;

                if(key == ':' || key == 'Q')
                    quit = quit+key;

                if(key == 'w') {
                    wG.moveUp(avatarPos, generatedWorld, "AVATAR");
                    wG.moveUp(enemyPos, generatedWorld,"ENEMY");
                    //System.out.println("UP");
                }

                else if (key == 'a') {
                    wG.moveLeft(avatarPos, generatedWorld,"AVATAR");
                    wG.moveLeft(enemyPos, generatedWorld,"ENEMY");
                    //System.out.println("LEFT");
                }

                if(key == 's') {
                    wG.moveDown(avatarPos, generatedWorld,"AVATAR");
                    wG.moveDown(enemyPos, generatedWorld,"ENEMY");
                    //System.out.println("DOWN");
                }

                else if (key == 'd') {
                    wG.moveRight(avatarPos, generatedWorld,"AVATAR");
                    wG.moveRight(enemyPos, generatedWorld,"ENEMY");
                    //System.out.println("RIGHT");
                }
                else if (quit.equals(":Q"))
                {
                    //System.out.println(history);
                    wG.saveState(history, wG.SEED);
                    System.exit(0);
                }
            }

            //System.out.println(quit);

        }
        */

    }

    /*
    public static void main(String[] args) throws FileNotFoundException {

//        Engine e = new Engine();
//        e.interactWithInputString("n21644673s");

        myWorldGenerator wG = null;
        TETile[][] generatedWorld = null;
        Point avatarPos = null;
        Point enemyPos = null;
        Point exitDoor = null;
        String avatarName = "";

        while(true)
        {
                 if(StdDraw.hasNextKeyTyped())
                 {
                     if (StdDraw.nextKeyTyped() == 'L')
                     {
                         //System.out.println("L was pressed");
                         wG = myWorldGenerator.loadState(null,null,false);
                         generatedWorld = wG.myTiles;
                         avatarPos = wG.avatar;
                         exitDoor = wG.exitDoor;
                         break;
                     }
                     else
                         break;
                 }
                 else
                 {
                     //System.out.println("L was not pressed");
                 }
        }


        if (wG == null)
        {
            //System.out.println("WG is still null");
            wG = new myWorldGenerator(21644673);
            generatedWorld = wG.generateWorld();
            long seedy = wG.SEED;
            avatarPos = wG.placeAvatar(generatedWorld);
            enemyPos = wG.placeEnemy(generatedWorld);
            exitDoor = wG.placeExitDoor(generatedWorld);
        }

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        String quit= "";
        char key = ' ';
        String history="";

        while(true)
        {
            ter.renderFrame(generatedWorld);
            wG.setCurrState(generatedWorld,new Point((int)StdDraw.mouseX(),(int)StdDraw.mouseY()), exitDoor, avatarName);

            if(StdDraw.hasNextKeyTyped())
            {
                key = StdDraw.nextKeyTyped();
                history = history+key;

                if(key == ':' || key == 'Q')
                    quit = quit+key;

                if(key == 'w') {
                    wG.moveUp(avatarPos, generatedWorld);
                    //System.out.println("UP");
                }

                else if (key == 'a') {
                    wG.moveLeft(avatarPos, generatedWorld);
                    //System.out.println("LEFT");
                }

                if(key == 's') {
                    wG.moveDown(avatarPos, generatedWorld);
                    //System.out.println("DOWN");
                }

                else if (key == 'd') {
                    wG.moveRight(avatarPos, generatedWorld);
                    //System.out.println("RIGHT");
                }
                else if (quit.equals(":Q"))
                {
                    //System.out.println(history);
                    wG.saveState(history, wG.SEED);
                    System.exit(0);
                }
            }

            //System.out.println(quit);

        }


    }
    */
}
