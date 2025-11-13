package heroes.factory;

import heroes.BaseHero;
import strategy.Projectile;

import java.util.List;

public interface HeroFactory {
    BaseHero createHero(List<Projectile> projectileList);

}
