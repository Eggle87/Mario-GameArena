public class Main {
    
    public static void main(String[] args)
    {
        GameArena arena = new GameArena(2000,720);
        Mario mario = new Mario(150, 0);
        Tiles tiles = new Tiles();
        Goomba goomba1 = new Goomba(400, 180);
        Goomba goomba2 = new Goomba(800, 180);
        Goomba goomba3 = new Goomba(880, 180);
        Coin coin = new Coin(400, 80);
        Text timer = new Text(" ", 20, 0.0,20.0,"WHITE");
        Text coinsCollected = new Text(" ", 20, 0.0,20.0,"WHITE");
        
        arena.setBackgroundImage("Sprites/mario_title-screen.png");
        while(true) {
            arena.pause();
            if (arena.spacePressed()) {
                break;
            }
        };
        arena.setBackgroundImage("Sprites/Map.png");

        arena.width_ = 3376;
        arena.height_ = 240;
        
        Goomba goombas[] = new Goomba[3];
        Coin coins[] = new Coin[1];

        goombas[0] = goomba1;
        goombas[1] = goomba2;
        goombas[2] = goomba3;
        coins[0] = coin;

        mario.addTo(arena);
        tiles.addTo(arena);
        arena.addText(timer);
        arena.addText(coinsCollected);

        for (int i = 0; i < goombas.length; i++)
            goombas[i].addTo(arena);
        
        for (int i = 0; i < coins.length; i++)
            coins[i].addTo(arena);

        arena.graphics.scale(3, 3);

        while(true)
        {
            mario.update(arena, goombas, tiles, timer, coins, coinsCollected);
            tiles.update(arena);
            for (int i = 0; i < goombas.length; i++)
                goombas[i].update(arena, tiles);

            if (mario.collided == 1) {
                arena.clearGameArena();
                arena.width_ = 1080;
                arena.height_ = 240;
                arena.setBackgroundImage("Sprites/endingscreen.png");
                return;
            }
            
            arena.pause();
        }
    }

    // Creates a grid
    /* public static void Grid(GameArena arena)
    {
        for(double i=0;i<=3500;i=i+50)
        {
            Line gridLiney = new Line(i,0.0,i,500.0,1.0,"");
            arena.addLine(gridLiney);
import java.applet.*;
import java.net.*;
            Line gridLinex = new Line(0.0,i,500,i,1.0,"");
            arena.addLine(gridLinex);
        }
    } */
}
