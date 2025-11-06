package core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background {
    private BufferedImage sky, trees, ground, bushes;
    private int groundTopY, groundBottomY;

    public Background() {
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
    }

    public void draw(Graphics g, int panelWidth) {
        int x = 0;
        while (x < panelWidth) {
            g.drawImage(sky, x, 0, null);
            x += sky.getWidth();
        }

        int treesY = 250;
        x = 0;
        while (x < panelWidth) {
            g.drawImage(trees, x, treesY, null);
            x += trees.getWidth();
        }

        x = 0;
        while (x < panelWidth) {
            g.drawImage(ground, x, groundTopY, null);
            x += ground.getWidth();
        }

        int bushesY = groundTopY + ground.getHeight() - 64;
        x = 0;
        while (x < panelWidth) {
            g.drawImage(bushes, x, bushesY, null);
            x += bushes.getWidth();
        }
    }

    public int getGroundTopY() { return groundTopY; }
    public int getGroundBottomY() { return groundBottomY; }
}
