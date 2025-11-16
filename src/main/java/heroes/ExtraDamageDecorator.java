package heroes;

import strategy.AttackStrategy;
import strategy.DamageBoostAttackDecorator;

import java.util.ArrayList;
import java.util.List;

public class ExtraDamageDecorator extends HeroDecorator {
    private List<AttackStrategy> boostedMelee;
    private List<AttackStrategy> boostedRanged;

    public ExtraDamageDecorator(BaseHero hero) {
        super(hero);
        wrapAttacksWithDamageBoost();
    }

    private void wrapAttacksWithDamageBoost() {
        boostedMelee = new ArrayList<>();
        for (AttackStrategy attack : wrappedHero.getMeleeAttacks()) {
            boostedMelee.add(new DamageBoostAttackDecorator(attack));
        }
        wrappedHero.setMeleeAttacks(boostedMelee);

        boostedRanged = new ArrayList<>();
        for (AttackStrategy attack : wrappedHero.getRangedAttacks()) {
            boostedRanged.add(new DamageBoostAttackDecorator(attack));
        }
        wrappedHero.setRangedAttacks(boostedRanged);
    }

    @Override
    public List<AttackStrategy> getMeleeAttacks() {
        return boostedMelee != null ? boostedMelee : wrappedHero.getMeleeAttacks();
    }

    @Override
    public List<AttackStrategy> getRangedAttacks() {
        return boostedRanged != null ? boostedRanged : wrappedHero.getRangedAttacks();
    }

    @Override
    public void performAttack(BaseHero target, AttackStrategy strategy) {
        AttackStrategy boostedStrategy = strategy;
        if (!(strategy instanceof DamageBoostAttackDecorator)) {
            boostedStrategy = new DamageBoostAttackDecorator(strategy);
        }
        wrappedHero.performAttack(target, boostedStrategy);
    }
}

