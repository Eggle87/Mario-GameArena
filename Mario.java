public class Mario {
    private int x;
    private int y;
    private int w = 50;
    private int h = 100;

    int velocity_x = 0;
    int velocity_y = 0;

    int old_velocity_y = 0;

    private int speed = 7;

    private Rectangle[] rects = new Rectangle[1];

    public Mario(int _x, int _y) {
        this.x = _x;
        this.y = _y;

        Rectangle a = new Rectangle(x, y, w, h, "RED");
        rects[0] = a;

        JFrame panel = new JFrame("Tile Puzzle");
        panel.add(new JLabel(new ImageIcon("Sprites/Mario_Right.png")));
        panel.pack();
        panel.setVisible(true);
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

        if (arena.leftPressed() && x > 0) {
            dir_x = -1;
        }
        
        if (arena.rightPressed() && x < arena.getWidth() - 50) {
            dir_x = 1;
        }
        
        if (y < arena.getHeight() - 100) {
            velocity_y += 1;
        }
        else {
            velocity_y = 0;
        }
        
        if (arena.upPressed() && velocity_y == 0 && old_velocity_y == velocity_y) {
            velocity_y = -20;
        }

        move(dir_x * speed, velocity_y);

        old_velocity_y = velocity_y;
    }
}
