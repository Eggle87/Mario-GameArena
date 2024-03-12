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
    
    public Sprite[] sprites = new Sprite[1];
    private BufferedImage goombaImages[] = new BufferedImage[4];
    private BufferedImage plc;
    int walkAnim[] = {1, 2};
    int animInt = 1;
    int animTimer = 0;

    int deathTimer = 0;
    boolean dead = false;

    int velocity_x = 0;
    double velocity_y = 0;
    double old_velocity_y = 0;

    //1 going right, -1 for going left
    int direction = 1;
    
    public Goomba(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;

        for (int i = 0; i <= 3; i++) {
            try {
                plc = ImageIO.read(new File("Sprites/goomba/"+Integer.toString(i)+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            goombaImages[i] = plc;
        }

        Sprite s = new Sprite(x, y, w, h, goombaImages[0]);
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
        if (dead == false) {
            animTimer++;
            if (animTimer > 100) {
                animTimer = 0;
                animInt++;
                if (animInt > 2) {
                    animInt = 1;
                }
            }

            sprites[0].setImage(goombaImages[animInt]);

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
            
            velocity_y += GRAVITY;

            if (stop_y == 1) {
                velocity_y = 0;
            }

            if (stop_y == -1) {
                velocity_y = 1;
            }
            
            move(direction * SPEED, velocity_y, arena, stop_x, stop_y);
        }
        else {
            deathTimer++;
            if (deathTimer == 100) {
                move(0, 1000, arena, 0, 0);
            }
        }
    }

    public double getYposition() {
        return y;
    }

    public boolean isDead() {
        return dead;
    }

    public void die() {
        dead = true;
        sprites[0].setImage(goombaImages[3]);
    }
}