package heroes;

import core.FrameSplitter;
import core.MultiRowAutoSplitter;
import strategy.AttackStrategy;
import strategy.MeleeAttack;
import strategy.Projectile;
import strategy.RangedAttack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeroBuilder {
    private String name;
    private double attackRange;
    private int reiatsu;
    private String basePath;
    private HeroMovement movement = new HeroMovement();
    private AttackStrategy rangedAttackStrategy;
    private AttackStrategy meleeAttackStrategy;
    private List<Projectile> sharedProjectiles;



    public HeroBuilder setName(String name){
        this.name = name;
        return this;
    }

    public HeroBuilder setAttackRange(double attackRange){
        this.attackRange = attackRange;
        return this;
    }

    public HeroBuilder setReiatsu(int reiatsu){
        this.reiatsu = reiatsu;
        return this;
    }


    public HeroBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public HeroBuilder setMovement(HeroMovement movement) {
        this.movement = movement;
        return this;
    }

    public HeroBuilder setRangedAttackStrategy(AttackStrategy strategy) {
        this.rangedAttackStrategy = strategy;
        return this;
    }
    public HeroBuilder setMeleeAttackStrategy(AttackStrategy strategy) {
        this.meleeAttackStrategy = strategy;
        return this;
    }

    public HeroBuilder setProjectiles(List<Projectile> projectiles) {
        this.sharedProjectiles = projectiles;
        return this;
    }


    public BaseHero build() {
        try {
            BufferedImage idle = ImageIO.read(new File(basePath + "/idle.png"));
            BufferedImage run = ImageIO.read(new File(basePath + "/run.png"));
            BufferedImage attack = ImageIO.read(new File(basePath + "/attack1.png"));
            BufferedImage rangedAttack = ImageIO.read(new File(basePath + "/range_attack.png"));
            BufferedImage projectileSprite = ImageIO.read(new File(basePath + "/projectile.png"));

            List<Projectile> projectileList = (sharedProjectiles != null)
                    ? sharedProjectiles
                    : new ArrayList<>();

            BufferedImage[] idleFrames = FrameSplitter.splitByTransparentGaps(idle, 10);
            BufferedImage[] runFrames = FrameSplitter.splitByTransparentGaps(run, 10);
            BufferedImage[] attackFrames = FrameSplitter.splitByTransparentGaps(attack, 10);
            BufferedImage[] rangedAttackFrames = MultiRowAutoSplitter.split(rangedAttack, 10);


            HeroAnimation animation = new HeroAnimation(idleFrames, null, runFrames, attackFrames, rangedAttackFrames);
            BaseHero hero = new BaseHero(animation, movement);

            hero.setName(name);
            hero.setReiatsu(reiatsu);
            hero.setAttackRange(attackRange);
            if (meleeAttackStrategy == null) {
                meleeAttackStrategy = new MeleeAttack(attackFrames);
            }
            if (rangedAttackStrategy == null) {
                rangedAttackStrategy = new RangedAttack(rangedAttackFrames, projectileList, projectileSprite);
            }
            hero.setRangedStrategy(rangedAttackStrategy);
            hero.setMeleeStrategy(meleeAttackStrategy);
            return hero;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
