package heroes;

import core.FrameSplitter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeroBuilder {
    private String basePath;
    private HeroMovement movement = new HeroMovement();

    public HeroBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public HeroBuilder setMovement(HeroMovement movement) {
        this.movement = movement;
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
            return new BaseHero(animation, movement);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
