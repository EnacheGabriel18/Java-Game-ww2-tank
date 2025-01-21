import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Player {
    private int x, y;
    private Image playerImage;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;


        try {
            playerImage = ImageIO.read(new File("resources/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {}

    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, 100, 100, null); 
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bullet shoot() {
        int bulletX = x + 43;
        int bulletY = y;
        return new Bullet(bulletX, bulletY);
    }
    
}