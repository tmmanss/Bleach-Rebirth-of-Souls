package heroes;

import java.awt.image.BufferedImage;

public class HeroAnimation {
    public BufferedImage[] idleFrames;
    public BufferedImage[] startRunFrames;
    public BufferedImage[] runFrames;
    public BufferedImage[] attackFrames;

    public HeroAnimation(BufferedImage[] idle, BufferedImage[] startRun,
                         BufferedImage[] run, BufferedImage[] attack) {
        this.idleFrames = idle;
        this.startRunFrames = startRun;
        this.runFrames = run;
        this.attackFrames = attack;
    }
}

