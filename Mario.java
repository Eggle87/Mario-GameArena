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
        //Collision
        int sizeLeft=13;
        int sizeRight;
        for (int i = 0; i < tiles.tilesSize; i++) {
            if (sprites[0].collides(tiles.tiles[i])) {

                if (tiles.tiles[i].getWidth()>16) //checking for pipes
                {
                    sizeRight=31;
                }
                else{
                    sizeRight=15;
                }

                if (tiles.tiles[i].getXPosition() < x && tiles.tiles[i].getYPosition() < y + 16) {
                    stop_x = -1;
                }
                if (tiles.tiles[i].getXPosition() > x && tiles.tiles[i].getYPosition() < y + 16) {
                    stop_x = 1;
                }
                //these stop mario falling/going through blocks on the y axis (the x parts are so he collides correctly on both sides of the block)
                if (tiles.tiles[i].getYPosition() < y && tiles.tiles[i].getXPosition() > x-sizeRight && tiles.tiles[i].getXPosition() < x+sizeLeft) {   ///< is checking marios left, > checks the right
                    stop_y = -1;
                }
                if (tiles.tiles[i].getYPosition() > y && tiles.tiles[i].getXPosition() > x-sizeRight && tiles.tiles[i].getXPosition() < x+sizeLeft) {
                    stop_y = 1;
                }

                //Checking if mario hit a question block
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
        //Just applying gravity
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

        // Animationing mario (moving from one frame of animation (moving from one frame of animation) of animation)
        if (velocity_y != 0 && direction == 1) {
            sprites[0].setImage(MarioImages[7]);
        }
        else if (velocity_y != 0 && direction == -1) {
            sprites[0].setImage(MarioImages[8]);
        }
        else if (dir_x == 1) {
            sprites[0].setImage(MarioImages[animInt]);

            animTimer++; // this adds to the animTimer variable which is an integer variable that adds to the timer to do with anim (animation) animation is when imagaes move from one image to another 
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
        //stopping mario from moving at the start
        if (y > 260) {
            collided = 3;
            arena.graphics.translate(x - 80, 0);
        }
        //stopping mario from moving at the end
        if (x > 3160) {
            collided = 2;
            arena.graphics.translate(x - 80, 0);
        }

        //checking if mario collected a coin
        for (int i = 0; i < tiles.getCoinsSize(); i++) {
            if (sprites[0].collides(coins[i].sprites[0])) {
                coins[i].move(500000, 0, arena);
                coinsCollected_++;
            }
        }
    }
}



// olccccc:clodxxkOOOOkxxdoooddxkkOO00KKXXNNNWWWWWWWNNNNNNNNNXXXXXXNNNWWWWWWWWWWWWNNNNNNNXXXNNNNXXXXK00OOkkxxxxxdlc;,'..........'',,;:clodddddxxxxxxxxddddolc:;,'............',,;;::ccccccccccccccccc::::::
// doooollloddxxkOOOOOkxddoooddxkkOO00KKXNNNNWWWWWWNNNNNNNNNXXXXXXXNNNWWWWWWWWWWWWNNNNNNNNXNNNNXXXXKK0Okkkkxxxxxoc:,,'..........'',,;:clooddddxxxxxxxdddddolc:;,'............',,;::ccccccccccccccccccc:::::
// kkxxxxxxkkkkOOO000OOkkxxxxxxkkOO000KKXNNNNNWWWWNNNNNNNNNXXXXXXXNNNWWWWWWWWWWWWNNNNNNNNNNNNNNXXXKK0Okkkxxxkkxol:;,''..........'',,;:clooddddxxxxxxxddddoolc:;,'............',,;::ccccccccccccccccccc:::::
// OOOOOOOOO00000000000OOOOOOOOO00000KKXXXXNNNNNNNNNNNNXXXXXXXXXXXNNNNWWWWWWWWWNNNNNNNNNNNNNNNXXXKK0Okkxxxxkkxdlc:;,'...........'',,;:clooddddxxxxxxxddddoolc;,''............',,;::cccccccccccccccccc::::::
// 00KK00000KKKKKKKKKK00000000000KKKKKKKXXXXXNNNNNXXXXXXXXXXXXXXXXXNNNNNNNNNNNNNNNNNNNNNNNNNNXXXKK0Okkkkxxkkxdoc:;;,'...........'',,;:cloodddddxdxxxdddddolc:;,''............',,;::ccccccccccccccccc:::::::
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXXXXXXXXXXXXXXKKKKXXXXXXNNNNNNNNNNNNNNNNNNNXNNXXXKK0Okkkkkkkkkxdolcc::;;,''''''',,;;;::cloodddddxxxxxxxddddoolc:;,,'.........'',,;;:cclllccccccccccccccccc::c
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXXXXXXXXXXXXXNNNNXXXXXXKK0OkkkkkkOOOOkxxdddoooolllllllllllloooddxxxxxxxkkkkkkxxxxxxddollccc::::::::::cccllloooooooooooooooooooolllll
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXXXXXXXXXXXXXXXXXKKK0Okkxxxxkkxxxxxxxxxxxxdddddddddddddxxxxxxkkkkkkkkkkkkkkxxxxxxdddoooooooooooooooddddddxxddddddddddddddddddddd
// KKKKKKKKKKKKKKKKKKKKKKKKXXKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXKKKKKKKKKKXXXXXXXXXXXXXXXXXXXXK0Okoccc::;;::;,,;::ccloddxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxddddddddddddddddddddddxxxxxxxxxdddxxxxxxxxxxxxxxd
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXXXXXXXXXXXKOxo::;,,,,,,,;;;;,,,,,'''',,;:lodxkxxxxxxxxxxxxxdddddddddddddddddddddddddddddddddddddddddddooooooooddddddddddddddd
// KKKKKKKKKKKKKKKKKK000000KKKKKKKKKKKKKKKKXXKKKKKKKKKKKKKKXXXXXXXXKKKKKKKXKXXXXXXXXX0xoc:,,;,,,,;;;;;:::c::;;;;,,'''..'',;coxxxxxxxxxxxxxdddddddddooolllooooooooooooooooooooooooooollllllllllllllllllllooo
// KKKKKKKKKK0000000OOOOOOOOOOOO0000KKKKXXXXXXXKKKKKKKKKKXXXNNNNNXXXXXKKXXXXXXXXXX0ko:;,,,;;;::::;;:clooodolccc:::;;,'......':ldkkkkkkkkkkkkkkkkkxxddooooodddxxxxxddddddddddddddddddoooooollllccclllloooooo
// 0000KKKK0000OOOkkkxxxxxkkkkkkkOO00KKKXXXXXXXXKKKKKKKKXXNNNNNNNNNXXXXXXXXXXXXKOdc;;;;:clllcccclollddxxdoollcccc:;;;,''.......'cxOOOOOOOOOO000OOkkxdddddxxxkkkkkkkkkkkkkkkkxxxxxxxxxxxdddoollccllloodddddd
// 0KKKKKKKK000OOkkxxxxddxxxxxxxxkO00KKXXXXXXXXXXKKKKKKXNNNWWWWWWNNNNXXXXXXXXK0xc;;:cloxkxdddddoodxxxxxddddolcccc:;;;,,''........':dO0000000000OOkkxxdddxxkkOOOOOOOkkkkkkkkkkkkkkkkkkkxxxddolllllloddxxxkkk
// 0KKKKKKKK000OOkkxxxxxxxxxxxxxkO00KKXXXXXXXXXXXXKKKKXNNNWWWWWWWNNNNNXXXXXX0occcloxkOOkkkkxkkxxxxxkkkkxxdoollllc:::;,'''''''......':dO00O0000OOkkxxdddxxkkOOOOOOOOOOOOOOkkkkkkkkkkkkkkxxdoolllloodxxkkkOOO
// KKKKKKKKK00OOkkxxxdddddxxxxxkkO00KKXXXNNXXXXXXXKKXXNNNWWWWWWWWWNNNNNXXXXkl;cdkO0K00KK00000OkkOkkOkkkxxdololllllcc:;,,,,,,,''......,okOOOOOkkkxxddddxxkOOOOOOOOOOOOkkkkkkkkkkkkkkkkxxdoollllloddxxkkOOO00
// KKKKKKKKK00OOkkxxdddddddxdxxkOO0KKXXXXNNXXXXXXKKXXNNNWWWWWWWWWWNNNNNXXKklcx0KXXXXXXXXXXKKKK000O00OOkkkxdooloooooolc:::;;,;;,'''....':dkkkkxxddddddxxkOOOOOOOOOOOOkkkkkkkkkkkkkkkkxxdoolllloodxxxkkkOO000
// 0KKKKKKK00OOkkkxxdddddxxxxxxkO00KKXXXXXXXXXXXXKXXNNNWWWWWWWWWWWNNNNNX0dld0XXXXXXXXXXXXXXXXKKKKKK00000Okkkxdxxxxxdddooollccc:;,,,,'...,oxxddddddddxkkOOOOOOOOOOOOkkkkkkkkkkkkkkkkxddoolllloodxxxkkkOO0000
// 0KKKKKK0000OOOkkkxxxxxkkkkkkOO00KKKXXXXXXXXKKKKXXNNNNWWWWWWWWWWNNNN/;NKkxkKXXXXXXXXXXXXXXXXXXKKKKKKKKK00OOOOkkkkkkkkxxxxxxxdodolcc:;,'..,looooddddxkkOOOOOOOOOkkkkkkkkkkkkkkkkkkxxdoollllooddxxxkkkOOO0000
// KKKKKKKKKK000000OOOOOOOOOOOO000KKKKKXXXXXKKKKKKKXXNNNNWWWWWWWNNNNNNKOxOKXXXXXXXXXXXXXXXXXXXXKKKKKKKKK00OOOOkkOOkkkkkkkOOkkxxxddddoc,'..,clooddxxkkOOOOOOOOkkkkkkkkkkkkkkkkkkkxxdoolllooddxxkkkkkOOO00000
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXNNNNNNNNNNNNNNNX0kOXXXXXXXXXXXXXXXXXXXXXXXXKKKKKKK00OOOkOOOOkkkOOOOOOOOkkkxxxxxdc,'..;loddxxkkOOOOOOkkkkkkkkkkkkkkkkkkkkxxddoollooddxxkkkkkkOOO000000
// XXXXXXXXXXXXXKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXXNNNNNNNNNNKO0XNXXNNXXXXXXXXXXXXXXXXKKKKKKKK0000OOkOOOOOkkOOOOOOOOOOkkkkxxxxdc;''':oddxxkkOOOOOkkkkkkkkkxxxkkkkkkkxxxdooolooddxkkkkkkkkOOOOO00000
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNNNX00XNXXNNNXXXXXXXXXXXXXXXXKKKKKKK0000OOOOOkOkkkkkkkOO00OOOOkkkkxxxxdc,,',cddxxkkkkkkkkkkkkkxxxxxxxxxxxxxxdooolooodxxkkkOOOOkkkOOOOOO000
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNXK0XNNNNNNNXXXXXXXXXXXXXXXXKKK00K0000OOOOkkkkkkkkkkkkOOOOOOOkkkxxxxxxdc,'';odxxxxxkkkkkkkkxxxxxxxxxxxxxddoolllooodxkkkOOOOOkkkkkkkOOOO00
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXKKXNNNNNNXNXXXXXXXXXXXXXXKKKK000000OOOOOkkkkkkkkkkkkkkkkkOOOkkkxxxxxxxdc;;;ldxxxxxxxxxxxxxxxxxxxxxdddddoollllooddxxkkkOOOOkkkkkkkkkkOOO0
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXKXNNNNNNXXXXXXXXXXXXXXKKKKKK00000OOOOOkkkkkkkkkkxxxxxxxxxkkxkkxxxxxxxxxoc;;:lddddxxxxxxxxxxxxddddddddoolllllloodxxkkkkkkkkkkkkkkkkkkOOOO
// 0000000000000000000000000KKKKKXXXXXXXXXXXXXNNNNXXXXXXXXXXXXXXXXXXNNNNNXXXXXXXXXXXXXKKKKKKKK00000OOOOOOkkkkkkkxxxxxxxdxxxxxxxxxddddxxxxxdl:;;coooddddxxxxxxxddddddddoollccllloodxxxxxxkkkkkkkkkkkkkkkOOOO
// xxxxxxxxxxkkkkkkkkkkkkkkO000KKKXXXXXXXXNNNNNNNNNNNXXXXXXXXXXXXXKXNNNNXXXXXXXXXXXXXXKKKKKKKK0000OOOOkkkkkkkkkkxxxxxxddddddxxxxxdddddxxxxxdl::cooooodddddddddddddddoollcccclloodddxxxxxxxxxxkkkkkkkkkkkkkk
// ccccccclllllllllllllooddxkO00KKXXXXXXXNNNNWWWWWWNNNNXXXXXXXXXXXKXXXXXXXXXXXXXXXXXXXXXXKKKKK00000OOkkOOOkOOkkkkkxxxxxxxxdxxxxxddddddxxxxkxdl:cloooooooodddddoooooolccccccllooddddddxxxxxxxxxxxxxxxkkkkkkk
// :::::::cccccccccccccclodxkO00KKXXXXXXNNNWWWWWWWWWNNNNXXXXXXXXXKXXKXXXXXXXXXXXXXXXXXXXXKKKKKK0KK0OkkOOOOOOOOOkkkkxxxxxxxxxddddddddddxxxkkxdocclooooooooooooooooollc:::cccloodddddxxxxxkkkkkxxxxxxxxkkkkkk
// :::::ccccccccccccccclodxkOO0KKKXXXXXNNNWWWWWWWWWWNNNNNXXXXXXXXKXXXXXXXXXXXXXXXXXXXXXXXXKKKKKKKK0OkkO000000OOkOkkkkkkkkxxxxxddddddddxxxkkxollloooooooooolllllllcc:::::ccloodddddxxxkkkkOOOOkkkxxxxxxxkkkk
// cllllllllllllllllllloxxkkO00KKKXXXXXNNNWWWWWWWWWWNNNNNNNNXXXXXKKKXXXXXXXXNNXXXXXXXXXXXXKKKKKKK0OOOOO00000OOOkkkkkkkkkkkkkxxxxxdddddddxkkdlccloddddddooolllllcc::::::cclooodddddxxkkOOO0000OOOOkkkxxxxxxx
// ddddddddddddddddddddxkkkOO000KKKKKXXXNNNWWWWWWWWWNNNNNNNNXXXXNX0OOKXXXXXNXXKK00000KK00000000K0OkOOO00000Okkkkkkkkkkkkkkkkkkkxxdddddxdxxxxo:::ldxddddddooollcc::::::cclooooodddxxkkOOO000000000OOOkkkxxxx
// OOOOOOOOOOOOOOOOOOOOOOO000000KKKKKKXXXNNNNNNNNNNNNNNNNNNNNXXXXXKkodKNNXXX0OOkkkkkxddddddxxkOOOkkOkkO0OOkxkkkkxxxxxxddddddooddddddddxxddddo:;;cdxxxxddddoollc:::::ccloooooooddxxkkOOO000KKKKKK0000OOOOkkx
// KKKKKKKK00000000000000KKKKKKKKKKKKKKKXXXNNNNNNNNNNNNNNNNNNNN0xkKkoox0XXK0OOO0000OOOxdooooodxxxxxxkkkOkxddxxddoollcccccccccccccloodxxxxdlc:;;:ldxxxxxdddoolc::::cclooddddddddxxkkOOO000KKKKKKKK00000000OO
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXXXXNNNNNNNNNNNNNN0dxX0oodxO00OOOO0KK0000OkxxdddddxxddxOOkkxdoloollc:::::::cccccc:::;:lodxddc,,;:loc:oxdddddoolcc:::cloodxxxxxxxxxxkkOOO0000KKKKKKKKKKKKKKKKK0
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNNNNXX0k0NXxddldkOOkO000OkxdolcclllllloddkO00Okxol:::::cclooddxxdddddooc::codooc,,ccldc,,;oxdddoollccccclodxxkkkkkkxxkkkOOO0000KKKKKKKKKKKKXXXXXXK
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNNNNXXKKKKXKko;oKOkkOkolc::;'...',;;:::coodkkkxdlc;:c::cc::::::;;::clllcc::cll:,;llcdxx:',lxdooollcccllodxxkkkkkkkkkkkkOOOO00000KKKKKKKKKKXXXXXXXX
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNNNXXKKX0k0X0d:xXKOxocclddxo:;;,,,,,;;:ldookxoolc:;:;,,,''','........';cllc:::;'lxoddxko,,lddoolllllloddxxkkkkkkkkkkkkOOOOOO000000KKKKKKKXXXXXXXXX
// KKKKKKKKKKKKKKKKKKKKXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNXXK00K0k0XXXOkXX0OkxkkOOkkxdoocc::cldxdcoOK0Okdc,,,,,,;:lolccc::::,..;:;;:lc..oxxdc:ldl:odooollllloddxxkkkkkkkkkOOOOOOOOOOOOO0000000KKXXXXXXXXXX
// 00000000000000000KKKKKKKXXXXXNNNNNNNXXXXXXXXXXXXXXNNNNNXXKKO0XKO0KXNXO0XXK0OOkkxdoolcc::cloxkx:'lKXK0kdc'.';:;;;;:ccccccccc;;;;;;cdl..cxxd:,;looddooolllloodddxxxkkkkkkkkOOOOOOOOOOOOOOOO000KKXXXXXXXXXK
// xxxxxxxxxxxxxxxkO00KKKKKXXXNNNNNNNNNNNNXXXXXXXXXXXNNNNXXKK0OOKXXXKXNNXOOOO0KKK0OOkxdollloddkkxlo0XXK0Oko,.',;:ccccc::::c::::;;;;:odlclodxo:,;loodooooloooooddddddxxxxkkkkkkOOOkOOkkkOOOOOOO0KKKXXXXXXXKK
// :::::cccccccccloxkO0KKKKXNNNNWWWWWWWNNNNXXXXXXXXXXNNXXXKK0OkkOXNXXXXXXKKK00000OOOkxolcldxxkkxxOKXXXK0Okxl;,;:::loddddddddoc:;::cloloxxooxo:,:oddoooooooooooooooooodddxxxxkkkkkkkkkkkOOOkkOO00KKKKKXXXKKK
// ;;;;;;::::;;;:coxk00KKXXNNNNWWWWWWWWNNNNNXXXXXXXXXXXXXK00OkkxOXNNXXXXXXNXXNXXK0OkxoooodddxkkOXXXXXXK0OOkxoc:ccloooooooooolcc:cclloodxxoldollooddoooooddddooolllllloooodddxxxxkkkkkkkkOOOkOOO0000KKKKKK00
// lllllllllllllldxkO0KKKXXNNNWWWWWWWWWNNNNNNXXXXXXXXXXXK00OkkxxONNXXXXXXNNNXXXKKKKKOOkOkkxdxO0KXXXNXK0OOkOkdoc:;:clllllcccc::clooddxxddxxooooxxddooooodddddoooollllllooooddddddxxxxkkkkkkkOOOOOOOOO000000O
// ddddddddddddddxkOO00KKXXNNNNWWWWWWWWNNNNNNXXXXXXXXXKK00OkkxxxkKNXXXXXNNNXXXXXXXK0OkkxoloxOKXXXXNNXAdrianOkxdooc;:clooooooollooodddxxxdxxdodddxxdlooooddooooooooooooooooddddddddddddxxkkkkkOOOOOOOkkOOOOO
// OkkkkkkkkkkkkkOOO000KKKXXNNNNNWWWWNNNNNNNNNXXXXXXXKK00OOkxxxxxxOKKKXXXXXXXXXXXK0kxdlcclxk0XXXXXXXX0OOkxxxddxxdc;;:cloooooooodddddxkkxkxollodxdoooddddoollooooooooddddddxxxxxdddddddddxxkkkOOOOOkkkkkkkkk
// 000000000000000000KKKKKKXXXNNNNNNNNNNNNNNNNNNXXXXKK00OOkxxxxdolcooxXNXXXXXXKK0Oxdlc:coxkO0K0OO0KK0Okxxdooooodoc;;;;:clooolloodddddxkxkxlloodxdddxxxddooolllllloooddxxxkkkkkxxxxxddddddxxkkkOOOOkkkkkkkkk
// KKKKKKKKKKKKKKKKKKKKKKKKKKXXXXXNNNNNNNNNNNNNNXXXXK00OOkxxxxdolc::;l0NXXXKKKK0OxdlcloxkkO000Okxxk0Okdddoc:;,;:;,,;;;;::loolllooddddxxkkdlllodddxxkkxxxddooollllllooddxxkkOOOkkkkxxxxxxxxxxkkOOOkkkkkkkkkk
// XXXXXXXXXXXXXKKKKKXXXXXXXXXXXXXXXXXNNNNNNNNNNXXXK00OOkxxxxdolc:::::kXXXKKKK0OkdlldxxkOOO00OOOkxxxxdolc:;,,,,,,,,,,;;;;:loollloooddxxkkdlooodxkkkOOkkkxxdddoooooooooddxxkkOOOOOOkkkkkxxxxxkkkkkkkkkkkkkkk
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNNNNXXKK0OOkxxxxdol::::::;oKXXKKKK0OdclxkkkkO000000Okxdolcc::;;;;,,,,,,,;;;::;;collllooddxxxxoloodxkkOOOOOOOkkkxxxdddooooddddxxxkkkOOOOOOOOkkkkkkkkxxxxxxkkkkkk
// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNNXXKK0OOkxxxxdoc::::::::cOXXKKKKKOdclxkkkO00KKKK00Okdollcclc:::;;:;;;;;;::::;;lollllloddxxdooddxkkOOOOOOOOOOOkkkxxxdddddddddxxxxxkkkkOOOOOkkOkkkxdddddxxxxxxx
// XXKKKKKKKKKKXXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNXXKK0OOkxxxxdoc::::::::::xXXXKKKK0xloxdxxollxkkOkkkxddoollllcc::c::::::::::c::odllccloddxxooodxxkkkOOOOOOOOOOOkkkkkxxxddddddddxxxxxxxkkkkOOOOOOkxdddooooddddx
// KKKKKK000KKKKKXXXNNNNNNNNXXXXXXXXXXXXXNNNNXXKK00Okxxxxdoc:::::::::::lOXXKKKKKOddxxxxdc,cx00kOkxxdlccccc::::;::,'..,;;:c:ldollclodddddooddxxxkkkkkkkOOOOOOOOkkkkkkkxxxdddddddxxxxxxxkkOO0OOkxxddolllllooo
// OOOOOOOOO000KXXNNNNNNNNNNNNXXXXXXXXXXXNNXXXKK00Okxxxxdolc::::::::::::oKXKKKKK0kxxxk000kddxkxOO0K0xodxkxdlcc;:,...';::::ldoolccloddddooddddddxxxxxkkkkkkkkkkkkkkkkkkkkxxxxddddddxxxxkOO0000Okxxdollllllll
// dddxxxxxkkO0KXXNNNWWWWWNNNNNXXXXXXXXXXXXXXKK00Okxxxxdolc:::::::::::c:ckXXKKKKK0kxkk0KKKK00koc:ldoc:clooo:,,,;,,;:::::clddolccloddddooddddoooodddddxxxxkkkkkkkkkkkkkkkkkkkxxxxdddddxkOO0000OOkxddollccccc
// dddddddxxkO0KXNNNWWWWWWWNNNNXXXXXXXXXXXXXKK00Okxxxxdolcccc:::::::::c:ckXXKKKKK0OOO00KKKKKKK0Oxolc::ccccc::ccccllccc:cldoolcclodddoooddddooooooooooodddxxxxkkkkkkkkkkkkkkkkkkkxxxxxxkOOO00OOOkxxdoollllcc
// xxxxxxxxkkO0KXXNNNWWWWWWNNNNXXXXXXXXXXXKK000Okxxxxxolccccccccccccccc::xXXKKKKKKK0000KKXXKK00OxddoddxxdollccllllllccclllllccloooddoooooooooooooolllooooddxxxxkkkkkkkkkkkkkkkkkkkkkxkkOOOOOkkkxxddooooolll
// kkkkOOOOOOO0KXXNNNNNNNNNNNNNNXXXXXXXXXKK000Okxxxxxdlcccccccccccccccc::xXXXXKKKXXKKKK0KKKKK0Oxdollllllllllccclllllloolllc::looddxddooooooooooooolllllooodddxxxxkkkkkkkkkkkkkkkkkkkkkOOOOOkkxxxddooooooooo
// 000000000000KKXXXNNNNNNNNNNNNNXXXXXXXKK00OOkxxxxxdllccccccccccccccccc:xXXXXXKKXXXKKKK00KK00Okddooollooolccccllllloooolc:clooddxxxxddoolllllllllllllooooddxxxxxkkkkkkkkkkkkkkkkkkkkOOOOOkkxxxddddoooooddd
// KKKKKKKKKKKKKKKXXXXNNNNNNNNNNNNNXXXXKK00OOkxxxxxdolccccccccccccccccc::xXXXXXKKKXXXXKKK0000OOkxdddddodoolllccclllooooolcclodddddxkkxxddooolllllllllooodddxxxkkkkkkkkkkkkkkkkkkkkkkOOOOOOkkkxxxxdddddddddd
// KKKKKXXXXXXXXXXXXXXXXXNNNNNNNNNNNXXXK00OOkxxxxxdolcccccccccccccccccclxKXXKKKXXXXXXXXXXKK0000OkxddddooollclllooooooodlcloodddddxO0OkkxxxddooolllllllooddxxxkkkkkkkkkkkkkkkkkkkkkkkOOOOOOkkkkkxxxxxxdddddd
// XXXXXXXXXXXXXXXXXXXXXXNNNNNNNNNNNXXKK00Okxxxxxdolllllccccccccccccclk000XXXXXXXKKKXXXXXXKKKKKK000Okxxxxdddddddooloodolloddddddddd0X0OkkkxxxdddooollooooddxxkkkkOOOOOOOOOOOOOkkkkkOOOOOOOkkkkkkkkkxxxxxxxx
// XXXXXXXXXXXXXXXXXXXXXXXNNNNNNNNNXXKK00OkxxxxxdollllllccccccccccclxK0xoOXXXXXKKKKKKXXKXXXXKKK0000OOkxxxxddddddollodoooooooooodxl;xKXK0OOkkkxxxxddddoooddddxxkkkkOOOOOOOOOOOOOOOOOOOOOOOOkkkkkkkkkkkkkkxxx
// XXXXXXXXXXXXXXXXXXXXXXXXNNNNNNNXXKK00Okxxxxxdollllllllllccccclco0XOo::kXXKKKKKKKKKKKKKKKKK00OOkkkkxxxddooooolllooooooooooooodx:.c0XXXK0OOOOkkkxxxxxddddddxxxxkkkOOOOOOOOOOOOOOOOOOOOOOkkkkkkkkkkkkkkkkkk
// KKXXXNNNNNNNNNNXXXXXXXXXNNNNNNXXKK00OkxxxxxdollllllllllllllllldXNOl:;cOXXKKKKKKKKKKKKK0000OOkkxxxxdddoollllllooooooooooooooodd' ,kKKXXX0OOOOOOOkkkxxxxxxxxxxxxkkkkOOOOOOO000OOOOOOOOOkkkkkkkkkkkkkkkkkkk
// KKXXNNNNWWNNNNNNNXXXXXXXNNNNNXXKKK0OkxxxxxdolllllllllllllllllkNWKdccco0XXXKKXKKKKKKKK00OOOkkkxxxddooooolollloooooooolloooooddl. 'xKKKXXXK0OOOOOOOOkkkkkxxxxxxxxkkkkkOOOOOOO000OOOOOOkkkkkkkkkkkkkkkkkkkk
// KKXXNNWWWWWWWNNNNNXXXXXXNNNNXXKKK0OkxxxxxdolllllllllllllllloOWWNOl;:;oKXXXXKKKKKKKKKKKK0OOkkkxxddddddddoooooooooooollloooooddc. 'xKKKXXXXXKKKKK000OOOkkkkkkkkkkkkkkkkkkOOOOOOOOOOOOOkkxxxkkkkkkkkkkkkkkO
// 0KXXNNNWWWWWWNNNNNNXXXXXXNXXXKKK0Okkxxxxdolllllllollllloolo0WWWXx;'':OXXXXXKKKKKKKKXXXKKK0OOkkxxdddddddooooooooooollolloooooo;. ,kKKKKXXXXXKXXXXXXXXKKK00OkkkkkkkkkkkkkkOOOOOOOOOOOkkxxxxxxkkkkkkkkkkkkO
// 0KKXXNNNWWWWNNNNNNNXXXXXXXXXKK00Okkxxkxdolllllllooooooolld0WWWWXd,';o0XXXXXXXXKKKKXXXXXXKK00OOkkxdddooooolllloooooooolooooooc'..c0KKKKKXXXXXKKKKKKKKKXXXXXKK0OkkkkkkkkkkOOOOOOOOOOkxxxxxxxxxkkkkkkkkkkOO
// 0KKXXNNNNNNNNNNNNNNXXXXXXXKKK00OkkxxkxdoollllloooooodxkOKXWWWWWXo,,:dOKXXKKXXXXXKKXXXXXXXKKK0Okkxxddoooolllloooooooooooooooc,..,k00KKKKXXXXXXKKKKKK000KXKKXXXKK00OOkkkkkkOOOOOOOkkkxxxxxxxkkkkkkkkkOkkO0
// KKKKXXXXNNNNNNNNNNNNXXXXXKK000Okkxkkxxoollloolodxk0KNWWWWMWWWWWXo,,cok0KKKKXXXXXXXKKXXXXKKKK0Okkxxddooooolooooooooooooooooc;..'d000KKKKKXXXXXK00KKKK0OKKKKKXXXXXXXXKK00OOkkkOkkkkkxxxxxxxkkkkkkkkOOOOO0K
// KKKKXXXXXXXNNNNNNNNNNXXXKK000OOkkkkkxdooolodxOKXNWWWWWWWWWWWWWWNd,;:lxO0KKKXXXXXXXKKKKKKKKKK0OOkkxddddoooooooooooooooooooc;'.,d00KKKKKKXXXXKXXK0O0KK0O0K00KKXXXXXXXXXXXXKK0OOkkkkxxxxxxxkkkkkkOOOOOOO00K
// XXXXXXXXXXXXNNNNNNNNNXXXKK00OOkkkkkxdoooxOKXNNNWWWWWWWWWWWWWWWWW0c;:ldkO00KXXXXXXXKKKKKKKKK00OOkkxxdooooooooooooolloooool:,.;x00KKKKKKKKXXXXXXK0OkO0OO00O000KKKXXXKKKXXXXXXXK00OkxxxxxkkkkkkkOOOOOOOO0KX
// XXXXXXXXXXXNNNNNNNNNNXXKK000OOOOOOkxxk0KNNNNNWWWWWWWWWWWWWWWWWWWXOc:codkkO0KXXXXKKKKKKKKKK000OOkkxdddddddooooooooooooool:,'ck00KKKKKKKKKXXXXXXK00OOOkkkk0OO0KKKKKKKKKXXXXXXXXXXXK0OkkkkkkkkkkOOOOOOO0KKX
// XXXXXXXXXXXXNNNNNNNNXXKK000OOOO000KXNWWNNXNWWWWWWWWWWWWWWWWWWWWWNX0dccodxkO0KXXXKKK0000KKK000OOOkxxxxxxdddddoooooooooolc;,o000KKKKKKKKKKXXXXXXK000OOOkk00OO0KKKKK0KKKXXXXXXXXXXXXNNXK0OOkkkkOOOOOOOO0KXX
// NNNNXXXXXXXXXXNNNNNXXKKK00OOO0KXNWWWWWNNXNWWWWWWNNWWMWWWWWWWWWWWNXKKxclodxkO0KKKKKK0000KKK000OOOkkxxxxxxddddoddddoooolc:;d0KKKKKKKKKKKXXXXXXXXXK00OkkkO0OkO0KKKKKK00KKKKKKXXXXXXXXXXXNXXK00OkOOOOOO0KKXX
// NNNNNXXXXXXXXXXXXXXXKK00000KNNWWWWWWWNNNXXNNNNNNNWWMWWWWWWWWWWWWWNXXKOllodxOO00KKKK0000KKKK00OOOOkkkxxxxxdddddxdddooolc:dKKKXXXXXXKKKKXXXXXXXXXK0OkxkO00OOO0KKKKKKKKKKKKKKKKKKXXXXXXXXXXNNNXK00OOOO0KXXN
// NNNNNXXXXXXXXXXXXXXKK0KKXNWWWWWNWWWWWWWWNXXXNNNWWMMWWWWWWWWWWWWWWNXXKX0olodkOO000KK00000KKK000OOOkkkkkkkxxxxxxxxxxdoollkKX0xdxxxkO0KXXKKXXXXXXXK0OxxO00OkOO000KKKKKKKKKKKKXKKKXXXXXXXXXXXXXXXNXX0OO0KXXN
// NNNNNNXXXXXXXXXXXKKKXNWWWWWWWWWWWWWWWWWWWWNNNNNNWMWWWWWWWWWWWWWWWNNXXXX0dcodkOO0000000000K0000OOOOkkkkkkkkxkkkkkxxxdod0XX0o:c::;;;:cok0KXXXXXXXK0kxk00OkOO0000KKKKKKKKKKKKKKKXXXKKXXXXXXXXXXXXXNNXKKKXNN
// NNNNNNXXXXXXXXXXXNNWWMWWNWWNNNWWNNWWNWWWWWNWNNNNWMWWWWWWWWWWWMWWWWNXXXKX0dcldxOO00000000000000OOOOOOOOkkkkkkkkkkkxxod0NXOolc::;;;;::clok0KXXXXXK0kk00OkkOO0000KKKKKKKKXXKKKKKXXKKKXXXXXXKKKXXXXXXXXXXXNN