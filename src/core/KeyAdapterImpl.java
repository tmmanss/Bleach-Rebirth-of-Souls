package core;

import heroes.BaseHero;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyAdapterImpl extends KeyAdapter {
    private final BaseHero hero1;
    private final BaseHero hero2;

    private long lastLeftPressP1 = 0, lastRightPressP1 = 0;
    private long lastLeftPressP2 = 0, lastRightPressP2 = 0;
    private boolean leftPressedP1 = false;
    private boolean rightPressedP1 = false;
    private boolean leftPressedP2 = false;
    private boolean rightPressedP2 = false;

    private final int doubleTapThreshold = 250;

    public KeyAdapterImpl(BaseHero hero1, BaseHero hero2) {
        this.hero1 = hero1;
        this.hero2 = hero2;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // --- HERO 1 ---
        if (key == Controls.P1_UP) hero1.up = true;
        if (key == Controls.P1_DOWN) hero1.down = true;

        if (key == Controls.P1_LEFT) {
            hero1.left = true;
            if (!leftPressedP1) {
                long now = System.currentTimeMillis();
                if (now - lastLeftPressP1 < doubleTapThreshold) {
                    hero1.performDash(false); // рывок влево
                }
                lastLeftPressP1 = now;
            }
            leftPressedP1 = true;
        }

        if (key == Controls.P1_RIGHT) {
            hero1.right = true;
            if (!rightPressedP1) {
                long now = System.currentTimeMillis();
                if (now - lastRightPressP1 < doubleTapThreshold) {
                    hero1.performDash(true);
                }
                lastRightPressP1 = now;
            }
            rightPressedP1 = true;
        }


        if (key == Controls.P1_ATTACK) hero1.performAttack(hero2, hero1.getMeleeStrategy());
        if (key == Controls.P1_RANGED_ATTACK) hero1.performAttack(hero2, hero1.getRangedStrategy());

        // --- HERO 2 ---
        if (key == Controls.P2_UP) hero2.up = true;
        if (key == Controls.P2_DOWN) hero2.down = true;

        if (key == Controls.P2_LEFT) {
            hero2.left = true;
            if (!leftPressedP2) {
                long now = System.currentTimeMillis();
                if (now - lastLeftPressP2 < doubleTapThreshold) {
                    hero2.performDash(false);
                }
                lastLeftPressP2 = now;
            }
            leftPressedP2 = true;
        }

        if (key == Controls.P2_RIGHT) {
            hero2.right = true;
            if (!rightPressedP2) {
                long now = System.currentTimeMillis();
                if (now - lastRightPressP2 < doubleTapThreshold) {
                    hero2.performDash(true); // рывок вправо
                }
                lastRightPressP2 = now;
            }
            rightPressedP2 = true;
        }

        if (key == Controls.P2_ATTACK) hero2.performAttack(hero1, hero2.getMeleeStrategy());
        if (key == Controls.P2_RANGED_ATTACK) hero2.performAttack(hero1, hero2.getRangedStrategy());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == Controls.P1_UP) hero1.up = false;
        if (key == Controls.P1_DOWN) hero1.down = false;
        if (key == Controls.P1_LEFT) {
            hero1.left = false;
            leftPressedP1 = false;
        }
        if (key == Controls.P1_RIGHT) {
            hero1.right = false;
            rightPressedP1 = false;
        }


        if (key == Controls.P2_UP) hero2.up = false;
        if (key == Controls.P2_DOWN) hero2.down = false;
        if (key == Controls.P2_LEFT) {
            hero2.left = false;
            leftPressedP2 = false;
        }
        if (key == Controls.P2_RIGHT) {
            hero2.right = false;
            rightPressedP2 = false;
        }

    }
}
