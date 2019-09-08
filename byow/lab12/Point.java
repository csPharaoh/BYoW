package byow.lab12;

public class Point
{

    int x;
    int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    private int getX()
    {
        return x;
    }
    private int getY()
    {
        return y;
    }

    private Point addPoint(Point p)
    {
        return new Point(p.x+this.x, p.y+this.y);
    }

}
