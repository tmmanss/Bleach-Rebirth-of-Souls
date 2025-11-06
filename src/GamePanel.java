import core.Background;
import core.Controls;
import core.KeyAdapterImpl;
import heroes.*;
import strategy.MeleeAttack;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private BaseHero hero1;
    private BaseHero hero2;
    private Background background;
    private Thread gameThread;
    private boolean running = true;

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

        drawHPBars(g);
    }

    private void drawHPBars(Graphics g) {
        int hpWidth1 = (int) ((hero1.getReiatsu() / 1000.0) * 300);
        g.setColor(Color.RED);
        g.fillRect(25, 50, hpWidth1, 20);
        g.setColor(Color.WHITE);
        g.drawRect(25, 50, 300, 20);
        g.drawString(hero1.getName() + " HP: " + hero1.getReiatsu(), 25, 45);

        int hpWidth2 = (int) ((hero2.getReiatsu() / 1000.0) * 300);
        g.setColor(Color.RED);
        g.fillRect(950, 50, hpWidth2, 20);
        g.setColor(Color.WHITE);
        g.drawRect(950, 50, 300, 20);
        g.drawString(hero2.getName() + " HP: " + hero2.getReiatsu(), 950, 45);
    }
}
