package movements.ichigo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ichigo {

    private BufferedImage idleSheet;
    private BufferedImage startRunSheet;
    private BufferedImage runSheet;

    private BufferedImage[] idleFrames;
    private BufferedImage[] startRunFrames;
    private BufferedImage[] runFrames;

    private enum State { IDLE, START_RUN, RUN }
    private State currentState = State.IDLE;

    private int currentFrame = 0;
    private int frameDelay = 60;
    private long lastFrameTime = 0;

    private int x = 100, y = 200;
    private int speed = 14;
    private boolean movingRight = true;
    private boolean readyToMove = false;

    // 🔹 Границы движения (будут установлены извне)
    private int groundLevel = 400;
    private int groundTop = 0;
    private int groundLeft = 0;
    private int groundRight = 1024;

    // 🔹 Управление клавишами
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Ichigo() {
        try {
            idleSheet = ImageIO.read(new File("assets/movements/ichigo/idle_ichi.png"));
            startRunSheet = ImageIO.read(new File("assets/movements/ichigo/start_run.png"));
            runSheet = ImageIO.read(new File("assets/movements/ichigo/run_ichi.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        idleFrames = splitFrames(idleSheet, 3, 58, 58);
        startRunFrames = splitFrames(startRunSheet, 7, 73, 58);
        runFrames = splitFrames(runSheet, 8, 86, 50);
    }

    private BufferedImage[] splitFrames(BufferedImage sheet, int frameCount, int frameWidth, int frameHeight) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            int x = i * frameWidth;
            if (x + frameWidth > sheet.getWidth()) break;
            frames[i] = sheet.getSubimage(x, 0, frameWidth, frameHeight);
        }
        return frames;
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime < frameDelay) return;
        lastFrameTime = now;

        boolean anyKeyPressed = upPressed || downPressed || leftPressed || rightPressed;

        switch (currentState) {
            case IDLE -> {
                currentFrame = (currentFrame + 1) % idleFrames.length;
                if (anyKeyPressed) {
                    currentState = State.START_RUN;
                    currentFrame = 0;
                    readyToMove = false;
                }
            }
            case START_RUN -> {
                currentFrame++;
                if (currentFrame >= startRunFrames.length) {
                    currentFrame = 0;
                    currentState = State.RUN;
                    readyToMove = true;
                }
            }
            case RUN -> {
                currentFrame = (currentFrame + 1) % runFrames.length;
                if (!anyKeyPressed) {
                    currentState = State.IDLE;
                    currentFrame = 0;
                    readyToMove = false;
                }
            }
        }

        // 🔹 Обновляем направление движения сразу при нажатии клавиш
        if (leftPressed && !rightPressed) {
            movingRight = false;
        } else if (rightPressed && !leftPressed) {
            movingRight = true;
        }

        // 🔹 Движение во всех направлениях с ограничениями
        if (currentState == State.RUN && readyToMove) {
            int newX = x;
            int newY = y;

            int dx = 0;
            int dy = 0;

            if (leftPressed) dx -= speed;
            if (rightPressed) dx += speed;
            if (upPressed) dy -= speed;
            if (downPressed) dy += speed;

            // 🔹 Нормализация диагонального движения
            if (dx != 0 && dy != 0) {
                dx = (int)(dx * 0.707);
                dy = (int)(dy * 0.707);
            }

            newX += dx;
            newY += dy;

            // 🔹 Ограничение движения в пределах ground
            int frameWidth = getCurrentFrameWidth();
            int frameHeight = getCurrentFrameHeight();

            // Горизонтальные границы
            if (newX < groundLeft) newX = groundLeft;
            if (newX + frameWidth > groundRight) newX = groundRight - frameWidth;

            // Вертикальные границы
            if (newY < groundTop) newY = groundTop;
            if (newY + frameHeight > groundLevel) newY = groundLevel - frameHeight;

            x = newX;
            y = newY;
        }
    }

    private int getCurrentFrameWidth() {
        return switch (currentState) {
            case IDLE -> 58;
            case START_RUN -> 73;
            case RUN -> 86;
        };
    }

    private int getCurrentFrameHeight() {
        return switch (currentState) {
            case IDLE -> 58;
            case START_RUN -> 58;
            case RUN -> 50;
        };
    }

    public void draw(Graphics g) {
        BufferedImage[] framesToDraw = switch (currentState) {
            case IDLE -> idleFrames;
            case START_RUN -> startRunFrames;
            case RUN -> runFrames;
        };

        BufferedImage frame = framesToDraw[currentFrame];

        // 🔹 Зеркалируем все анимации, включая start_run
        if (!movingRight) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-frame.getWidth(), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            frame = op.filter(frame, null);
        }

        g.drawImage(frame, x, y, null);
    }

    // управление
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case 87 -> upPressed = true; // W
            case 83 -> downPressed = true; // S
            case 65 -> leftPressed = true; // A
            case 68 -> rightPressed = true; // D
        }
    }

    public void keyReleased(int keyCode) {
        switch (keyCode) {
            case 87 -> upPressed = false;
            case 83 -> downPressed = false;
            case 65 -> leftPressed = false;
            case 68 -> rightPressed = false;
        }
    }

    // 🔹 Устанавливаем границы движения (из GamePanel)
    public void setGroundBounds(int top, int bottom, int left, int right) {
        this.groundTop = top;
        this.groundLevel = bottom;
        this.groundLeft = left;
        this.groundRight = right;

        // Устанавливаем начальную позицию в пределах ground
        this.y = bottom - getCurrentFrameHeight() - 64;
        this.x = left + 100; // Отступ от левого края
    }

    public int getX() { return x; }
    public int getY() { return y; }
}