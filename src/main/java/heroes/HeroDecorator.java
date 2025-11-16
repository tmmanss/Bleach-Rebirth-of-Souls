package heroes;

import observer.Observer;
import strategy.AttackStrategy;

import java.awt.*;
import java.util.List;

public abstract class HeroDecorator extends BaseHero {
    protected BaseHero wrappedHero;

    public HeroDecorator(BaseHero hero) {
        super(hero.getAnimation(), hero.getMovement());
        this.wrappedHero = hero;
        this.setName(hero.getName());
        this.setReiatsu(hero.getReiatsu());
        this.setAttackRange(hero.getAttackRange());
        this.setMeleeAttacks(hero.getMeleeAttacks());
        this.setRangedAttacks(hero.getRangedAttacks());
    }

    @Override
    public void addObserver(Observer observer) {
        wrappedHero.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        wrappedHero.removeObserver(observer);
    }

    @Override
    public void notifyObservers(String event, int value) {
        wrappedHero.notifyObservers(event, value);
    }

    @Override
    public void update() {
        wrappedHero.up = this.up;
        wrappedHero.down = this.down;
        wrappedHero.left = this.left;
        wrappedHero.right = this.right;
        wrappedHero.idleTimeOver = this.idleTimeOver;
        
        wrappedHero.update();
        
        this.state = wrappedHero.state;
        this.idleTimeOver = wrappedHero.idleTimeOver;
        this.movingRight = wrappedHero.movingRight;
        this.currentFrame = wrappedHero.currentFrame;
    }

    @Override
    public void updateIdleAnimation() {
        wrappedHero.idleTimeOver = this.idleTimeOver;
        wrappedHero.updateIdleAnimation();
        this.currentFrame = wrappedHero.currentFrame;
        this.idleTimeOver = wrappedHero.idleTimeOver;
    }

    @Override
    public void draw(Graphics g) {
        wrappedHero.draw(g);
    }

    @Override
    public void performAttack(BaseHero target, AttackStrategy strategy) {
        wrappedHero.performAttack(target, strategy);
    }

    @Override
    public void takeDamage(int amount) {
        wrappedHero.takeDamage(amount);
    }

    @Override
    public void reduceReiatsu(int amount) {
        wrappedHero.reduceReiatsu(amount);
    }

    @Override
    public double getX() {
        return wrappedHero.getX();
    }

    @Override
    public double getY() {
        return wrappedHero.getY();
    }

    @Override
    public HeroMovement getMovement() {
        return wrappedHero.getMovement();
    }

    @Override
    public double distanceTo(BaseHero other) {
        return wrappedHero.distanceTo(other);
    }

    @Override
    public void setTarget(BaseHero target) {
        wrappedHero.setTarget(target);
    }

    @Override
    public boolean isAttacking() {
        return wrappedHero.isAttacking();
    }

    @Override
    public int getAttackFrame() {
        return wrappedHero.getAttackFrame();
    }

    @Override
    public boolean isMovingRight() {
        return wrappedHero.isMovingRight();
    }

    @Override
    public HeroAnimation getAnimation() {
        return wrappedHero.getAnimation();
    }

    @Override
    public void performDash(boolean toRight) {
        wrappedHero.performDash(toRight);
    }

    @Override
    public void reset() {
        wrappedHero.up = this.up;
        wrappedHero.down = this.down;
        wrappedHero.left = this.left;
        wrappedHero.right = this.right;
        wrappedHero.idleTimeOver = this.idleTimeOver;
        
        wrappedHero.reset();
        
        this.state = wrappedHero.state;
        this.idleTimeOver = wrappedHero.idleTimeOver;
        this.movingRight = wrappedHero.movingRight;
        this.currentFrame = wrappedHero.currentFrame;
        this.up = wrappedHero.up;
        this.down = wrappedHero.down;
        this.left = wrappedHero.left;
        this.right = wrappedHero.right;
    }

    @Override
    public int getReiatsu() {
        return wrappedHero.getReiatsu();
    }

    @Override
    public String getName() {
        return wrappedHero.getName();
    }

    @Override
    public double getAttackRange() {
        return wrappedHero.getAttackRange();
    }

    @Override
    public List<AttackStrategy> getMeleeAttacks() {
        return wrappedHero.getMeleeAttacks();
    }

    @Override
    public List<AttackStrategy> getRangedAttacks() {
        return wrappedHero.getRangedAttacks();
    }
}

