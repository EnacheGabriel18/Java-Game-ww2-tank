import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BossBullet {
    private int x, y;
    private int width, height;
    private boolean visible;
    private Image image; // Image for the bullet

    public BossBullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50; // You can adjust the size if necessary
        this.height = 50; // You can adjust the size if necessary
        this.visible = true;

        try {
            // Load the bullet image (make sure the file path is correct)
            image = ImageIO.read(new File("resources/bossbullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        y += 5; // Moves downward
        if (y > 600) {
            visible = false; // Remove if it goes off-screen
        }
    }

    public void draw(Graphics g) {
        if (visible && image != null) {
            // Draw the image at the bullet's position
            g.drawImage(image, x, y, width, height, null);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
