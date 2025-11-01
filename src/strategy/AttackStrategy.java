package strategy;

import heroes.Hero;

public interface AttackStrategy {
    void attack(Hero attacker, Hero target);
    String getName();
}
