package core;
import heroes.BaseHero;
import heroes.factory.HeroFactory;
import strategy.Projectile;
import core.GamePanel;
import java.util.ArrayList;
import java.util.List;

public class GameFacade {
    private BaseHero hero1;
    private BaseHero hero2;
    private List<Projectile> projectileList;

    public GameFacade(String hero1Name, String hero2Name){
        projectileList = new ArrayList<>();
        hero1= HeroFactory.createHero(hero1Name,projectileList);
        hero2= HeroFactory.createHero(hero2Name,projectileList);
    }
    public BaseHero getHero1(){
        return hero1;
    }
    public BaseHero getHero2(){
        return hero2;
    }
    public List<Projectile> getProjectileList(){
        return projectileList;
    }
    public GamePanel createGamePanel(){
        GamePanel panel=new GamePanel(hero1,hero2,projectileList);
        panel.initPositions();
        return panel;
    }
}
