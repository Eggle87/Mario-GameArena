import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Mario {
    //Declare variables
    private int x;
    private int y;
    private int w = 100;
    private int h = 100;
    private BufferedImage marioImage;

    int velocity_x = 0;
    int velocity_y = 0;
    int old_velocity_y = 0;

    private int speed = 7;

    private Sprite[] sprites = new Sprite[1];

    public Mario(int _x, int _y) {
        this.x = _x;
        this.y = _y;

        //Get mario sprint png
        try {
            marioImage = ImageIO.read(new File("Sprites/Mario_Left.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sprite b = new Sprite(x, y, w, h, marioImage);
        sprites[0] = b;
    }


    public void addTo(GameArena arena) {
        for (int i = 0; i < sprites.length; i++)
            arena.addSprite(sprites[i]);
    }
    
    public void move(int dx, int dy) {
        x = x + dx;
        y = y + dy;

        for (int i = 0; i < sprites.length; i++)
        sprites[i].move(dx, dy);
    }


    //All for movement
    public void update(GameArena arena) {
        int dir_x = 0;
        int dir_y = 0;

        if (arena.leftPressed() && x > 0) {
            dir_x = -1;
        }

        if (arena.rightPressed() && x < arena.getWidth() - w) {
            dir_x = 1;
        }

        if (y < arena.getHeight() - h) {
            velocity_y += 1;
        } else {
            velocity_y = 0;
        }

        if (arena.upPressed() && velocity_y == 0 && old_velocity_y == velocity_y) {
            velocity_y = -20;
        }

        move(dir_x * speed, velocity_y);

        old_velocity_y = velocity_y;
    }
}
