package strategy;

import heroes.BaseHero;

import java.awt.image.BufferedImage;

public class MeleeAttack implements AttackStrategy {
    private BufferedImage[] frames;

    public MeleeAttack(BufferedImage[] frames) {
        this.frames = frames;
    }

    @Override
    public void attack(BaseHero attacker, BaseHero target) {
        attacker.reduceReiatsu(20);
        double dist = attacker.distanceTo(target);
        if (dist <= attacker.getAttackRange()) {
            target.takeDamage(100);
        }
    }

    @Override
    public BufferedImage[] getAttackFrames() {
        return frames;
    }
}

