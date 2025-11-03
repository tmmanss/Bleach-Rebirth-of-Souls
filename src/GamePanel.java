import core.Controls;
import heroes.BaseHero;
import heroes.IchigoHero;
import heroes.ZangetsuHero;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    private BaseHero hero1;
    private BaseHero hero2;
    private Thread gameThread;
    private boolean running = true;

    // 🔹 Фоновые изображения
    private BufferedImage sky;
    private BufferedImage trees;
    private BufferedImage ground;
    private BufferedImage bushes;

    // 🔹 Параметры сцены
    private int groundTopY;
    private int groundBottomY;
    private int groundLeftX;
    private int groundRightX;

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(1280, 720));

        loadBackground();
        initHeroes();
        initControls();

        gameThread = new Thread(this);
        gameThread.start();
    }

    // ----------------- 🔹 Загрузка фона -----------------
    private void loadBackground() {
        try {
            sky = ImageIO.read(new File("assets/stage/forest_sky.png"));
            trees = ImageIO.read(new File("assets/stage/forest_trees.png"));
            ground = ImageIO.read(new File("assets/stage/forest_ground.png"));
            bushes = ImageIO.read(new File("assets/stage/forest_bush.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int treesY = 250;
        groundTopY = treesY + 216;
        groundBottomY = groundTopY + ground.getHeight() - bushes.getHeight();
        groundLeftX = 0;
        groundRightX = 1280;
    }

    // ----------------- 🔹 Инициализация героев -----------------
    private void initHeroes() {
        hero1 = new IchigoHero();
        hero1.getMovement().x = 200;
        hero1.getMovement().y = 500;
        hero1.getMovement().groundTop = groundTopY - 20;
        hero1.getMovement().groundBottom = groundBottomY;
        hero1.getMovement().groundLeft = groundLeftX;
        hero1.getMovement().groundRight = groundRightX;

        hero2 = new ZangetsuHero();
        hero2.getMovement().x = 800;
        hero2.getMovement().y = 500;
        hero2.getMovement().groundTop = groundTopY - 20;
        hero2.getMovement().groundBottom = groundBottomY;
        hero2.getMovement().groundLeft = groundLeftX;
        hero2.getMovement().groundRight = groundRightX;
    }

    // ----------------- 🔹 Управление -----------------
    private void initControls() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                // 🧍‍♂️ Hero 1
                if (key == Controls.P1_UP) hero1.up = true;
                if (key == Controls.P1_DOWN) hero1.down = true;
                if (key == Controls.P1_LEFT) hero1.left = true;
                if (key == Controls.P1_RIGHT) hero1.right = true;
                if (key == Controls.P1_ATTACK) hero1.startAttack();

                // 🧍‍♂️ Hero 2
                if (key == Controls.P2_UP) hero2.up = true;
                if (key == Controls.P2_DOWN) hero2.down = true;
                if (key == Controls.P2_LEFT) hero2.left = true;
                if (key == Controls.P2_RIGHT) hero2.right = true;
                if (key == Controls.P2_ATTACK) hero2.startAttack();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();

                // 🧍‍♂️ Hero 1
                if (key == Controls.P1_UP) hero1.up = false;
                if (key == Controls.P1_DOWN) hero1.down = false;
                if (key == Controls.P1_LEFT) hero1.left = false;
                if (key == Controls.P1_RIGHT) hero1.right = false;

                // 🧍‍♂️ Hero 2
                if (key == Controls.P2_UP) hero2.up = false;
                if (key == Controls.P2_DOWN) hero2.down = false;
                if (key == Controls.P2_LEFT) hero2.left = false;
                if (key == Controls.P2_RIGHT) hero2.right = false;
            }
        });
    }

    // ----------------- 🔹 Игровой цикл -----------------
    @Override
    public void run() {
        while (running) {
            hero1.update();
            hero2.update();
            repaint();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException ignored) { }
        }
    }

    // ----------------- 🔹 Отрисовка -----------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);

        // Рисуем героев поверх
        hero1.draw(g);
        hero2.draw(g);
    }

    // ----------------- 🔹 Рисуем фон -----------------
    private void drawBackground(Graphics g) {
        int panelWidth = getWidth();
        int x = 0;

        // 🌄 Небо
        while (x < panelWidth) {
            g.drawImage(sky, x, 0, null);
            x += sky.getWidth();
        }

        // 🌲 Деревья
        int treesY = 250;
        x = 0;
        while (x < panelWidth) {
            g.drawImage(trees, x, treesY, null);
            x += trees.getWidth();
        }

        // 🌾 Земля
        x = 0;
        while (x < panelWidth) {
            g.drawImage(ground, x, groundTopY, null);
            x += ground.getWidth();
        }

        // 🌿 Кусты
        int bushesY = groundTopY + ground.getHeight() - 64;
        x = 0;
        while (x < panelWidth) {
            g.drawImage(bushes, x, bushesY, null);
            x += bushes.getWidth();
        }
    }
}
