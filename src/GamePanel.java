import core.Background;
import core.Controls;
import core.KeyAdapterImpl;
import heroes.*;
import observer.HPBar;
import strategy.MeleeAttack;
import strategy.Projectile;
import strategy.RangedAttack;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private BaseHero hero1;
    private BaseHero hero2;
    private Background background;
    private Thread gameThread;
    private boolean running = true;
    private HPBar hpBar1, hpBar2;
    private List<Projectile> projectiles = new ArrayList<>();

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(1280, 720));

        background = new Background();
        initHeroes();
        initControls();

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initHeroes() {
        List<Projectile> sharedProjectiles = projectiles;

        Heroes heroes = new Heroes(sharedProjectiles);
        hero1 = heroes.ichigo;
        hero2 = heroes.zangetsu;

        int groundTopY = background.getGroundTopY();
        int groundBottomY = background.getGroundBottomY();

        hero1.getMovement().x = 200;
        hero1.getMovement().y = groundBottomY - 50;
        hero1.getMovement().groundTop = groundTopY + 40;
        hero1.getMovement().groundBottom = groundBottomY + 70;
        hero1.getMovement().groundLeft = 0;
        hero1.getMovement().groundRight = 1280;

        hero2.getMovement().x = 1000;
        hero2.getMovement().y = groundBottomY - 50;
        hero2.getMovement().groundTop = groundTopY + 40;
        hero2.getMovement().groundBottom = groundBottomY + 70;
        hero2.getMovement().groundLeft = 0;
        hero2.getMovement().groundRight = 1280;

        hero1.movingRight = true;
        hero2.movingRight = false;

        hpBar1 = new HPBar(25, 50, 300, 20);
        hpBar2 = new HPBar(950, 50, 300, 20);

        hero1.addObserver(hpBar1);
        hero2.addObserver(hpBar2);
    }


    private void initControls() {
        addKeyListener(new KeyAdapterImpl(hero1, hero2));
    }

    private void checkAttacks() {
        if (hero1.isAttacking()) hero1.setTarget(hero2);
        if (hero2.isAttacking()) hero2.setTarget(hero1);
    }

    @Override
    public void run() {
        while (running) {
            hero1.update();
            hero2.update();

            for (Projectile p : projectiles) {
                p.update();
            }
            projectiles.removeIf(p -> !p.active);

            checkAttacks();

            repaint();

            try { Thread.sleep(16); } catch (InterruptedException ignored) { }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        background.drawBackground(g, getWidth());

        BaseHero first, second;

        if (hero1.getMovement().y < hero2.getMovement().y) {
            first = hero1;
            second = hero2;
        } else {
            first = hero2;
            second = hero1;
        }

        first.draw(g);
        second.draw(g);


        for (Projectile p : projectiles) {
            p.draw(g);
        }

        background.drawForeground(g, getWidth());

        hpBar1.draw(g, hero1);
        hpBar2.draw(g, hero2);
    }
}
