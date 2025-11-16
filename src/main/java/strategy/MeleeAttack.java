package strategy;

import heroes.BaseHero;

import java.awt.image.BufferedImage;

public class MeleeAttack implements AttackStrategy {
    private BufferedImage[] frames;
    private double range;
    private int damage;
    private int cost;

    public MeleeAttack(BufferedImage[] frames, double range, int damage, int cost) {
        this.frames = frames;
        this.range = range;
        this.damage = damage;
        this.cost = cost;
    }

    @Override
    public void attack(BaseHero attacker, BaseHero target) {
        attacker.reduceReiatsu(cost);

        if (attacker.distanceTo(target) <= range) {
            target.takeDamage(getDamage());
        }
    }


    @Override
    public BufferedImage[] getAttackFrames() { return frames; }
    @Override public double getRange() { return range; }
    @Override public int getDamage() { return damage; }
    @Override public int getCost() { return cost; }
}


