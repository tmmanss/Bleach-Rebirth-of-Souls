package core;
import heroes.BaseHero;
import factory.HeroFactory;
import strategy.Projectile;

import java.util.ArrayList;
import java.util.List;

public class GameFacade {

    private BaseHero hero1;
    private BaseHero hero2;
    private List<Projectile> projectileList = new ArrayList<>();

    public GameFacade(BaseHero hero1, BaseHero hero2) {
        this.hero1 = hero1;
        this.hero2 = hero2;
    }

    public GamePanel createGamePanel() {
        GamePanel panel = new GamePanel(hero1, hero2, projectileList);
        panel.initPositions();
        return panel;
    }
}

