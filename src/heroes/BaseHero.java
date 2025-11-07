package heroes;

import observer.Observer;
import observer.Subject;
import java.util.ArrayList;
import java.util.List;
import strategy.AttackStrategy;
import strategy.RangedAttack;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class BaseHero implements Subject {
    private String name;
    private double attackRange;
    private int reiatsu;

    public HeroState state = HeroState.IDLE;
    private long spawnTime = System.currentTimeMillis();
    public boolean idleTimeOver = false;

    protected HeroAnimation animation;
    protected HeroMovement movement;
    public AttackStrategy attackStrategy;
    private AttackStrategy meleeStrategy;
    private AttackStrategy rangedStrategy;
    protected boolean projectileFiredThisAttack = false;

    public boolean movingRight = true;
    public boolean attacking = false;
    protected boolean damageDealt = false;

    private boolean dashing = false;
    private long dashStartTime;
    private int dashDuration = 100;
    private int dashDistance = 200;

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
    private List<Observer> observers = new ArrayList<>();


    public BaseHero(HeroAnimation animation, HeroMovement movement) {
        this.animation = animation;
        this.movement = movement;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }


    @Override
    public void notifyObservers(String event, int value) {
        for (Observer obs : observers) {
            obs.update(event, this, value);
        }
    }

    public void setMeleeStrategy(AttackStrategy s) { meleeStrategy = s; }
    public void setRangedStrategy(AttackStrategy s) { rangedStrategy = s; }
    public AttackStrategy getMeleeStrategy() { return meleeStrategy; }
    public AttackStrategy getRangedStrategy() { return rangedStrategy; }

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

    public int getAttackFrame() {
        return attackFrame;
    }

    public boolean isMovingRight() {
        return movingRight;
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

    public void performAttack(BaseHero target, AttackStrategy strategy) {
        if (attacking) return;

        this.currentTarget = target;
        this.attackStrategy = strategy;
        attacking = true;
        state = HeroState.ATTACK;
        attackFrame = 0;
        attackStartTime = System.currentTimeMillis();
    }

    public void setTarget(BaseHero target) {
        this.currentTarget = target;
    }


    public boolean isAttacking() {
        return attacking;
    }

    public void reduceReiatsu(int amount) {
        reiatsu -= amount;
    }


    public void takeDamage(int amount) {
        reiatsu -= amount;
        if (reiatsu < 0) reiatsu = 0;
        wasHit = true;
        lastHitTime = System.currentTimeMillis();
        System.out.println(name + " took " + amount + " damage. HP: " + reiatsu);
        notifyObservers("HIT", amount);
    }

    private BufferedImage[] getCurrentAttackFrames() {
        if (attackStrategy != null) {
            return attackStrategy.getAttackFrames();
        } else {
            return meleeStrategy != null ? meleeStrategy.getAttackFrames() : animation.attackFrames;
        }
    }

    public void performDash(boolean toRight) {
        if (dashing) return;
        dashing = true;
        dashStartTime = System.currentTimeMillis();
        state = HeroState.DASH;

        movement.x += toRight ? dashDistance : -dashDistance;
        if (movement.x < movement.groundLeft) movement.x = movement.groundLeft;
        if (movement.x > movement.groundRight) movement.x = movement.groundRight - 1;
    }

    public void updateIdleAnimation() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime < frameDelay) return;
        lastFrameTime = now;

        if (!idleTimeOver) {
            BufferedImage[] frames = animation.idleFrames;
            if (frames != null && frames.length > 0) {
                currentFrame = (currentFrame + 1) % frames.length;
            }
        }
    }




    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime < frameDelay) return;
        lastFrameTime = now;

        boolean anyKey = up || down || left || right;

        if (!idleTimeOver && now - spawnTime > 5000) {
            idleTimeOver = true; // первые 5 секунд прошли
            state = HeroState.STAND;
            currentFrame = 0;
        }

        switch (state) {
            case IDLE, STAND -> {
                BufferedImage[] framesToUse = !idleTimeOver ? animation.idleFrames : animation.standFrames;
                currentFrame = (currentFrame + 1) % framesToUse.length;

                if (anyKey) state = HeroState.RUN;
            }

            case RUN -> {
                currentFrame = (currentFrame + 1) % animation.runFrames.length;
                int frameWidth = animation.runFrames[currentFrame].getWidth();
                movement.move(left, right, up, down, frameWidth);
                if (!anyKey) state = idleTimeOver ? HeroState.STAND : HeroState.IDLE;
            }

            case ATTACK -> {
                if (now - attackStartTime > attackFrameDelay) {
                    attackFrame++;
                    attackStartTime = now;

                    if (attackStrategy != null && currentTarget != null) {
                        if (attackStrategy instanceof RangedAttack ranged) {
                            if (!projectileFiredThisAttack && attackFrame >= 7) {
                                ranged.fireProjectile(this, currentTarget);
                                projectileFiredThisAttack = true;
                            }
                        } else {
                            if (!damageDealt) {
                                attackStrategy.attack(this, currentTarget);
                                damageDealt = true;
                            }
                        }
                    }

                    BufferedImage[] framesToUse = getCurrentAttackFrames();
                    if (attackFrame >= framesToUse.length) {
                        attackFrame = 0;
                        attacking = false;
                        damageDealt = false;
                        projectileFiredThisAttack = false;
                        state = idleTimeOver ? HeroState.STAND : HeroState.IDLE;
                        currentTarget = null;
                    }
                }
            }
            case DASH -> {
                if (System.currentTimeMillis() - dashStartTime > dashDuration) {
                    dashing = false;
                    state = idleTimeOver ? HeroState.STAND : HeroState.IDLE;
                    currentFrame = 0;
                }
            }

        }

        movingRight = right || (!left && movingRight);
    }



    public void draw(Graphics g) {
        BufferedImage frame;

        switch (state) {
            case RUN -> frame = animation.runFrames[currentFrame % animation.runFrames.length];
            case ATTACK -> {
                BufferedImage[] framesToUse = getCurrentAttackFrames();
                frame = framesToUse[attackFrame % framesToUse.length];
            }
            case IDLE, STAND -> {
                BufferedImage[] framesToUse = !idleTimeOver ? animation.idleFrames : animation.standFrames;
                if (framesToUse == null || framesToUse.length == 0) return;
                frame = framesToUse[currentFrame % framesToUse.length];
            }
            case DASH -> {
                BufferedImage dashFrame = animation.dashFrame;
                if (dashFrame != null) {
                    frame = dashFrame;
                } else {
                    frame = animation.standFrames[0];
                }
            }

            default -> {
                if (animation.standFrames != null && animation.standFrames.length > 0) {
                    frame = animation.standFrames[currentFrame % animation.standFrames.length];
                } else return;
            }
        }

        if (!movingRight) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-frame.getWidth(), 0);
            frame = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(frame, null);
        }

        double scale = 2.0;
        if (state == HeroState.DASH) {
            scale = 0.7;
        }
        int scaledWidth = (int)(frame.getWidth() * scale);
        int scaledHeight = (int)(frame.getHeight() * scale);

        int drawX = movement.x;
        int drawY = movement.y - scaledHeight;

        if (wasHit && System.currentTimeMillis() - lastHitTime < 150) {
            g.setColor(new Color(255, 0, 0, 120));
            g.fillRect(drawX, drawY, scaledWidth, scaledHeight);
        } else {
            wasHit = false;
        }

        g.drawImage(frame, drawX, drawY, scaledWidth, scaledHeight, null);
    }

    public void reset() {
        up = false;
        down = false;
        left = false;
        right = false;

        attacking = false;
        damageDealt = false;
        projectileFiredThisAttack = false;
        currentTarget = null;
        attackFrame = 0;
        attackStartTime = 0;

        dashing = false;
        dashStartTime = 0;

        currentFrame = 0;
        lastFrameTime = 0;
        state = HeroState.IDLE;

        idleTimeOver = false;
        spawnTime = System.currentTimeMillis();
        wasHit = false;
        lastHitTime = 0;

        notifyObservers("RESET", 0);
    }
}
