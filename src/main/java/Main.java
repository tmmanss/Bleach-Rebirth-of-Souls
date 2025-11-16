import core.GameFacade;
import core.GamePanel;
import core.HeroSelectionScreen;
import factory.HeroFactory;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        JFrame dummy = new JFrame();

        HeroSelectionScreen screen = new HeroSelectionScreen(dummy);
        screen.setVisible(true);

        String hero1Name = screen.getPlayer1Choice();
        String hero2Name = screen.getPlayer2Choice();

        System.out.println("Heroes selected: " + hero1Name + " vs " + hero2Name);

        // Create shared projectile list for both heroes
        java.util.List<strategy.Projectile> sharedProjectiles = new ArrayList<>();

        GameFacade game = new GameFacade(
                HeroFactory.createHero(hero1Name, sharedProjectiles),
                HeroFactory.createHero(hero2Name, sharedProjectiles),
                sharedProjectiles
        );

        GamePanel panel = game.createGamePanel();

        JFrame frame = new JFrame("Bleach Fighters");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}


