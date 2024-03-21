import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tiles {
    static BufferedImage tileImage;
    static BufferedImage PipeImage;
    static BufferedImage blockImage;
    static BufferedImage brickImage;
    public static BufferedImage[] QuestionImage = new BufferedImage[5];
    public static Sprite[] tiles = new Sprite[1000];
    public static Sprite[] questionblocks = new Sprite[100];
    static int tilesSize = 0;
    static int questionsSize = 0;

    static Coin coins[] = new Coin[100];
    static int coinsSize = 0;
    static int goombaCounter = 0;

    public static Goomba[] goombalist = new Goomba[100];

    public Tiles() {
        try {
            tileImage = ImageIO.read(new File("Sprites/tile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PipeImage = ImageIO.read(new File("Sprites/pipe2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            blockImage = ImageIO.read(new File("Sprites/block.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            brickImage = ImageIO.read(new File("Sprites/brick.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 5; i++) {
            try {
                QuestionImage[i] = ImageIO.read(new File("Sprites/questionblock/"+Integer.toString(i)+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addPipe(450,180,32.0,100.0);

        addPipe(610,166,32.0,100.0);

        addPipe(736,150,32.0,100.0);

        addPipe(916,150,32.0,100.0);

        addTileArray();
    }

    // Creates a new tile
    public static void addTile(double x, double y, double w, double h, BufferedImage i) {
        Sprite tile = new Sprite(x * w, y * h, w, h, i);
        tiles[tilesSize] = tile;
        tilesSize++;
    }

    public static Sprite getsprite()
    {
        return this.sprite;
    }

    // Creates a new tile
    public static void addPipe(double x, double y, double w, double h) {
        Sprite tile = new Sprite(x, y, w, h, PipeImage);
        tiles[tilesSize] = tile;
        tilesSize++;
    }

    // Creates a new question mark
    public static void addQuestion(double x, double y, double w, double h) {
        Sprite tile = new Sprite(x * w, y * h, w, h, QuestionImage);
        tiles[tilesSize] = tile;
        tilesSize++;
        questionblocks[questionsSize] = tile;
        questionsSize++;
    }

    public static void addCoin(double x, double y, double w, double h) {
        Coin coin = new Coin(x * w, y * h);
        coins[coinsSize] = coin;
        coinsSize++;
    }

    // Adds the sprites to the GameArena
    public void addTo(GameArena arena) {
        for (int i = 0; i < tilesSize; i++)
            arena.addSprite(tiles[i]);
    }

    // Updates every frame
    public void update(GameArena arena) {
        for (int i = 0; i < questionsSize; i++) {
            questionblocks[i].animate();
        }
    }

    public static void addTileArray()
    {
        String[] layers = new String[15];
        layers[0] =  "                                                                                                                                                                                                                   ";
        layers[1] =  "                                                                                                                                                                                                                   ";
        layers[2] =  "                                                                                                                             cc                                                                                    ";
        layers[3] =  "                                                                                 G   G   c                                  c                                                                                      ";
        layers[4] =  "                                                                                                                                                                                                                   ";
        layers[5] =  "                      ?                                                       c TTTTTTTT   TTT?             c?c          TTT    T??T                                                       **                      ";
        layers[6] =  "                                                 c  c  c                     c c                                                                         c              c  c              ***                      ";
        layers[7] =  "                                         c                                    c                                                                                                          ****                      ";
        layers[8] =  "                                c  c                                                                                                                                                    *****                      ";
        layers[9] =  "                ?   T?T?T                                                    T?T              T     T     ?cc?cc?     T          TT      *  *          **  *            TT?T           ******                      ";
        layers[10]=  "                                                                                                                                        **  **        ***  **                         *******                      ";
        layers[11]=  "                      G              G              G G                                         G G                       G G G G      ***  ***      ****  ***                G G    ********                      ";
        layers[12]=  "                                                                                                                                      ****  ****    *****  ****                     *********         *            ";
        layers[13]=  "#####################################################################  ###############   ################################################################  ########################################################";
        layers[14]=  "#####################################################################  ###############   ################################################################  ########################################################";

        for (int x=0;x<15;x++)
        {
            for (int i=0;i<211;i++)
            {
                if (layers[x].charAt(i) == '#') {
                    addTile(i,x,16.0,16.0, tileImage);
                }
                else if (layers[x].charAt(i) == '?') {
                    addQuestion(i,x,16.0,16.0);
                }
                else if (layers[x].charAt(i) == '*') {
                    addTile(i,x,16.0,16.0, blockImage);
                }
                else if (layers[x].charAt(i) == 'T') {
                    addTile(i,x,16.0,16.0, brickImage);
                }
                else if (layers[x].charAt(i) == 'c') {
                    addCoin(i, x, 16, 16);
                }
                else if (layers[x].charAt(i)=='G')
                {
                   goombalist[goombaCounter] = new Goomba(i*16,x*16);
                   goombaCounter++;

                   
            }
        }
    }

    }

    public Coin[] getCoins() {
        return coins;
    }

    // Gets the coin size
    public int getCoinsSize() {
        return coinsSize;
    }

    public Sprite[] getQuestions() {
        return questionblocks;
    }

    public int getQuestionsSize() {
        return questionsSize;
    }

    public void addCoins(GameArena arena) {
        for (int i = 0; i < coinsSize; i++)
            coins[i].addTo(arena); 
    }

    public void setCollected(Sprite s) {
        s.setCollected();
        
    }

    public Goomba[] returnGoombas()
    {
        return goombalist;
    }
}

