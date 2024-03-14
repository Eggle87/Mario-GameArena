import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Mario {

    //Declare Variables
    private double x;
    private double y;
    private int w = 16;
    private int h = 20;
    private double GRAVITY = 0.07;
    private double JUMP_SIZE = -3.2;

    public int collided=0;

    int walkAnim[] = {1, 2, 1, 3};
    int animInt = 1;
    int animTimer = 0;

    double accel = 0.02;
    double velocity_x = 0;
    double velocity_y = 0;
    double old_velocity_y = 0;

    double direction = 0;

    int time = 600;
    int timerframe = 0;

    int coinsCollected_ = 0;

    private double SPEED = 0.6;

    // The sprites that comprise mario
    private Sprite[] sprites = new Sprite[1];
    private BufferedImage MarioImages[] = new BufferedImage[9];
    private BufferedImage plc;

    public Mario(int _x, int _y) {

        //Take in loc variables
        this.x = _x;
        this.y = _y;

        //Get mario sprite png
        for (int i = 1; i <= 8; i++) {
            try {
                plc = ImageIO.read(new File("Sprites/mario/"+Integer.toString(i)+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MarioImages[i] = plc;
        }

        //Create new sprite object
        Sprite s = new Sprite(x, y, w, h, MarioImages[1]);
        sprites[0] = s;
    }

    // Adds the sprites to the GameArena
    public void addTo(GameArena arena) {
        for (int i = 0; i < sprites.length; i++)
            arena.addSprite(sprites[i]);
    }
    
    public void move(double dx, double dy, GameArena arena, int stop_x, int stop_y, Text timer, Text coinsCollected, Sprite coinSprite) {
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

        //Translate the x value in to the sprite location
        if (x > 80 && x < 3284) {
            arena.graphics.translate(-dx, 0);
            timer.setXPosition(x - 70);
            coinsCollected.setXPosition(x + 165);
            coinSprite.setXPosition(x + 145);
        }

        //Move all mario sprites(idfk how move works i cant lie to you joe is very smart)
        for (int i = 0; i < sprites.length; i++)
        sprites[i].move(dx, dy);
    }

    // Updates every frame
    public void update(GameArena arena, Goomba[] goombas, Tiles tiles, Text timer, Coin[] coins, Text coinsCollected, Sprite coinSprite) {
        double dir_x = 0;

        int stop_x = 0;
        int stop_y = 0;

        coinsCollected.setText(Integer.toString(coinsCollected_));

        timerframe += 1;

        if (timerframe == 240) {
            timerframe = 0;
            timer.setText(Integer.toString(time--));
        }
        
        // Movement variables
        if (arena.leftPressed() && x > 0) {
            dir_x = -1;
        }

        if (arena.rightPressed() && x < 10000000 - w) {
            dir_x = 1;
        }

        for (int i = 0; i < tiles.tilesSize; i++) {
            if (sprites[0].collides(tiles.tiles[i])) {
                if (tiles.tiles[i].getXPosition() < x && tiles.tiles[i].getYPosition() < y + 16) {
                    stop_x = -1;
                }
                if (tiles.tiles[i].getXPosition() > x && tiles.tiles[i].getYPosition() < y + 16) {
                    stop_x = 1;
                }
                if (tiles.tiles[i].getYPosition() < y && tiles.tiles[i].getXPosition() < x + 8) {
                    stop_y = -1;
                }
                if (tiles.tiles[i].getYPosition() > y && tiles.tiles[i].getXPosition() < x + 8) {
                    stop_y = 1;
                }

                for (int j = 0; j < tiles.getQuestionsSize(); j++) {
                    if (tiles.tiles[i] == tiles.getQuestions()[j]) {
                        if (stop_y == -1 && tiles.tiles[i].isCollected() == false) {
                            tiles.setCollected(tiles.tiles[i]);
                            coinsCollected_++;
                            break;
                        }
                    }
                }
            }
        }

        velocity_y += GRAVITY;

        if (stop_y == 1) {
            velocity_y = 0;
        }

        if (stop_y == -1) {
            velocity_y = 0.1;
        }

        // Jumping
        if (arena.upPressed() && velocity_y == 0 && old_velocity_y == velocity_y) {
            velocity_y = JUMP_SIZE;
        }

        if (dir_x != 0) direction = dir_x;

        if (velocity_y != 0 && direction == 1) {
            sprites[0].setImage(MarioImages[7]);
        }
        else if (velocity_y != 0 && direction == -1) {
            sprites[0].setImage(MarioImages[8]);
        }
        else if (dir_x == 1) {
            sprites[0].setImage(MarioImages[animInt]);

            animTimer++;
            if (animTimer > 15) {
                animTimer = 0;
                animInt++;
                if (animInt > 3) {
                    animInt = 1;
                }
            }
        }
        else if (dir_x == -1) {
            sprites[0].setImage(MarioImages[animInt]);

            animTimer++;
            if (animTimer > 15) {
                animTimer = 0;
                animInt++;
                if (animInt > 6) {
                    animInt = 4;
                }
            }
        }
        else if (sprites[0].getImage() == MarioImages[7]) {
            sprites[0].setImage(MarioImages[1]);
        }
        else if (sprites[0].getImage() == MarioImages[8]) {
            sprites[0].setImage(MarioImages[4]);
        }
        else {
            if (direction == 1) 
            sprites[0].setImage(MarioImages[1]);
            if (direction == -1) 
            sprites[0].setImage(MarioImages[4]);
        }

        if (dir_x == 1) {
            velocity_x += accel;
        }
        else if (dir_x == -1) {
            velocity_x -= accel;
        }
        else {
            for (int i = 0; i < 2; i++) if (velocity_x > 0) velocity_x -= accel;
            for (int i = 0; i < 2; i++) if (velocity_x < 0) velocity_x += accel;
        }

        if (velocity_x > SPEED) velocity_x = SPEED;
        else if (velocity_x < -SPEED) velocity_x = -SPEED;

        if (Math.abs(velocity_x) < 0.02) velocity_x = 0;

        //Acc move them innit
        move(velocity_x, velocity_y, arena, stop_x, stop_y, timer, coinsCollected, coinSprite);

        old_velocity_y = velocity_y;
        
        for (int i = 0; i < 14; i++) {
            if (sprites[0].collides(goombas[i].sprites[0]) && goombas[i].isDead() == false) {
                if (y + 8 < goombas[i].getYposition()) {
                    goombas[i].die();
                    velocity_y = JUMP_SIZE / 2;
                }
                else {
                    arena.graphics.translate(x - 80, 0);
                    collided=1;
                }
            }
        }

        if (y > 260) {
            collided = 3;
            arena.graphics.translate(x - 80, 0);
        }

        if (x > 3160) {
            collided = 2;
            arena.graphics.translate(x - 80, 0);
        }

        for (int i = 0; i < tiles.getCoinsSize(); i++) {
            if (sprites[0].collides(coins[i].sprites[0])) {
                coins[i].move(500000, 0, arena);
                coinsCollected_++;
            }
        }
    }
}

