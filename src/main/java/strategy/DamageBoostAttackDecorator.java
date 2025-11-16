package strategy;

import heroes.BaseHero;
import java.awt.image.BufferedImage;

public class DamageBoostAttackDecorator implements AttackStrategy {
    private AttackStrategy wrappedStrategy;
    private BoostedRangedAttack boostedRangedCache;
    private static final double DAMAGE_MULTIPLIER = 1.5;

    public DamageBoostAttackDecorator(AttackStrategy strategy) {
        this.wrappedStrategy = strategy;
        if (strategy instanceof RangedAttack) {
            this.boostedRangedCache = new BoostedRangedAttack((RangedAttack) strategy, DAMAGE_MULTIPLIER);
        }
    }

    @Override
    public void attack(BaseHero attacker, BaseHero target) {
        if (wrappedStrategy instanceof RangedAttack && boostedRangedCache != null) {
            boostedRangedCache.attack(attacker, target);
        } else {
            wrappedStrategy.attack(attacker, target);
        }
    }

    @Override
    public BufferedImage[] getAttackFrames() {
        return wrappedStrategy.getAttackFrames();
    }

    @Override
    public double getRange() {
        return wrappedStrategy.getRange();
    }

    @Override
    public int getDamage() {
        return (int)(wrappedStrategy.getDamage() * DAMAGE_MULTIPLIER);
    }

    @Override
    public int getCost() {
        return wrappedStrategy.getCost();
    }

    // Inner class to wrap RangedAttack with boosted damage
    private static class BoostedRangedAttack extends RangedAttack {
        private final RangedAttack original;
        private final double multiplier;

        public BoostedRangedAttack(RangedAttack original, double multiplier) {
            super(original.getAttackFrames(), 
                  original.projectileList, 
                  original.projectileSprite, 
                  original.projectileSpeed, 
                  original.getRange(), 
                  (int)(original.getDamage() * multiplier), 
                  original.getCost(), 
                  original.fireFrame);
            this.original = original;
            this.multiplier = multiplier;
        }

        @Override
        public int getDamage() {
            return (int)(original.getDamage() * multiplier);
        }
    }
}

