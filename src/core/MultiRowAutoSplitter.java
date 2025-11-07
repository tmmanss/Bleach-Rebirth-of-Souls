package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MultiRowAutoSplitter {

    /**
     * Разбивает sprite sheet на кадры по рядам и столбцам, автоматически определяя ряды.
     * Сначала идёт первый ряд слева-направо, потом второй и т.д.
     *
     * @param sheet    Исходный спрайт-лист
     * @param minAlpha Порог прозрачности для определения пустых пикселей
     * @return массив всех кадров по порядку
     */
    public static BufferedImage[] split(BufferedImage sheet, int minAlpha) {
        List<BufferedImage> frames = new ArrayList<>();

        List<int[]> rows = findRows(sheet, minAlpha);
        System.out.println("Найдено рядов: " + rows.size());

        for (int i = 0; i < rows.size(); i++) {
            int[] row = rows.get(i);
            int yStart = row[0];
            int rowHeight = row[1];
            System.out.println("Ряд " + i + ": y=" + yStart + ", высота=" + rowHeight);

            BufferedImage rowImage = sheet.getSubimage(0, yStart, sheet.getWidth(), rowHeight);
            List<BufferedImage> rowFrames = splitRow(rowImage, minAlpha);
            System.out.println("В ряду " + i + " найдено спрайтов: " + rowFrames.size());

            frames.addAll(rowFrames);
        }

        return frames.toArray(new BufferedImage[0]);
    }

    // Находит ряды: возвращает список [yStart, rowHeight]
    private static List<int[]> findRows(BufferedImage sheet, int minAlpha) {
        List<int[]> rows = new ArrayList<>();

        int width = sheet.getWidth();
        int height = sheet.getHeight();
        int startY = -1;

        for (int y = 0; y < height; y++) {
            boolean emptyRow = true;
            for (int x = 0; x < width; x++) {
                int alpha = (sheet.getRGB(x, y) >> 24) & 0xff;
                if (alpha > minAlpha) {
                    emptyRow = false;
                    break;
                }
            }

            if (emptyRow) {
                if (startY != -1) {
                    int rowHeight = y - startY;
                    if (rowHeight > 2) rows.add(new int[]{startY, rowHeight});
                    startY = -1;
                }
            } else {
                if (startY == -1) startY = y;
            }
        }

        if (startY != -1) {
            int rowHeight = height - startY;
            rows.add(new int[]{startY, rowHeight});
        }

        return rows;
    }

    // Разбивает одну строку на кадры по пустым вертикальным столбцам
    private static List<BufferedImage> splitRow(BufferedImage row, int minAlpha) {
        List<BufferedImage> frames = new ArrayList<>();

        int width = row.getWidth();
        int height = row.getHeight();
        int startX = -1;

        for (int x = 0; x < width; x++) {
            boolean emptyColumn = true;
            for (int y = 0; y < height; y++) {
                int alpha = (row.getRGB(x, y) >> 24) & 0xff;
                if (alpha > minAlpha) {
                    emptyColumn = false;
                    break;
                }
            }

            if (emptyColumn) {
                if (startX != -1) {
                    int frameWidth = x - startX;
                    if (frameWidth > 2) {
                        BufferedImage rawFrame = row.getSubimage(startX, 0, frameWidth, height);
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
            BufferedImage rawFrame = row.getSubimage(startX, 0, frameWidth, height);
            frames.add(trimVertical(rawFrame, minAlpha));
        }

        return frames;
    }

    // Отсекаем пустые пиксели сверху/снизу
    private static BufferedImage trimVertical(BufferedImage frame, int minAlpha) {
        int top = 0;
        int bottom = frame.getHeight() - 1;
        int width = frame.getWidth();
        int height = frame.getHeight();

        outerTop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int alpha = (frame.getRGB(x, y) >> 24) & 0xff;
                if (alpha > minAlpha) {
                    top = y;
                    break outerTop;
                }
            }
        }

        outerBottom:
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int alpha = (frame.getRGB(x, y) >> 24) & 0xff;
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
