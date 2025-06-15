package com.flappybird;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// Import utility classes
import java.util.ArrayList;
import java.util.List;
// Import game object classes
import com.flappybird.Bird;
import com.flappybird.Pipe;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Screen dimensions
    private static final int SCREEN_WIDTH = 360;
    private static final int SCREEN_HEIGHT = 640;

    // Pipe management
    private static final int PIPE_SPAWN_INTERVAL = 220; // Distance between pipes
    private List<Pipe> pipes;

    private Timer gameLoopTimer;
    private Bird bird;
    private boolean gameOver = false; // Game over state flag
    private int score = 0; // Player's score

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        bird = new Bird(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2);
        pipes = new ArrayList<>();
        // No initial pipes needed if we want a clean start before first jump
        // spawnInitialPipes();
        // Let's start with one pipe further away to give player time
        pipes.add(new Pipe(SCREEN_WIDTH + SCREEN_WIDTH / 2, SCREEN_HEIGHT));


        gameLoopTimer = new Timer(16, this);
        gameLoopTimer.start();
    }

    private void spawnInitialPipes() {
        // This method can be used to set up a specific starting scenario if needed.
        // For now, the constructor adds the first pipe.
        // If we want multiple initial pipes:
        // pipes.add(new Pipe(SCREEN_WIDTH + 50, SCREEN_HEIGHT));
        // pipes.add(new Pipe(SCREEN_WIDTH + 50 + PIPE_SPAWN_INTERVAL, SCREEN_HEIGHT));
    }

    private void spawnPipe() {
        // New pipes start from the right edge of the screen
        pipes.add(new Pipe(SCREEN_WIDTH, SCREEN_HEIGHT));
    }

    private void updatePipes() {
        if (gameOver) return; // Don't update pipes if game is over

        for (Pipe pipe : pipes) {
            pipe.update();

            // Check for scoring
            if (!pipe.isPassed() && bird.x - bird.width / 2 > pipe.getX() + Pipe.PIPE_WIDTH) {
                score++;
                pipe.setPassed(true);
                System.out.println("Score: " + score); // Debugging
            }
        }

        // Remove pipes that are off-screen
        pipes.removeIf(pipe -> pipe.getX() + Pipe.PIPE_WIDTH < 0);

        // Check if it's time to spawn a new pipe
        if (!pipes.isEmpty()) {
            Pipe lastPipe = pipes.get(pipes.size() - 1);
            // Ensure there's enough space from the right edge before spawning next pipe
            if (lastPipe.getX() < SCREEN_WIDTH - PIPE_SPAWN_INTERVAL) {
                spawnPipe();
            }
        } else if (pipes.isEmpty() && !gameOver) { // If all pipes are gone and game not over, spawn one.
            spawnPipe();
        }
    }

    private void checkCollisions() {
        if (gameOver) return; // Already game over, no need to check again

        // Bird-Pipe collision
        for (Pipe pipe : pipes) {
            if (bird.getBounds().intersects(pipe.getTopPipeBounds()) ||
                bird.getBounds().intersects(pipe.getBottomPipeBounds())) {
                gameOver = true;
                gameLoopTimer.stop();
                // System.out.println("Game Over: Bird hit a pipe!"); // Replaced by visual
                return; // Exit after first collision
            }
        }

        // Bird-Boundary collision (bottom)
        // Bird's y is center, so y + height/2 is the bottom edge
        if (bird.y + bird.height / 2 >= SCREEN_HEIGHT) {
            gameOver = true;
            gameLoopTimer.stop();
            // System.out.println("Game Over: Bird hit the ground!"); // Replaced by visual
            return;
        }

        // Bird-Boundary collision (top) - Bird's own update handles not going off screen.
        // If y - height/2 <= 0, it means top edge hit ceiling.
        // Bird.update already has: this.y = Math.max(this.y, this.height / 2);
        // So bird cannot go above y = bird.height / 2.
        // If we wanted this to be game over:
        // if (bird.y - bird.height / 2 <= 0) {
        //    gameOver = true;
        //    gameLoopTimer.stop();
        //    System.out.println("Game Over: Bird hit the ceiling!");
        // }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Background is handled by setBackground(Color.CYAN)

        // Draw pipes
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }

        // Draw the bird
        bird.draw(g);

        // Draw Score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 30);

        // Draw Game Over message if gameOver is true
        if (gameOver) {
            // "Game Over" Text
            String msg = "Game Over";
            Font gameOverFont = new Font("Arial", Font.BOLD, 48);
            FontMetrics metricsGameOver = g.getFontMetrics(gameOverFont);
            int xGameOver = (SCREEN_WIDTH - metricsGameOver.stringWidth(msg)) / 2;
            int yGameOver = (SCREEN_HEIGHT - metricsGameOver.getHeight()) / 2 + metricsGameOver.getAscent() - 50; // Move up a bit

            g.setFont(gameOverFont);
            g.setColor(Color.RED);
            g.drawString(msg, xGameOver, yGameOver);

            // Final Score Text
            String finalScoreMsg = "Your Score: " + score;
            Font scoreFont = new Font("Arial", Font.PLAIN, 28);
            FontMetrics metricsScore = g.getFontMetrics(scoreFont);
            int xScore = (SCREEN_WIDTH - metricsScore.stringWidth(finalScoreMsg)) / 2;
            int yScore = yGameOver + metricsGameOver.getHeight() + 20; // Position below "Game Over"

            g.setFont(scoreFont);
            g.setColor(Color.BLACK); // Or a different color for final score
            g.drawString(finalScoreMsg, xScore, yScore);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            // If game is over, we still need to repaint to show "Game Over" message
            // and potentially other static elements. The timer is stopped, so this
            // actionPerformed might only be called once more if triggered manually,
            // or not at all if timer is the sole source.
            // To ensure repaint on game over shown:
            repaint();
            return;
        }

        // Update game state
        bird.update();    // Bird moves first
        updatePipes();  // Pipes move next

        checkCollisions(); // Check for game ending conditions

        // Repaint the panel
        repaint();
    }

    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this game
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) { // Only jump if game is not over
                bird.jump();
            } else {
                // Restart game on space press after game over
                restartGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used in this game
    }


    private void restartGame() {
        // Reset bird's position and velocity
        bird.x = SCREEN_WIDTH / 4; // Initial X position
        bird.y = SCREEN_HEIGHT / 2; // Initial Y position
        bird.velocityY = 0;

        // Clear pipes and add the initial pipe(s)
        pipes.clear();
        // Re-add initial pipe setup, similar to constructor
        pipes.add(new Pipe(SCREEN_WIDTH + SCREEN_WIDTH / 2, SCREEN_HEIGHT));
        // Or call spawnInitialPipes() if it's designed for this:
        // spawnInitialPipes();

        // Reset game state
        gameOver = false;
        score = 0;

        // Restart game loop timer
        gameLoopTimer.start();

        // Request focus for key events
        requestFocusInWindow();
    }
}
