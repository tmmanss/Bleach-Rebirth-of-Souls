package heroes;

import strategy.MeleeAttack;

public class Heroes {

    Hero ichigo = new HeroBuilder()
            .setName("Ичиго Куросаки")
            .setType(HeroType.SHINIGAMI)
            .setReiatsu(200)
            .setStrategy(new MeleeAttack())
            .build();

}
