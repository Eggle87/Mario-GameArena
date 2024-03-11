import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Goomba
{
    private double x;
    private double y;
    private int w = 20;
    private int h = 20;
    private double SPEED = 0.25;
    private double GRAVITY = 0.1;
    private BufferedImage goombaImage;

    int velocity_x = 0;
    double velocity_y = 0;
    double old_velocity_y = 0;

    //1 going right, -1 for going left
    int direction = 1;

    public Sprite[] sprites = new Sprite[1];

    public Goomba(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;


        try {
            goombaImage = ImageIO.read(new File("Sprites/goomba2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sprite s = new Sprite(x, y, w, h, goombaImage);
        sprites[0] = s;
    }

    // Adds the sprites to the GameArena
    public void addTo(GameArena arena) {
        for (int i = 0; i < sprites.length; i++)
            arena.addSprite(sprites[i]);
    }

    public void move(double dx, double dy, GameArena arena , int stop_x, int stop_y) {
        if (stop_x == -1 && dx < 0) {
            dx = 0;
        }
        if (stop_x == 1 && dx > 0) {
            dx = 0;
        }
        if (stop_y == -1 && dy < 0) {
            dy = 0;
        }
        if (stop_y == 1 && dy > 0) {
            dy = 0;
        }

        //Move sprite by adding to x and y
        x = x + dx;
        y = y + dy;

        //Move all goomba sprites(idfk how move works i cant lie to you joe is very smart)
        for (int i = 0; i < sprites.length; i++)
        sprites[i].move(dx, dy);
    }

    public void update(GameArena arena, Tiles tiles)
    {
        int stop_x = 0;
        int stop_y = 0;

        for (int i = 0; i < tiles.tilesSize; i++) {
            if (sprites[0].collides(tiles.tiles[i])) {
                if (tiles.tiles[i].getXPosition() <= x && tiles.tiles[i].getYPosition() <= y + 16) {
                    stop_x = -1;
                    direction = -direction;
                }
                if (tiles.tiles[i].getXPosition() >= x && tiles.tiles[i].getYPosition() <= y + 16) {
                    stop_x = 1;
                    direction = -direction;
                }
                if (tiles.tiles[i].getYPosition() <= y) {
                    stop_y = -1;
                }
                if (tiles.tiles[i].getYPosition() >= y) {
                    stop_y = 1;
                }
            }
        }
        
        // Gravity
        if (y < arena.getHeight() / 3 - 32 - h) {
            velocity_y += GRAVITY;
        } else {
            velocity_y = 0;
        }
        
        move(direction * SPEED, velocity_y, arena, stop_x, stop_y);
    }

    public double getYposition() {
        return y;
    }
}