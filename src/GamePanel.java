import movements.ichigo.Ichigo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements KeyListener {

    private BufferedImage sky;
    private BufferedImage trees;
    private BufferedImage ground;
    private BufferedImage bushes;

    private Ichigo ichigo;
    private int groundTopY, groundBottomY, groundLeftX, groundRightX;

    public GamePanel() {
        try {
            sky = ImageIO.read(new File("assets/stage/forest_sky.png"));
            trees = ImageIO.read(new File("assets/stage/forest_trees.png"));
            ground = ImageIO.read(new File("assets/stage/forest_ground.png"));
            bushes = ImageIO.read(new File("assets/stage/forest_bush.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(1024, 722));
        setFocusable(true);
        addKeyListener(this);

        // 🔹 Определяем границы ground.png
        int treesY = 250;
        groundTopY = treesY + 216; // Верх ground.png
        groundBottomY = groundTopY + ground.getHeight() - bushes.getHeight(); // Низ ground.png
        groundLeftX = 0; // Левый край ground.png
        groundRightX = 1024; // Правый край ground.png

        ichigo = new Ichigo();
        // 🔹 Устанавливаем границы движения в пределах ground.png
        ichigo.setGroundBounds(groundTopY - 20, groundBottomY, groundLeftX, groundRightX);

        // таймер обновления
        new Timer(16, e -> {
            ichigo.update();
            repaint();
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // рисуем фон
        int panelWidth = getWidth();
        int x = 0;

        // небо
        while (x < panelWidth) {
            g.drawImage(sky, x, 0, null);
            x += sky.getWidth();
        }

        // деревья
        int treesY = 250;
        x = 0;
        while (x < panelWidth) {
            g.drawImage(trees, x, treesY, null);
            x += trees.getWidth();
        }

        // 🔹 земля (ground.png) - здесь будет находиться Ichigo
        x = 0;
        while (x < panelWidth) {
            g.drawImage(ground, x, groundTopY, null);
            x += ground.getWidth();
        }

        // кусты
        int bushesY = groundTopY + ground.getHeight() - 64;
        x = 0;
        while (x < panelWidth) {
            g.drawImage(bushes, x, bushesY, null);
            x += bushes.getWidth();
        }

        // 🔹 Ichigo рисуется поверх ground.png
        ichigo.draw(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ichigo.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ichigo.keyReleased(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}