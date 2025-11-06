package observer;

import heroes.BaseHero;
import java.awt.*;

public class HPBar implements Observer {
    private int x, y, width, height;

    public HPBar(int x, int y, int width, int height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }

    @Override
    public void update(String event, BaseHero hero, int value) {
        if (event.equals("HIT")) {
            System.out.println(hero.getName() + " HP changed: " + hero.getReiatsu());
        }
    }

    public void draw(Graphics g, BaseHero hero) {
        double ratio = hero.getReiatsu() / 1000.0;
        g.setColor(Color.RED);
        g.fillRect(x, y, (int)(width * ratio), height);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
        g.drawString(hero.getName() + " HP: " + hero.getReiatsu(), x, y - 5);
    }
}
