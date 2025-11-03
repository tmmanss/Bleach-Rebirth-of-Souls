package heroes;

import core.*;
import heroes.BaseHero;
import heroes.HeroBuilder;

public class IchigoHero extends BaseHero {
    public IchigoHero() {
        super(new HeroBuilder()
                        .setBasePath("assets/frames/ichigo")
                        .build()
                        .animation,
                new HeroMovement());
    }
}
