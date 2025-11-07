package core;

import heroes.BaseHero;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyAdapterImpl extends KeyAdapter {
    private final BaseHero hero1;
    private final BaseHero hero2;

    public KeyAdapterImpl(BaseHero hero1, BaseHero hero2) {
        this.hero1 = hero1;
        this.hero2 = hero2;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == Controls.P1_UP) hero1.up = true;
        if (key == Controls.P1_DOWN) hero1.down = true;
        if (key == Controls.P1_LEFT) hero1.left = true;
        if (key == Controls.P1_RIGHT) hero1.right = true;
        if (key == Controls.P1_ATTACK) hero1.performAttack(hero2, hero1.getMeleeStrategy());
        if (key == Controls.P1_RANGED_ATTACK) hero1.performAttack(hero2, hero1.getRangedStrategy());

        if (key == Controls.P2_UP) hero2.up = true;
        if (key == Controls.P2_DOWN) hero2.down = true;
        if (key == Controls.P2_LEFT) hero2.left = true;
        if (key == Controls.P2_RIGHT) hero2.right = true;
        if (key == Controls.P2_ATTACK) hero2.performAttack(hero1, hero2.getMeleeStrategy());
        if (key == Controls.P2_RANGED_ATTACK) hero2.performAttack(hero1, hero2.getRangedStrategy());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == Controls.P1_UP) hero1.up = false;
        if (key == Controls.P1_DOWN) hero1.down = false;
        if (key == Controls.P1_LEFT) hero1.left = false;
        if (key == Controls.P1_RIGHT) hero1.right = false;

        if (key == Controls.P2_UP) hero2.up = false;
        if (key == Controls.P2_DOWN) hero2.down = false;
        if (key == Controls.P2_LEFT) hero2.left = false;
        if (key == Controls.P2_RIGHT) hero2.right = false;
    }
}
