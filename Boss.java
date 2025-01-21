import java.awt.*;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class Boss {
    private int x, y;
    private int width, height;
    private int health;
    private boolean visible;
    private int direction; // -1 for left, 1 for right
    private Image bossImage;
    private Random random;

    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 150; // Bigger hitbox
        this.height = 100;
        this.health = 5; // 5 hits to kill
        this.visible = true;
        this.direction = 1; // Starts moving to the right
        this.random = new Random();

        try {
            bossImage = ImageIO.read(new File("resources/boss.png")); // Load boss image
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Move side to side
        x += direction * 3; // Horizontal speed
        if (x <= 0 || x + width >= 800|| random.nextInt(100) < 2)  {
            direction *= -1; // Reverse direction
        }
        if (x < 0) {
            x = 0;  // Ensure boss doesn't go off the left side
        }
        if (x + width > 800) {
            x = 800 - width;  // Ensure boss doesn't go off the right side
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            g.drawImage(bossImage, x, y, width, height, null);
            // Draw health bar
            g.setColor(Color.RED);
            g.fillRect(x, y - 10, width, 5);
            g.setColor(Color.GREEN);
            g.fillRect(x, y - 10, (int) (width * (health / 5.0)), 5); // Proportionate health
        }
    }

    public boolean collidesWith(Rectangle other) {
        return visible && new Rectangle(x, y, width, height).intersects(other);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage() {
        health--;
        if (health <= 0) {
            visible = false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BossBullet shoot() {
        if (visible) {  // Only shoot if the boss is still alive
            return new BossBullet(x + width / 2, y + height); // Spawn bullet from the boss's position
        }
        return null;  // Return null if the boss is dead
    }    
}
