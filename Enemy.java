import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Enemy {
    private int x, y;
    private int speed = 2;
    private boolean visible;
    private Image enemyImage;
    
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 120;
    private static final int HITBOX_WIDTH = 50; 
    private static final int HITBOX_HEIGHT = 60;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;

        try {
            enemyImage = ImageIO.read(new File("resources/enemy.png")); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (visible) {
            y += speed;
        }
    }

    public void draw(Graphics g) {
        if (visible) {

            int imageX = x - (IMAGE_WIDTH / 2);
            int imageY = y - (IMAGE_HEIGHT / 2);

            g.drawImage(enemyImage, imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT, null); 
            g.setColor(Color.RED);
            g.drawRect(x - (HITBOX_WIDTH / 2), y - (HITBOX_HEIGHT / 2), HITBOX_WIDTH, HITBOX_HEIGHT);
        }
    }

    public boolean collidesWith(Rectangle other) {
        return getBounds().intersects(other);
    }

    public Rectangle getBounds() {

        return new Rectangle(x - (HITBOX_WIDTH / 2), y - (HITBOX_HEIGHT / 2), HITBOX_WIDTH, HITBOX_HEIGHT);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
