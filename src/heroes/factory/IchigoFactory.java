package heroes.factory;

import heroes.BaseHero;
import heroes.HeroBuilder;
import heroes.HeroMovement;
import strategy.Projectile;

import java.util.List;

public class IchigoFactory implements HeroFactory {
    @Override
    public BaseHero createHero(List<Projectile> sharedProjectiles) {
        return new HeroBuilder()
                .setName("Ichigo Kurosaki")
                .setAttackRange(80)
                .setReiatsu(1000)
                .setBasePath("assets/frames/ichigo")
                .setMovement(new HeroMovement())
                .setProjectiles(sharedProjectiles)
                .build();
    }
}
