package heroes;

import java.awt.image.BufferedImage;

public class HeroAnimation {
    public BufferedImage[] idleFrames;
    public BufferedImage[] startRunFrames;
    public BufferedImage[] runFrames;
    public BufferedImage[] attackFrames;
    public BufferedImage[] rangedAttackFrames;


    public HeroAnimation(BufferedImage[] idle, BufferedImage[] startRun,
                         BufferedImage[] run, BufferedImage[] attack, BufferedImage[] rangedAttack) {
        this.idleFrames = idle;
        this.startRunFrames = startRun;
        this.runFrames = run;
        this.attackFrames = attack;
        this.rangedAttackFrames = rangedAttack;
    }
}

