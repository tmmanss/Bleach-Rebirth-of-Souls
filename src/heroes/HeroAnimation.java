package heroes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeroAnimation {
    public BufferedImage[] idleFrames;
    public BufferedImage[] standFrames;
    public BufferedImage[] runFrames;
    public BufferedImage[] attackFrames;
    public BufferedImage[] rangedAttackFrames;
    public BufferedImage dashFrame;

    public HeroAnimation(String spriteFolder) {
        try {
            this.idleFrames = loadFrames(spriteFolder + "idle/");
            this.standFrames = loadFrames(spriteFolder + "stand/");
            this.runFrames = loadFrames(spriteFolder + "run/");
            this.attackFrames = loadFrames(spriteFolder + "attack/");
            this.rangedAttackFrames = loadFrames(spriteFolder + "ranged_attack/");
            this.dashFrame = ImageIO.read(new File(spriteFolder + "dash.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error for " + spriteFolder);
        }
    }
public HeroAnimation(BufferedImage[] idle,
                     BufferedImage[] stand,
                     BufferedImage[] run,
                     BufferedImage[] attack,
                     BufferedImage[] rangedAttack,
                     BufferedImage dash
) { this.idleFrames = idle;
    this.standFrames = stand;
    this.runFrames = run;
    this.attackFrames = attack;
    this.rangedAttackFrames = rangedAttack;
    this.dashFrame = dash;

        }
        private BufferedImage[] loadFrames(String folderPath) throws IOException {
            File folder = new File(folderPath);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
            if (files == null || files.length == 0) {
                return new BufferedImage[0];
            }

            BufferedImage[] frames = new BufferedImage[files.length];
            for (int i = 0; i < files.length; i++) {
                frames[i] = ImageIO.read(files[i]);
            }
            return frames;
        }
}

