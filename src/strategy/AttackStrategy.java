package strategy;

import heroes.BaseHero;

public interface AttackStrategy {
    void attack(BaseHero attacker, BaseHero target);
}
