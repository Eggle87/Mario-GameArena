import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    private static BufferedImage coinImage;
    
    public static void main(String[] args)
    {
        //Create initial Objects
        GameArena arena = new GameArena(800,720);
        Mario mario = new Mario(32, 0);
        Tiles tiles = new Tiles();
        Goomba goombas[] = tiles.returnGoombas();
        
        //Starting screen
        arena.width_ = 800;
        arena.height_ = 720;

        arena.setBackgroundImage("Sprites/screens/StartingScreen.png");
        while(true) {
            arena.pause();
            if (arena.spacePressed()) {
                break;
            }
        };
        arena.setBackgroundImage("Sprites/Map.png");


        //Timer and coins collected text
        Text timer = new Text(" ", 16, 5000000,15.0,"WHITE");
        Text coinsCollected = new Text(" ", 16, 2500000.0,15.0,"WHITE");
        
        try {
            coinImage = ImageIO.read(new File("Sprites/coin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sprite coinSprite = new Sprite(200000, 2, 16, 16, coinImage);

        arena.width_ = 3376;
        arena.height_ = 240;
        arena.graphics.scale(3, 3); //scale game to fit in window
        
        //Add objects to arena (after starting screen has been done)
        mario.addTo(arena);
        tiles.addTo(arena);
        arena.addText(timer);
        arena.addText(coinsCollected);
        tiles.addCoins(arena);
        arena.addSprite(coinSprite);

        for (int i = 0; i < 14; i++) {
            goombas[i].addTo(arena);
        }
        //Update every frame
        while(true)
        {
            mario.update(arena, goombas, tiles, timer, tiles.getCoins(), coinsCollected, coinSprite);
            tiles.update(arena);
            //Updating goombas (walking+collision)
            for (int i = 0; i < 14; i++)
                goombas[i].update(arena, tiles);

            //Testing collision innit
            if (mario.collided == 1) { //if died from goomba
                arena.graphics.scale(0.333333, 0.3333333); //arena.graphics.scale(0.333333, 0.3333333); <- this is what this code does 
                arena.clearGameArena(); 
                arena.width_ = 800;
                arena.height_ = 720;
                arena.setBackgroundImage("Sprites/screens/DeathScreen.png");
                return;
            } else if (mario.collided == 2) { //if won
                arena.graphics.scale(0.3333333, 0.333333);
                arena.clearGameArena();
                arena.width_ = 800;
                arena.height_ = 720;
                arena.setBackgroundImage("Sprites/screens/WinScreen.png");
                return;
            }
            else if (mario.collided == 3) { //if fell to death (idk why its not second and win is in)
                arena.graphics.scale(0.333333, 0.3333333);
                arena.clearGameArena();
                arena.width_ = 800;
                arena.height_ = 720;
                arena.setBackgroundImage("Sprites/FELLTOYOURDEATHLOL.png");
                return;
            }
            
            arena.pause();
        }
    }

    // best part of the game
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
