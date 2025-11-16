package core;

public class ComboKeyTracker {
    private int pressCount;
    private long lastPressTime;
    private final int comboTimeout = 250;

    public int registerPress() {
        long now = System.currentTimeMillis();
        
        if (now - lastPressTime > comboTimeout) {
            // Timeout exceeded, reset combo count
            pressCount = 1;
        } else {
            // Within timeout, increment combo count
            pressCount++;
        }
        
        lastPressTime = now;
        return pressCount;
    }
}

