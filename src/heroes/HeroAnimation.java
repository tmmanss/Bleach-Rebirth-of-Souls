package heroes;

import java.awt.image.BufferedImage;

public class HeroAnimation {
    public BufferedImage[] idleFrames;
    public BufferedImage[] standFrames;
    public BufferedImage[] runFrames;
    public BufferedImage[] attackFrames;
    public BufferedImage[] rangedAttackFrames;
    public BufferedImage dashFrame;


    public HeroAnimation(BufferedImage[] idle,
                         BufferedImage[] stand,
                         BufferedImage[] run,
                         BufferedImage[] attack,
                         BufferedImage[] rangedAttack,
                         BufferedImage dash
    ) {
        this.idleFrames = idle;
        this.standFrames = stand;
        this.runFrames = run;
        this.attackFrames = attack;
        this.rangedAttackFrames = rangedAttack;
        this.dashFrame = dash;
    }
}

