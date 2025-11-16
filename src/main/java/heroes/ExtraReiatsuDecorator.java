package heroes;

public class ExtraReiatsuDecorator extends HeroDecorator {
    private static final int EXTRA_REIATSU = 50;

    public ExtraReiatsuDecorator(BaseHero hero) {
        super(hero);
        int currentReiatsu = hero.getReiatsu();
        wrappedHero.setReiatsu(currentReiatsu + EXTRA_REIATSU);
    }

    @Override
    public int getReiatsu() {
        return wrappedHero.getReiatsu();
    }
}

