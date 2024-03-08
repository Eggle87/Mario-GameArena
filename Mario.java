import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Mario {
    private int x;
    private int y;
    private int w = 10;
    private int h = 20;
    private int GRAVITY = 1;
    private int JUMP_SIZE = -10;
    private BufferedImage marioImage;

    int velocity_x = 0;
    int velocity_y = 0;
    int old_velocity_y = 0;

    private int SPEED = 2;

    // The sprites that comprise mario
    private Sprite[] sprites = new Sprite[1];

    public Mario(int _x, int _y) {
        this.x = _x;
        this.y = _y;

        //Get mario sprite png
        try {
            marioImage = ImageIO.read(new File("Sprites/Mario_Left.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sprite s = new Sprite(x, y, w, h, marioImage);
        sprites[0] = s;
    }

    // Adds the sprites to the GameArena
    public void addTo(GameArena arena) {
        for (int i = 0; i < sprites.length; i++)
            arena.addSprite(sprites[i]);
    }
    
    public void move(int dx, int dy, GameArena arena) {
        x = x + dx;
        y = y + dy;

        arena.graphics.translate(-dx, 0);

        for (int i = 0; i < sprites.length; i++)
        sprites[i].move(dx, dy);
    }

    // Updates every frame
    public void update(GameArena arena) {
        int dir_x = 0;

        // Movement
        if (arena.leftPressed() && x > 0) {
            dir_x = -1;
        }

        if (arena.rightPressed() && x < 10000000 - w) {
            dir_x = 1;
        }

        // Gravity
        if (y < arena.getHeight() - 32 - h) {
            velocity_y += GRAVITY;
        } else {
            velocity_y = 0;
        }

        // Jumping
        if (arena.upPressed() && velocity_y == 0 && old_velocity_y == velocity_y) {
            velocity_y = JUMP_SIZE;
        }

        move(dir_x * SPEED, velocity_y, arena);

        old_velocity_y = velocity_y;
    }
}
