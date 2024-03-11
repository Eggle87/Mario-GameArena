import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tiles extends Sprite{
    static BufferedImage tileImage;
    static BufferedImage PipeImage;
    public static Sprite[] tiles = new Sprite[10000];
    static int tilesSize = 0;
        static int PipeSize = 0;

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

        double tile_x= 1;

        for (int i = 0; i <= 68; i++) {
            addTile((tile_x * i), 13,16.0,16.0);
            addTile((tile_x * i), 14,16.0,16.0);
        }

        for (int i = 71; i <= 86; i++) {
            addTile((tile_x * i), 13,16.0,16.0);
            addTile((tile_x * i), 14,16.0,16.0);
        }

        for (int i = 90; i <= 300; i++) {
            addTile((tile_x * i), 13,16.0,16.0);
            addTile((tile_x * i), 14,16.0,16.0);
        }

        addTile(16, 9, 16.0,16.0);
        addTile(20, 9, 16.0,16.0);
        addTile(21, 9, 16.0,16.0);
        addTile(22, 9, 16.0,16.0);
        addTile(23, 9, 16.0,16.0);
        addTile(24, 9, 16.0,16.0);
        addTile(77, 9, 16.0,16.0);
        addTile(78, 9, 16.0,16.0);
        addTile(79, 9, 16.0,16.0);
        addTile(80, 5, 16.0,16.0);

        addPipe(450,180,32.0,100.0);

        addPipe(610,166,32.0,100.0);

        addPipe(736,150,32.0,100.0);

        addPipe(916,150,32.0,100.0);
    }

    // Creates a new tile
    public static void addTile(double x, double y, double w, double h) {
        Sprite tile = new Sprite(x * w, y * h, w, h, tileImage);
        tiles[tilesSize] = tile;
        tilesSize++;
    }

    // Creates a new tile
    public static void addPipe(double x, double y, double w, double h) {
        Sprite tile = new Sprite(x, y, w, h, PipeImage);
        tiles[PipeSize] = tile;
        PipeSize++;
    }

    // Adds the sprites to the GameArena
    public void addTo(GameArena arena) {
        for (int i = 0; i < tilesSize; i++)
            arena.addSprite(tiles[i]);
    }

    // Updates every frame
    public void update(GameArena arena) {
        
    }
}

