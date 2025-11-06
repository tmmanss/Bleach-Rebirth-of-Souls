package strategy;

import heroes.BaseHero;

public class MeleeAttack implements AttackStrategy {
    @Override
    public void attack(BaseHero attacker, BaseHero target) {
        double distance = attacker.distanceTo(target);
        attacker.reduceReiatsu(20);
        if (distance <= attacker.getAttackRange()) {
            target.takeDamage(100);
            System.out.println(attacker.getName() + " hit " + target.getName() + " with melee (" + (int) distance + ")");
        } else {
            System.out.println(attacker.getName() + " missed! Distance " + (int) distance);
        }
    }
}
