import java.awt.Graphics;

public class Main {

    public static void main(String[] args)
    {
        GameArena arena = new GameArena(500,500);
        Graphics g2d = arena.getGraphics();

        Mario mario = new Mario(150, 0);

        mario.addTo(arena);

        Grid(arena);

        while(true)
        {
            mario.update(arena);

            arena.pause();
        }
    }
    public static void Grid(GameArena arena)
    {
        for(double i=0;i<=500;i=i+50)
        {
            Line gridLiney = new Line(i,0.0,i,500.0,1.0,"");
            arena.addLine(gridLiney);

            Line gridLinex = new Line(0.0,i,500,i,1.0,"");
            arena.addLine(gridLinex);
        }
    }
}
