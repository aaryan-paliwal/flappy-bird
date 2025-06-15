package com.flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Pipe {

    // Static constants for pipe behavior
    public static final int PIPE_WIDTH = 60;
    public static final int PIPE_GAP = 180; // Adjusted for better playability
    public static final int PIPE_SPEED = 3;

    // Properties
    private int x; // x-coordinate of the left edge of the pipe
    private int topPipeHeight;
    private int bottomPipeY; // y-coordinate where the bottom pipe's top edge starts
    private boolean passed;

    private int panelHeight; // To know the drawing boundaries
    private static Random random = new Random(); // Static for efficiency

    public Pipe(int startX, int panelHeight) {
        this.x = startX;
        this.panelHeight = panelHeight;
        this.passed = false;

        // Randomly generate topPipeHeight
        // Min height for top pipe part and bottom pipe part is, say, 50px.
        int minPipePartHeight = 50;
        int availableSpaceForRandom = panelHeight - PIPE_GAP - (minPipePartHeight * 2);
        if (availableSpaceForRandom <= 0) {
            // Fallback if panelHeight is too small, though GamePanel size should prevent this
            this.topPipeHeight = minPipePartHeight;
        } else {
            this.topPipeHeight = random.nextInt(availableSpaceForRandom) + minPipePartHeight;
        }

        this.bottomPipeY = this.topPipeHeight + PIPE_GAP;
    }

    public void update() {
        this.x -= PIPE_SPEED;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        // Draw top pipe
        g.fillRect(this.x, 0, PIPE_WIDTH, this.topPipeHeight);
        // Draw bottom pipe
        g.fillRect(this.x, this.bottomPipeY, PIPE_WIDTH, this.panelHeight - this.bottomPipeY);
    }

    // Getter methods
    public int getX() {
        return this.x;
    }

    public int getWidth() {
        return PIPE_WIDTH;
    }

    public boolean isPassed() {
        return this.passed;
    }

    // Setter method
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    // Bounding box methods
    public Rectangle getTopPipeBounds() {
        return new Rectangle(this.x, 0, PIPE_WIDTH, this.topPipeHeight);
    }

    public Rectangle getBottomPipeBounds() {
        return new Rectangle(this.x, this.bottomPipeY, PIPE_WIDTH, this.panelHeight - this.bottomPipeY);
    }
}
