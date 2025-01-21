import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bullet {
    private int x, y;
    private int speed = 10;
    private boolean visible;
    private Image bulletImage;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        visible = true;

        try {
            bulletImage = ImageIO.read(new File("resources/bullet.png")); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        y -= speed; 
    }

    public void draw(Graphics g) {
        if (visible) {
            g.drawImage(bulletImage, x, y, 20, 40, null);
        }
    }

    public boolean isVisible() {
        return visible && y > 0;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 5, 10);
    }
}
