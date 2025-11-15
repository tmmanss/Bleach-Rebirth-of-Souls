package core;

import factory.HeroFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HeroSelectionScreen extends JDialog {

    private String player1Choice;
    private String player2Choice;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> heroList = new JList<>(listModel);

    private int currentPlayer = 1;

    public HeroSelectionScreen(JFrame parent) {
        super(parent, "Select Heroes", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        Set<String> heroNames = HeroFactory.getAllHeroNames();
        heroNames.forEach(listModel::addElement);

        heroList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        heroList.setFont(new Font("Arial", Font.PLAIN, 20));

        JScrollPane scrollPane = new JScrollPane(heroList);
        add(scrollPane, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select for Player 1");
        selectButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(selectButton, BorderLayout.SOUTH);

        selectButton.addActionListener(e -> {

            String selected = heroList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a hero first!");
                return;
            }

            if (currentPlayer == 1) {
                player1Choice = selected;
                currentPlayer = 2;
                selectButton.setText("Select for Player 2");
                JOptionPane.showMessageDialog(this,
                        "Player 1 selected: " + selected + "\nPlayer 2 choose now!");
            } else {
                player2Choice = selected;
                JOptionPane.showMessageDialog(this,
                        "Player 2 selected: " + selected + "\nStarting battle...");
                dispose();
            }
        });
    }

    public String getPlayer1Choice() {
        return player1Choice;
    }

    public String getPlayer2Choice() {
        return player2Choice;
    }
}
