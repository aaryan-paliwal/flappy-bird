package com.flappybird;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensure UI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame();
            }
        });
    }
}
