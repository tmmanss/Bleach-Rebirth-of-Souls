package core;
import core.Background;
import core.KeyAdapterImpl;
import heroes.*;
import heroes.factory.HeroFactory;
import heroes.factory.HeroFactoryGet;
import observer.HPBar;
import strategy.Projectile;
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
    private long battleStartTime;
    private boolean battleStarted = false;
    private List<Projectile> projectiles = new ArrayList<>();
    private boolean gameOver = false;
    private boolean dialogShown = false;

    public GamePanel(BaseHero hero1, BaseHero hero2,List<Projectile> projectileList) {
        setFocusable(true);
        setPreferredSize(new Dimension(1280, 720));

        this.hero1 = hero1;
        this.hero2 = hero2;
        this.projectiles = projectileList;

        background = new Background();
        initControls();
        initHPBars();

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void initPositions() {
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
    }
    private void initHPBars() {
        hpBar1 = new HPBar(25, 50, 300, 20);
        hpBar2 = new HPBar(950, 50, 300, 20);

        hero1.addObserver(hpBar1);
        hero2.addObserver(hpBar2);
    }
    private BaseHero chooseHero(String playerName, List<Projectile> sharedProjectiles) {
        String[] options = {"Ichigo", "Zangetsu"};
        int choice = JOptionPane.showOptionDialog(this,
                playerName + ", выберите героя:",
                "Выбор героя",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if(choice<0 || choice>=options.length)choice = 0;
        String name = options[choice];
        HeroFactory heroFactory= HeroFactoryGet.getFactory(name);
        return heroFactory.createHero(sharedProjectiles);

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
        battleStartTime = System.currentTimeMillis();

        while (running) {
            long now = System.currentTimeMillis();

            if (!battleStarted) {
                hero1.updateIdleAnimation();
                hero2.updateIdleAnimation();

                if (now - battleStartTime >= 5000) {
                    battleStarted = true;
                    hero1.idleTimeOver = true;
                    hero2.idleTimeOver = true;
                }
            } else if (!gameOver) {
                hero1.update();
                hero2.update();

                for (Projectile p : projectiles) {
                    p.update();
                }
                projectiles.removeIf(p -> !p.active);

                checkAttacks();

                if ((hero1.getReiatsu() <= 0 || hero2.getReiatsu() <= 0) && !dialogShown) {
                    gameOver = true;
                    dialogShown = true;
                    showGameOverDialog();
                }
            }

            repaint();

            try { Thread.sleep(16); } catch (InterruptedException ignored) { }
        }
    }

    private void showGameOverDialog() {
        SwingUtilities.invokeLater(() -> {
            String winner = hero1.getReiatsu() <= 0 ? hero2.getName() + " выиграл!" : hero1.getName() + " выиграл!";

            int result = JOptionPane.showConfirmDialog(this,
                    winner + "\nНачать новый бой?",
                    "Игра окончена",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        });
    }

    private void restartGame() {
        battleStarted = false;
        gameOver = false;
        dialogShown = false;
        battleStartTime = System.currentTimeMillis();
        hero1.getMovement().x = 200;
        hero2.getMovement().x = 1000;
        hero1.setReiatsu(100);
        hero2.setReiatsu(100);
        hero1.idleTimeOver = false;
        hero2.idleTimeOver = false;
        projectiles.clear();

        hero1.reset();
        hero2.reset();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        background.drawBackground(g, getWidth());

        BaseHero first = hero1.getMovement().y < hero2.getMovement().y ? hero1 : hero2;
        BaseHero second = first == hero1 ? hero2 : hero1;

        if (!battleStarted) {
            first.updateIdleAnimation();
            second.updateIdleAnimation();
            first.draw(g);
            second.draw(g);

            long remaining = 5 - (System.currentTimeMillis() - battleStartTime) / 1000;
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("Бой начнётся через: " + remaining, 400, 300);
        } else {
            first.draw(g);
            second.draw(g);

            for (Projectile p : projectiles) {
                p.draw(g);
            }

            background.drawForeground(g, getWidth());

            hpBar1.draw(g, hero1);
            hpBar2.draw(g, hero2);

            if (gameOver) {
                g.setColor(new Color(255, 0, 0, 128)); // полупрозрачный красный
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 80));
                String winner = hero1.getReiatsu() <= 0 ? hero2.getName() + " выиграл!" : hero1.getName() + " выиграл!";
                g.drawString(winner, 400, 300);
            }
        }
    }
}