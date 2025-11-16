package heroes;

import core.FrameSplitter;
import core.MultiRowAutoSplitter;
import strategy.AttackStrategy;
import strategy.Projectile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeroBuilder {

    private String name;
    private double attackRange;
    private int reiatsu;
    private String basePath;

    private HeroMovement movement = new HeroMovement();
    private List<AttackStrategy> meleeAttacks = new ArrayList<>();
    private List<AttackStrategy> rangedAttacks = new ArrayList<>();
    private List<Projectile> sharedProjectiles;

    public HeroBuilder setName(String name) { this.name = name; return this; }
    public HeroBuilder setAttackRange(double range) { this.attackRange = range; return this; }
    public HeroBuilder setReiatsu(int reiatsu) { this.reiatsu = reiatsu; return this; }

    public HeroBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public HeroBuilder setProjectiles(List<Projectile> list) {
        this.sharedProjectiles = list;
        return this;
    }

    public HeroBuilder setMeleeAttacks(List<AttackStrategy> list) {
        if (list != null) {
            this.meleeAttacks = new ArrayList<>(list);
        }
        return this;
    }

    public HeroBuilder setRangedAttacks(List<AttackStrategy> list) {
        if (list != null) {
            this.rangedAttacks = new ArrayList<>(list);
        }
        return this;
    }

    public HeroBuilder addAttack(AttackStrategy strategy) {
        if (strategy != null) {
            if (strategy instanceof strategy.RangedAttack) {
                this.rangedAttacks.add(strategy);
            } else {
                this.meleeAttacks.add(strategy);
            }
        }
        return this;
    }

    public HeroBuilder addAttacks(List<AttackStrategy> strategies) {
        if (strategies != null) {
            for (AttackStrategy a : strategies) {
                addAttack(a);
            }
        }
        return this;
    }

    public BaseHero build() {

        try {
            BufferedImage idle = ImageIO.read(getClass().getResourceAsStream("/" + basePath + "/idle.png"));
            BufferedImage stand = ImageIO.read(getClass().getResourceAsStream("/" + basePath + "/stand.png"));
            BufferedImage run = ImageIO.read(getClass().getResourceAsStream("/" + basePath + "/run.png"));
            BufferedImage dash = ImageIO.read(getClass().getResourceAsStream("/" + basePath + "/dash.png"));

            BufferedImage[] idleFrames = FrameSplitter.splitByTransparentGaps(idle, 10);
            BufferedImage[] standFrames = MultiRowAutoSplitter.split(stand, 10);
            BufferedImage[] runFrames = FrameSplitter.splitByTransparentGaps(run, 10);

            HeroAnimation animation = new HeroAnimation(
                    idleFrames,
                    standFrames,
                    runFrames,
                    null,
                    null,
                    dash
            );

            BaseHero hero = new BaseHero(animation, movement);

            hero.setName(name);
            hero.setReiatsu(reiatsu);
            hero.setAttackRange(attackRange);

            hero.setMeleeAttacks(meleeAttacks);
            hero.setRangedAttacks(rangedAttacks);

            return hero;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
