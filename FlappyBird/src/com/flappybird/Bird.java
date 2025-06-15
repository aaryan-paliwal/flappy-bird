package com.flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bird {

    // Position (center of the bird)
    public int x, y;
    // Dimensions
    public int width, height;

    // Vertical movement
    public double velocityY;
    private static final double GRAVITY = 0.6;
    private static final double JUMP_STRENGTH = -10.0; // Negative as Y is 0 at top

    public Bird(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 34;  // Standard Flappy Bird sprite width (can be adjusted)
        this.height = 24; // Standard Flappy Bird sprite height (can be adjusted)
        this.velocityY = 0;
    }

    public void jump() {
        this.velocityY = JUMP_STRENGTH;
    }

    public void update() {
        // Apply gravity
        this.velocityY += GRAVITY;

        // Update position
        this.y += this.velocityY;

        // Optional: Prevent bird from going off the top of the screen
        // this.y = Math.max(this.y, 0);
        // A more precise top bound might consider height / 2 if y is center
        this.y = Math.max(this.y, this.height / 2);


        // Optional: Terminal velocity (example)
        // if (this.velocityY > 15) {
        //     this.velocityY = 15;
        // }
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        // Draw the bird centered at (x, y)
        g.fillRect(this.x - this.width / 2, this.y - this.height / 2, this.width, this.height);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x - this.width / 2, this.y - this.height / 2, this.width, this.height);
    }
}
