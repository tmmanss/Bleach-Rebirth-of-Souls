package heroes;

import core.FrameSplitter;
import strategy.AttackStrategy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeroBuilder {
    private String name;
    private double attackRange;
    private int reiatsu;
    private String basePath;
    private HeroMovement movement = new HeroMovement();
    private AttackStrategy attackStrategy;

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

    public HeroBuilder setAttackStrategy(AttackStrategy strategy) {
        this.attackStrategy = strategy;
        return this;
    }

    public BaseHero build() {
        try {
            BufferedImage idle = ImageIO.read(new File(basePath + "/idle.png"));
            BufferedImage run = ImageIO.read(new File(basePath + "/run.png"));
            BufferedImage attack = ImageIO.read(new File(basePath + "/attack1.png"));

            BufferedImage[] idleFrames = FrameSplitter.splitByTransparentGaps(idle, 10);
            BufferedImage[] runFrames = FrameSplitter.splitByTransparentGaps(run, 10);
            BufferedImage[] attackFrames = FrameSplitter.splitByTransparentGaps(attack, 10);

            HeroAnimation animation = new HeroAnimation(idleFrames, null, runFrames, attackFrames);
            BaseHero hero = new BaseHero(animation, movement);

            hero.setName(name);
            hero.setReiatsu(reiatsu);
            hero.setAttackRange(attackRange);
            hero.setAttackStrategy(attackStrategy);

            return hero;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
