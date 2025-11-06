package heroes;

import strategy.MeleeAttack;

public class Heroes {
    public BaseHero ichigo;
    public BaseHero zangetsu;

    public Heroes() {
        ichigo = new HeroBuilder()
                .setName("Ichigo Kurosaki")
                .setAttackRange(80)
                .setReiatsu(1000)
                .setAttackStrategy(new MeleeAttack())
                .setBasePath("assets/frames/ichigo")
                .setMovement(new HeroMovement())
                .build();

        zangetsu = new HeroBuilder()
                .setName("Zangetsu")
                .setAttackRange(80)
                .setReiatsu(1000)
                .setAttackStrategy(new MeleeAttack())
                .setBasePath("assets/frames/zangetsu")
                .setMovement(new HeroMovement())
                .build();
    }
}
