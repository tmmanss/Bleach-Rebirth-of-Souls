package core;

import heroes.BaseHero;
import strategy.AttackStrategy;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

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

    private ComboKeyTracker p1J = new ComboKeyTracker();
    private ComboKeyTracker p1K = new ComboKeyTracker();
    private ComboKeyTracker p1L = new ComboKeyTracker();

    // Combo trackers for Player 2
    private ComboKeyTracker p2J = new ComboKeyTracker();
    private ComboKeyTracker p2K = new ComboKeyTracker();
    private ComboKeyTracker p2L = new ComboKeyTracker();

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


        // Legacy attack keys - use first attack from list if available
        if (key == Controls.P1_ATTACK) {
            List<AttackStrategy> melee = hero1.getMeleeAttacks();
            if (melee != null && !melee.isEmpty()) {
                AttackStrategy attack = melee.get(0);
                System.out.println("P1 melee attack: " + (attack instanceof strategy.RangedAttack ? "RANGED (WRONG!)" : "MELEE"));
                hero1.performAttack(hero2, attack);
            } else {
                System.out.println("P1 has no melee attacks!");
            }
        }
        if (key == Controls.P1_RANGED_ATTACK) {
            List<AttackStrategy> ranged = hero1.getRangedAttacks();
            if (ranged != null && !ranged.isEmpty()) {
                AttackStrategy attack = ranged.get(0);
                System.out.println("P1 ranged attack: " + (attack instanceof strategy.RangedAttack ? "RANGED" : "MELEE (WRONG!)"));
                hero1.performAttack(hero2, attack);
            } else {
                System.out.println("P1 has no ranged attacks!");
            }
        }

        // === PLAYER 1 COMBO ATTACKS ===
        // J = melee combo
        if (key == Controls.P1_J) {
            int combo = p1J.registerPress();
            AttackStrategy attack = hero1.getComboAttack(combo, hero1.getMeleeAttacks());
            if (attack != null) {
                hero1.performAttack(hero2, attack);
            }
        }

        // K = ranged combo
        if (key == Controls.P1_K) {
            int combo = p1K.registerPress();
            AttackStrategy attack = hero1.getComboAttack(combo, hero1.getRangedAttacks());
            if (attack != null) {
                hero1.performAttack(hero2, attack);
            }
        }

        // L = melee combo (same as J)
        if (key == Controls.P1_L) {
            int combo = p1L.registerPress();
            AttackStrategy attack = hero1.getComboAttack(combo, hero1.getMeleeAttacks());
            if (attack != null) {
                hero1.performAttack(hero2, attack);
            }
        }

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

        // Legacy attack keys - use first attack from list if available
        if (key == Controls.P2_ATTACK) {
            List<AttackStrategy> melee = hero2.getMeleeAttacks();
            if (melee != null && !melee.isEmpty()) {
                hero2.performAttack(hero1, melee.get(0));
            }
        }
        if (key == Controls.P2_RANGED_ATTACK) {
            List<AttackStrategy> ranged = hero2.getRangedAttacks();
            if (ranged != null && !ranged.isEmpty()) {
                hero2.performAttack(hero1, ranged.get(0));
            }
        }

        // === PLAYER 2 COMBO ATTACKS ===
        // J = melee combo
        if (key == Controls.P2_J) {
            int combo = p2J.registerPress();
            AttackStrategy attack = hero2.getComboAttack(combo, hero2.getMeleeAttacks());
            if (attack != null) {
                hero2.performAttack(hero1, attack);
            }
        }

        // K = ranged combo
        if (key == Controls.P2_K) {
            int combo = p2K.registerPress();
            AttackStrategy attack = hero2.getComboAttack(combo, hero2.getRangedAttacks());
            if (attack != null) {
                hero2.performAttack(hero1, attack);
            }
        }

        // L = melee combo (same as J)
        if (key == Controls.P2_L) {
            int combo = p2L.registerPress();
            AttackStrategy attack = hero2.getComboAttack(combo, hero2.getMeleeAttacks());
            if (attack != null) {
                hero2.performAttack(hero1, attack);
            }
        }
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
