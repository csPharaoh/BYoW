package byow.lab12;

public class Corridor
{

    Passage entrance;
    Passage exit;
    Point start;

    Point center;
    Point topLeft;
    Point botRight;
    String orientation;

    int ID;
    int width;
    int height;

    public Corridor(Point topLeft, Point botRight)
    {
        this.topLeft = topLeft;
        this.botRight = botRight;
        this.center = new Point((topLeft.x+botRight.x)/2, (topLeft.y+botRight.y)/2);
    }

    private int getWidth()
    {
        return width;
    }

    private int getHeight()
    {
        return height;
    }


}
