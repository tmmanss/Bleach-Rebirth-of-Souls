package core;

import core.FrameSplitter;
import core.MultiRowAutoSplitter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteLoader {

    public static BufferedImage loadImage(String path) {
        try {
            java.io.InputStream stream = SpriteLoader.class.getResourceAsStream("/" + path);
            if (stream == null) {
                return null;
            }
            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
            }
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage[] loadFrames(String path) {
        try {
            java.io.InputStream stream = SpriteLoader.class.getResourceAsStream("/" + path);
            if (stream == null) {
                return new BufferedImage[0];
            }
            BufferedImage sheet = ImageIO.read(stream);
            if (sheet == null) {
                return new BufferedImage[0];
            }
            return FrameSplitter.splitByTransparentGaps(sheet, 10);
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage[0];
        }
    }

    public static BufferedImage[] loadFramesMultiRow(String path) {
        try {
            java.io.InputStream stream = SpriteLoader.class.getResourceAsStream("/" + path);
            if (stream == null) {
                return new BufferedImage[0];
            }
            BufferedImage sheet = ImageIO.read(stream);
            if (sheet == null) {
                return new BufferedImage[0];
            }
            return MultiRowAutoSplitter.split(sheet, 10);
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage[0];
        }
    }
}
