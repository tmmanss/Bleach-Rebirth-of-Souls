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
                System.err.println("ERROR: Image file not found: /" + path);
                return null;
            }
            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
                System.err.println("ERROR: Could not decode image: /" + path);
            }
            return image;
        } catch (IOException e) {
            System.err.println("ERROR loading image: /" + path);
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage[] loadFrames(String path) {
        try {
            java.io.InputStream stream = SpriteLoader.class.getResourceAsStream("/" + path);
            if (stream == null) {
                System.err.println("ERROR: Frames file not found: /" + path);
                return new BufferedImage[0];
            }
            BufferedImage sheet = ImageIO.read(stream);
            if (sheet == null) {
                System.err.println("ERROR: Could not decode frames: /" + path);
                return new BufferedImage[0];
            }
            return FrameSplitter.splitByTransparentGaps(sheet, 10);
        } catch (Exception e) {
            System.err.println("ERROR loading frames: /" + path);
            e.printStackTrace();
            return new BufferedImage[0];
        }
    }

    public static BufferedImage[] loadFramesMultiRow(String path) {
        try {
            java.io.InputStream stream = SpriteLoader.class.getResourceAsStream("/" + path);
            if (stream == null) {
                System.err.println("ERROR: Multi-row frames file not found: /" + path);
                return new BufferedImage[0];
            }
            BufferedImage sheet = ImageIO.read(stream);
            if (sheet == null) {
                System.err.println("ERROR: Could not decode multi-row frames: /" + path);
                return new BufferedImage[0];
            }
            return MultiRowAutoSplitter.split(sheet, 10);
        } catch (Exception e) {
            System.err.println("ERROR loading multi-row frames: /" + path);
            e.printStackTrace();
            return new BufferedImage[0];
        }
    }
}
