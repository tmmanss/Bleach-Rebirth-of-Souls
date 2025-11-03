package heroes;

public class HeroMovement {
    public int x = 100, y = 500;
    public int speed = 14;
    public int groundTop = 50;
    public int groundBottom = 400;
    public int groundLeft = 0;
    public int groundRight = 1024;

    public void move(boolean left, boolean right, boolean up, boolean down, int frameWidth) {
        int dx = 0, dy = 0;

        if (left) dx -= speed;
        if (right) dx += speed;
        if (up) dy -= speed;
        if (down) dy += speed;

        if (dx != 0 && dy != 0) {
            dx *= 0.7;
            dy *= 0.7;
        }

        x += dx;
        y += dy;

        // 🔒 Ограничения по карте
        if (x < groundLeft) x = groundLeft;
        if (x + frameWidth > groundRight) x = groundRight - frameWidth;
        if (y < groundTop) y = groundTop;
        if (y > groundBottom - 50) y = groundBottom - 50;
    }
}

