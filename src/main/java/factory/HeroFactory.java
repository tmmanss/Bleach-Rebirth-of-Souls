package factory;

import heroes.BaseHero;
import strategy.Projectile;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeroFactory {

    // Cache attacks map to avoid reloading on every hero creation
    private static Map<String, strategy.AttackStrategy> cachedAttacksMap = null;

    public static BaseHero createHero(String name, List<Projectile> sharedProjectiles) {
        // Load attacks if not already loaded
        if (cachedAttacksMap == null) {
            cachedAttacksMap = AttackLoader.loadAttacks(sharedProjectiles);
        }

        // Load hero using HeroLoader
        return HeroLoader.loadHero(name, cachedAttacksMap, sharedProjectiles);
    }

    public static Set<String> getAllHeroNames() {
        return HeroLoader.getAllHeroNames();
    }

}
