import core.GameFacade;
import core.GamePanel;
import core.HeroSelectionScreen;
import factory.HeroFactory;
import heroes.BaseHero;
import heroes.ExtraReiatsuDecorator;
import heroes.ExtraDamageDecorator;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        JFrame dummy = new JFrame();
        HeroSelectionScreen screen = new HeroSelectionScreen(dummy);
        screen.setVisible(true);
        String hero1Name = screen.getPlayer1Choice();
        String hero2Name = screen.getPlayer2Choice();
        String player1Advantage = screen.getPlayer1Advantage();
        String player2Advantage = screen.getPlayer2Advantage();
        java.util.List<strategy.Projectile> sharedProjectiles = new ArrayList<>();

        BaseHero hero1 = HeroFactory.createHero(hero1Name, sharedProjectiles);
        BaseHero hero2 = HeroFactory.createHero(hero2Name, sharedProjectiles);

        if ("Extra Reiatsu".equals(player1Advantage)) {
            hero1 = new ExtraReiatsuDecorator(hero1);
        } else if ("Extra Damage".equals(player1Advantage)) {
            hero1 = new ExtraDamageDecorator(hero1);
        }

        if ("Extra Reiatsu".equals(player2Advantage)) {
            hero2 = new ExtraReiatsuDecorator(hero2);
        } else if ("Extra Damage".equals(player2Advantage)) {
            hero2 = new ExtraDamageDecorator(hero2);
        }

        GameFacade game = new GameFacade(hero1, hero2, sharedProjectiles);
        GamePanel panel = game.createGamePanel();

        JFrame frame = new JFrame("Bleach Fighters");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}


