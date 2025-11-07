package strategy;

import heroes.BaseHero;
import java.awt.image.BufferedImage;
import java.util.List;

public class RangedAttack implements AttackStrategy {
    private BufferedImage[] frames;
    private List<Projectile> projectileList;
    private BufferedImage projectileSprite;
    private boolean projectileFired = false;


    public RangedAttack(BufferedImage[] frames, List<Projectile> projectileList, BufferedImage sprite) {
        this.frames = frames;
        this.projectileList = projectileList;
        this.projectileSprite = sprite;
    }

    @Override
    public void attack(BaseHero attacker, BaseHero target) {
        attacker.reduceReiatsu(15);
        int attackFrame = attacker.getAttackFrame();
        if (attackFrame == attacker.getAnimation().rangedAttackFrames.length - 1) {
            projectileFired = false;
        }
    }

    public void fireProjectile(BaseHero attacker, BaseHero target) {
        Projectile p = new Projectile(
                attacker.getX(),
                attacker.getY(),
                target,
                60,
                projectileSprite,
                attacker.isMovingRight()
        );
        projectileList.add(p);
        System.out.println(attacker.getName() + " fired a projectile!");
    }

    @Override
    public BufferedImage[] getAttackFrames() {
        return frames;
    }
}
