package heroes;

public class ZangetsuHero extends BaseHero {
    public ZangetsuHero() {
        super(new HeroBuilder()
                        .setBasePath("assets/frames/zangetsu")
                        .build()
                        .animation,
                new HeroMovement());
    }
}