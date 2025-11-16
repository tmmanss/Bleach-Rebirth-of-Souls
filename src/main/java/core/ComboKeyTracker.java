package core;

public class ComboKeyTracker {
    private int pressCount;
    private long lastPressTime;
    private final int comboTimeout = 250;

    public int registerPress() {
        long now = System.currentTimeMillis();
        
        if (now - lastPressTime > comboTimeout) {
            pressCount = 1;
        } else {
            pressCount++;
        }
        
        lastPressTime = now;
        return pressCount;
    }
}


