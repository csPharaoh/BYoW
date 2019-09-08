package byow.lab12;

public class Room
{
     int closestRoomID;
     Passage entrance;
     Passage exit;

     Point topLeft;
     Point botRight;
     Point center;

     int ID;
     int width;
     int height;


     public Room()
     {

     }

     public Room(int width, int height, Point topLeft, Point botRight)
     {
            this.entrance = entrance;
            this.exit = exit;
            this.width = width;
            this.height = height;
            this.center = new Point((topLeft.x+botRight.x)/2, (topLeft.y+botRight.y)/2);
            this.topLeft = topLeft;
            this.botRight = botRight;
     }

     private void setEntrance()
     {

     }

    private void setExit()
    {

    }

    private void setStart()
    {

    }

     private int getWidth()
     {
         return width;
     }

     private int getHeight()
     {
         return height;
     }

     private Passage getEntrance()
     {
         return entrance;
     }

     private Passage getExit()
     {
        return exit;
     }
}
