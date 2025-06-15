package com.flappybird;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setSize(360, 640); // Removed, pack() will handle sizing

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        pack(); // Size the frame to fit the preferred size of its subcomponents

        setResizable(false);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        // Ensure the panel can receive key events
        gamePanel.requestFocusInWindow();
    }

    // Main method removed, will be in a separate Main class
    // public static void main(String[] args) {
    //     new GameFrame();
    // }
}
