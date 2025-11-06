package heroes;

import strategy.AttackStrategy;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class BaseHero {
    private String name;
    private double attackRange;
    private int reiatsu;

    public HeroState state = HeroState.IDLE;

    protected HeroAnimation animation;
    protected HeroMovement movement;
    protected AttackStrategy attackStrategy;

    protected boolean movingRight = true;
    public boolean attacking = false;
    protected boolean damageDealt = false;

    protected int currentFrame = 0;
    protected long lastFrameTime = 0;
    protected int frameDelay = 80;

    protected int attackFrame = 0;
    protected long attackStartTime = 0;
    protected int attackFrameDelay = 70;

    private long lastHitTime = 0;
    private boolean wasHit = false;

    public boolean up, down, left, right;

    private BaseHero currentTarget;

    public BaseHero(HeroAnimation animation, HeroMovement movement) {
        this.animation = animation;
        this.movement = movement;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setAttackRange(double range) {
        this.attackRange = range;
    }
    public double getAttackRange() {
        return attackRange;
    }

    public void setReiatsu(int reiatsu) {
        this.reiatsu = reiatsu;
    }
    public int getReiatsu() {
        return reiatsu;
    }

    public HeroAnimation getAnimation() {
        return animation;
    }


    public double getX() { return movement.x; }
    public double getY() { return movement.y; }

    public HeroMovement getMovement() {
        return movement;
    }

    public double distanceTo(BaseHero other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void setAttackStrategy(AttackStrategy strategy) {
        this.attackStrategy = strategy;
    }

    public void performAttack(BaseHero target) {
        if (attacking) return;
        this.currentTarget = target;
        startAttack();
    }

    private void doAttackHit() {
        if (attackStrategy != null && currentTarget != null && !damageDealt) {
            double distance = this.distanceTo(currentTarget);
            if (distance <= this.getAttackRange()) {
                attackStrategy.attack(this, currentTarget);
                damageDealt = true;
            } else {
                System.out.println(name + " missed the attack! Distance: " + (int) distance);
            }
        }
    }

    public void setTarget(BaseHero target) {
        this.currentTarget = target;
    }


    public boolean isAttacking() {
        return attacking;
    }

    public boolean isAttackFrame() {
        // можно сделать, чтобы атака происходила только на определённом фрейме удара
        return state == HeroState.ATTACK && attackFrame == (animation.attackFrames.length / 2);
    }

    public void reduceReiatsu(int amount) {
        reiatsu -= amount;
    }


    public void takeReiatsu(int amount) {
        reiatsu -= amount;
        if (reiatsu < 0) reiatsu = 0;
        wasHit = true;
        lastHitTime = System.currentTimeMillis();
        System.out.println(name + " took " + amount + " damage. HP: " + reiatsu);
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

                    if (attackFrame == 3) {
                        doAttackHit();
                    }

                    if (attackFrame >= animation.attackFrames.length) {
                        attackFrame = 0;
                        attacking = false;
                        damageDealt = false;
                        state = HeroState.IDLE;
                        currentTarget = null;
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

        // выбираем нужный кадр
        switch (state) {
            case RUN -> frame = animation.runFrames[currentFrame % animation.runFrames.length];
            case ATTACK -> frame = animation.attackFrames[attackFrame % animation.attackFrames.length];
            default -> frame = animation.idleFrames[currentFrame % animation.idleFrames.length];
        }

        // отражение по горизонтали, если смотрит влево
        if (!movingRight) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-frame.getWidth(), 0);
            frame = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(frame, null);
        }

        // эффект удара (вспышка красным)
        if (wasHit && System.currentTimeMillis() - lastHitTime < 150) {
            g.setColor(new Color(255, 0, 0, 120)); // прозрачный красный
            g.fillRect(movement.x, movement.y, frame.getWidth() * 2, frame.getHeight() * 2); // тоже увеличиваем!
        } else {
            wasHit = false;
        }

        // масштаб (во сколько раз увеличить)
        double scale = 2.0;

        // рисуем увеличенного героя
        g.drawImage(
                frame,
                movement.x, movement.y,                                         // позиция
                (int)(frame.getWidth() * scale), (int)(frame.getHeight() * scale), // масштабированные размеры
                null
        );
    }


}
