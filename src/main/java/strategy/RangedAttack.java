package strategy;

import heroes.BaseHero;
import java.awt.image.BufferedImage;
import java.util.List;

public class RangedAttack implements AttackStrategy {
    private BufferedImage[] frames;
    private List<Projectile> projectileList;

    private BufferedImage projectileSprite;
    private double projectileSpeed;
    private double range;
    private int damage;
    private int cost;
    private int fireFrame;

    private boolean fired = false;

    public RangedAttack(
            BufferedImage[] frames,
            List<Projectile> projectileList,
            BufferedImage projectileSprite,
            double projectileSpeed,
            double range,
            int damage,
            int cost,
            int fireFrame
    ) {
        this.frames = frames;
        this.projectileList = projectileList;
        this.projectileSprite = projectileSprite;
        this.projectileSpeed = projectileSpeed;
        this.range = range;
        this.damage = damage;
        this.cost = cost;
        this.fireFrame = fireFrame;
    }

    @Override
    public void attack(BaseHero attacker, BaseHero target) {

        int frame = attacker.getAttackFrame();
        
        // Debug output for zangetsu projectile issue
        if (frame == fireFrame) {
            System.out.println("RangedAttack: frame=" + frame + ", fireFrame=" + fireFrame + ", fired=" + fired + 
                             ", frames.length=" + frames.length + ", attacker=" + attacker.getName());
        }

        if (frame == fireFrame && !fired) {
            attacker.reduceReiatsu(cost);
            fireProjectile(attacker, target);
            fired = true;
        }

        if (frame == frames.length - 1) {
            fired = false;
        }
    }

    @Override public BufferedImage[] getAttackFrames() { return frames; }
    @Override public double getRange() { return range; }
    @Override public int getDamage() { return damage; }
    @Override public int getCost() { return cost; }

    public void fireProjectile(BaseHero attacker, BaseHero target) {
        if (projectileSprite == null) {
            System.err.println("ERROR: Cannot fire projectile - projectile sprite is null!");
            return;
        }
        
        System.out.println("Firing projectile: sprite size=" + projectileSprite.getWidth() + "x" + projectileSprite.getHeight() + 
                         ", damage=" + damage + ", speed=" + projectileSpeed);
        
        Projectile p = new Projectile(
                attacker.getX(),
                attacker.getY(),
                target,
                damage,
                projectileSprite,
                attacker.isMovingRight()
        );
        // Set the projectile speed if needed
        p.speed = projectileSpeed;
        projectileList.add(p);
        System.out.println("Projectile added to list. List size: " + projectileList.size());
    }
}

