package byow.lab12;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThreadForCreatures extends Thread
{

    static HashMap<Integer, Room> iDToRoom;
    static HashMap<Integer, Corridor> iDToCorridor;
    static TETile[][] worldTiles;

    public void eatFloor(TETile[][] worldTiles)
    {
        List<Integer> roomIDs = new ArrayList<>(iDToRoom.keySet());
        List<Integer> corrIDs = new ArrayList<>(iDToCorridor.keySet());

        for (int id : roomIDs) {
            Room currRoom = iDToRoom.get(id);

            for (int x = currRoom.topLeft.x + 1; x < currRoom.botRight.x; x += 1) {
                for (int y = currRoom.botRight.y + 1; y < currRoom.topLeft.y; y += 1) {
                    worldTiles[x][y] = Tileset.NOTHING;
                }
            }
        }

        for (int id : corrIDs) {
            Corridor currCorr = iDToCorridor.get(id);
            if (currCorr.orientation.equals("horizontal")) {
                for (int x = currCorr.topLeft.x + 1; x <= currCorr.botRight.x; x += 1) {
                    for (int y = currCorr.botRight.y + 1; y < currCorr.topLeft.y; y += 1) {
                        worldTiles[x][y] = Tileset.NOTHING;
                    }
                }
            } else {
                for (int x = currCorr.topLeft.x + 1; x < currCorr.botRight.x; x += 1) {
                    for (int y = currCorr.botRight.y + 1; y < currCorr.topLeft.y; y += 1) {
                        worldTiles[x][y] = Tileset.NOTHING;
                    }
                }
            }
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
                System.out.println("meee");
        }
        catch(Exception e)
        {
            System.out.println("fuckkkk");
        }
    }

}
