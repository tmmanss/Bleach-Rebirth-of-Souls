package heroes.factory;

import heroes.BaseHero;
import heroes.HeroBuilder;
import heroes.HeroMovement;
import strategy.Projectile;

import java.util.List;

public class ZangetsuFactory implements HeroFactory{
    @Override
    public BaseHero createHero(List<Projectile> sharedProjectiles) {
        return new HeroBuilder()
                .setName("Zangetsu")
                .setAttackRange(80)
                .setReiatsu(1000)
                .setBasePath("assets/frames/zangetsu")
                .setMovement(new HeroMovement())
                .setProjectiles(sharedProjectiles)
                .build();
    }
}
