public class Mario {
    private int x;
    private int y;
    private int w = 50;
    private int h = 100;

    private int speed = 7;

    private Rectangle[] rects = new Rectangle[1];

    public Mario(int _x, int _y) {
        this.x = _x;
        this.y = _y;

        Rectangle a = new Rectangle(x, y, w, h, "RED");
        rects[0] = a;
    }

    public Rectangle getRect() {
        return rects[0];
    }

    public void addTo(GameArena arena) {
        for(int i = 0; i < rects.length; i++)
            arena.addRectangle(rects[i]);
    }

    public void move(int dx, int dy) {
        x = x + dx;
        y = y + dy;

        for(int i = 0; i < rects.length; i++)
            rects[i].move(dx, dy);
    }

    public void update(GameArena arena) {
        int dir_x = 0;
        int dir_y = 0;

        if (arena.leftPressed() && x > 7) {
            dir_x = -1;
        }
        if (arena.rightPressed() && x < arena.getWidth() - 14) {
            dir_x = 1;
        }
        if (arena.downPressed() && y < arena.getHeight() - 14){
            dir_y = 1;
        }
        if (arena.upPressed() && y > 7) {
            dir_y = -1;
        }

        move(dir_x * speed, dir_y * speed);
    }
}