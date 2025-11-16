package strategy;

import heroes.BaseHero;
import java.awt.image.BufferedImage;
import java.util.List;

public class RangedAttack implements AttackStrategy {
    protected BufferedImage[] frames;
    protected List<Projectile> projectileList;

    protected BufferedImage projectileSprite;
    protected double projectileSpeed;
    protected double range;
    protected int damage;
    protected int cost;
    protected int fireFrame;

    protected boolean fired = false;

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
        
        int projectileDamage = getDamage();

        Projectile p = new Projectile(
                attacker.getX(),
                attacker.getY(),
                target,
                projectileDamage,
                projectileSprite,
                attacker.isMovingRight()
        );
        p.speed = projectileSpeed;
        projectileList.add(p);
        System.out.println("Projectile added to list. List size: " + projectileList.size());
    }
}

