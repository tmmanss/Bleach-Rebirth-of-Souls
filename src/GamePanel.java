import core.Background;
import core.Controls;
import core.KeyAdapterImpl;
import heroes.*;
import observer.HPBar;
import strategy.MeleeAttack;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private BaseHero hero1;
    private BaseHero hero2;
    private Background background;
    private Thread gameThread;
    private boolean running = true;
    private HPBar hpBar1, hpBar2;

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
        Heroes heroes = new Heroes();
        hero1 = heroes.ichigo;
        hero2 = heroes.zangetsu;

        int groundTopY = background.getGroundTopY();
        int groundBottomY = background.getGroundBottomY();

        hero1.getMovement().x = 200;
        hero1.getMovement().y = 500;
        hero1.getMovement().groundTop = groundTopY - 20;
        hero1.getMovement().groundBottom = groundBottomY;
        hero1.getMovement().groundLeft = 0;
        hero1.getMovement().groundRight = 1280;

        hero2.getMovement().x = 800;
        hero2.getMovement().y = 500;
        hero2.getMovement().groundTop = groundTopY - 20;
        hero2.getMovement().groundBottom = groundBottomY;
        hero2.getMovement().groundLeft = 0;
        hero2.getMovement().groundRight = 1280;

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

            checkAttacks();

            repaint();

            try { Thread.sleep(16); } catch (InterruptedException ignored) { }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.draw(g, getWidth());

        hero1.draw(g);
        hero2.draw(g);

        hpBar1.draw(g, hero1);
        hpBar2.draw(g, hero2);
    }


}
