package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FrameSplitter {

    public static BufferedImage[] splitByTransparentGaps(BufferedImage sheet, int minAlpha) {
        List<BufferedImage> frames = new ArrayList<>();

        int width = sheet.getWidth();
        int height = sheet.getHeight();
        int startX = -1;

        for (int x = 0; x < width; x++) {
            boolean emptyColumn = true;
            for (int y = 0; y < height; y++) {
                int pixel = sheet.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha > minAlpha) {
                    emptyColumn = false;
                    break;
                }
            }

            if (emptyColumn) {
                if (startX != -1) {
                    int frameWidth = x - startX;
                    if (frameWidth > 2) {
                        BufferedImage rawFrame = sheet.getSubimage(startX, 0, frameWidth, height);
                        frames.add(trimVertical(rawFrame, minAlpha));
                    }
                    startX = -1;
                }
            } else {
                if (startX == -1) startX = x;
            }
        }

        if (startX != -1) {
            int frameWidth = width - startX;
            BufferedImage rawFrame = sheet.getSubimage(startX, 0, frameWidth, height);
            frames.add(trimVertical(rawFrame, minAlpha));
        }

        return frames.toArray(new BufferedImage[0]);
    }

    private static BufferedImage trimVertical(BufferedImage frame, int minAlpha) {
        int top = 0;
        int bottom = frame.getHeight() - 1;
        int width = frame.getWidth();
        int height = frame.getHeight();

        outerTop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = frame.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha > minAlpha) {
                    top = y;
                    break outerTop;
                }
            }
        }

        outerBottom:
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int pixel = frame.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha > minAlpha) {
                    bottom = y;
                    break outerBottom;
                }
            }
        }

        int newHeight = bottom - top + 1;
        if (newHeight <= 0) return frame;

        return frame.getSubimage(0, top, width, newHeight);
    }
}
