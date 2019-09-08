package byow.lab12;

import java.util.Random;

public class TestShit
{
    public static void main(String[] args) {

        Random r = new Random(3);
        Random s = new Random(3);

        for (int i = 0; i < 5; i++)
        {
            System.out.println(r.nextInt());
            System.out.println(s.nextInt());
        }
    }













}
