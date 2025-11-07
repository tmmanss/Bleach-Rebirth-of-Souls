package heroes;

import strategy.MeleeAttack;
import strategy.Projectile;
import strategy.RangedAttack;
import java.util.ArrayList;
import java.util.List;


public class Heroes {
    public BaseHero ichigo;
    public BaseHero zangetsu;

    public Heroes(List<Projectile> sharedProjectiles) {
        ichigo = new HeroBuilder()
                .setName("Ichigo Kurosaki")
                .setAttackRange(80)
                .setReiatsu(1000)
                .setBasePath("assets/frames/ichigo")
                .setMovement(new HeroMovement())
                .setProjectiles(sharedProjectiles)
                .build();

        zangetsu = new HeroBuilder()
                .setName("Zangetsu")
                .setAttackRange(80)
                .setReiatsu(1000)
                .setBasePath("assets/frames/zangetsu")
                .setMovement(new HeroMovement())
                .setProjectiles(sharedProjectiles)
                .build();
    }
}
