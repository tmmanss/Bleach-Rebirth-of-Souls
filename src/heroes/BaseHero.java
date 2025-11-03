package heroes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class BaseHero {

    protected HeroAnimation animation;
    protected HeroMovement movement;
    protected HeroState state = HeroState.IDLE;

    protected boolean movingRight = true;
    protected boolean attacking = false;

    protected int currentFrame = 0;
    protected long lastFrameTime = 0;
    protected int frameDelay = 60;

    protected int attackFrame = 0;
    protected long attackStartTime = 0;
    protected int attackFrameDelay = 70;

    public boolean up, down, left, right;

    public BaseHero(HeroAnimation animation, HeroMovement movement) {
        this.animation = animation;
        this.movement = movement;
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime < frameDelay) return;
        lastFrameTime = now;

        boolean anyKey = up || down || left || right;

        switch (state) {
            case IDLE -> {
                currentFrame = (currentFrame + 1) % animation.idleFrames.length;
                if (anyKey) state = HeroState.RUN;
            }
            case RUN -> {
                currentFrame = (currentFrame + 1) % animation.runFrames.length;
                int frameWidth = animation.runFrames[currentFrame].getWidth();
                movement.move(left, right, up, down, frameWidth);
                if (!anyKey) state = HeroState.IDLE;
            }
            case ATTACK -> {
                if (now - attackStartTime > attackFrameDelay) {
                    attackFrame++;
                    attackStartTime = now;
                    if (attackFrame >= animation.attackFrames.length) {
                        attackFrame = 0;
                        attacking = false;
                        state = HeroState.IDLE;
                    }
                }
            }
        }

        movingRight = right || (!left && movingRight);
    }

    public void startAttack() {
        if (attacking) return;
        attacking = true;
        state = HeroState.ATTACK;
        attackFrame = 0;
        attackStartTime = System.currentTimeMillis();
    }

    public void draw(Graphics g) {
        BufferedImage frame;

        switch (state) {
            case RUN -> frame = animation.runFrames[currentFrame % animation.runFrames.length];
            case ATTACK -> frame = animation.attackFrames[attackFrame % animation.attackFrames.length];
            default -> frame = animation.idleFrames[currentFrame % animation.idleFrames.length];
        }

        if (!movingRight) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-frame.getWidth(), 0);
            frame = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(frame, null);
        }

        g.drawImage(frame, movement.x, movement.y, null);
    }

    public HeroMovement getMovement() {
        return movement;
    }
}
