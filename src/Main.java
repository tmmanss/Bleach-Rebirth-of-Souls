import core.GameFacade;
import core.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GameFacade game= new GameFacade("Ichigo","Zangetsu");
        GamePanel panel= game.createGamePanel();
        JFrame frame = new JFrame("Bleach Fighters");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
