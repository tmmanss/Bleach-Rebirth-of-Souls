package strategy;

import heroes.BaseHero;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile {
    public double x, y;
    public double targetX, targetY;
    public double speed = 10;
    public int damage;
    public boolean active = true;
    private BufferedImage sprite;
    private boolean movingRight;

    private BaseHero target;
    private double scale = 0.4;

    public Projectile(double x, double y, BaseHero target, int damage, BufferedImage sprite, boolean movingRight) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.targetX = target.getX();
        this.targetY = target.getY();
        this.damage = damage;
        this.sprite = sprite;
        this.movingRight = movingRight;

        if (sprite != null) this.y -= sprite.getHeight() * scale;
    }
    private void hitTarget() {
        if (target != null) {
            target.takeDamage(damage);
            System.out.println("Projectile hit " + target.getName() + " for " + damage);
        }
        active = false;
    }

    public void update() {
        if (!active) return;

        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist < speed) {
            hitTarget();
            return;
        }

        x += (movingRight ? 1 : -1) * speed * Math.abs(dx/dist);
        y += dy / dist * speed;
    }

    public void draw(Graphics g) {
        if (!active || sprite == null) return;

        int width = (int)(sprite.getWidth() * scale);
        int height = (int)(sprite.getHeight() * scale);
        int drawX = (int)x;
        int drawY = (int)(y - height);

        if (!movingRight) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(sprite, drawX + width, drawY, -width, height, null); // зеркально
        } else {
            g.drawImage(sprite, drawX, drawY, width, height, null);
        }
    }
}
