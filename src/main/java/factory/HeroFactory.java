package factory;

import heroes.BaseHero;
import heroes.HeroLoader;
import factory.AttackFactory;
import strategy.Projectile;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeroFactory {
    public static BaseHero createHero(String name, List<Projectile> sharedProjectiles) {
        return HeroLoader.loadHero(name, sharedProjectiles);
    }
    public static Set<String> getAllHeroNames() {
        return HeroLoader.getAllHeroNames();
    }

}
