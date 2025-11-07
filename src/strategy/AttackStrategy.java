package strategy;

import heroes.BaseHero;
import java.awt.image.BufferedImage;

public interface AttackStrategy {
    void attack(BaseHero attacker, BaseHero target);
    BufferedImage[] getAttackFrames();

}
