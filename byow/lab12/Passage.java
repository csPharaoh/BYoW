package byow.lab12;

public class Passage
{
   Point p1;
   Point p2;
   Point hole;

   int ID;

   String orientation;
   String direction;

   public Passage(Point hole,Point p1, Point p2, String orientation)
   {
       this.p1 = p1;
       this.p2 = p2;
       this.hole = hole;
       this.direction = direction;
       this.orientation = orientation;
   }

   private Point getPoint()
   {
       return p1;
   }


}
