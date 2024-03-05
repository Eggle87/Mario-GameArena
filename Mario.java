import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Mario {
    //Declare variables
    private int x;
    private int y;
    private int w = 0;
    private int h = 0;
    private BufferedImage marioImage;

    int velocity_x = 0;
    int velocity_y = 0;
    int old_velocity_y = 0;

    private int speed = 7;

    private Rectangle[] rects = new Rectangle[1];

    public Mario(int _x, int _y) {
        this.x = _x;
        this.y = _y;

        //Get mario sprint png
        try {
            marioImage = ImageIO.read(new File("Sprites/Mario_Left.png"));
        } catch (IOException e) {
            e.printStackTrace(); //if the png isnt found error
        }
        //idk i cant lie
        Rectangle a = new Rectangle(x, y, w, h, "RED");
        rects[0] = a;
    }


    public void addTo(GameArena arena) {
        for (int i = 0; i < rects.length; i++)
            arena.addRectangle(rects[i]);
    }
    
    public void move(int dx, int dy) {
        x = x + dx;
        y = y + dy;

        for (int i = 0; i < rects.length; i++)
            rects[i].move(dx, dy);
    }


    //All for movement
    public void update(GameArena arena) {
        int dir_x = 0;
        int dir_y = 0;

        if (arena.leftPressed() && x > 0) {
            dir_x = -1;
        }

        if (arena.rightPressed() && x < arena.getWidth() - 50) {
            dir_x = 1;
        }

        if (y < arena.getHeight() - 100) {
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


    //Draws mario image to screen
    public void draw(Graphics g2d) {
        g2d.drawImage(marioImage, x, y, null);
    }
}
