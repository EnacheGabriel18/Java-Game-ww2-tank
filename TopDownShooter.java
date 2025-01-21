import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class TopDownShooter extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private boolean gameOver;
    private int score;
    private Image backgroundImage;
    private Boss boss;
    private ArrayList<BossBullet> bossBullets;
    private boolean bossSpawned;
    private boolean bossDefeated;
    private int lastTimeDefeated;

    // Add music callbacks
    private Runnable startMusicCallback;
    private Runnable stopMusicCallback;

    // Modify constructor to accept music callbacks
    public TopDownShooter(Runnable startMusic, Runnable stopMusic) {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        this.startMusicCallback = startMusic;
        this.stopMusicCallback = stopMusic;

        initializeGame();

        boss = null; 
        bossBullets = new ArrayList<>();
        bossSpawned = false;

        try {
            backgroundImage = ImageIO.read(new File("resources/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(16, this);
        timer.start();
    }

    private void initializeGame() {
        player = new Player(400, 500);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        gameOver = false;
        score = 0;

        // Start music when the game starts
        startMusicCallback.run();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", 300, 250);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Score: " + score, 350, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.setColor(Color.WHITE);
            g.drawString("Press R to Retry", 300, 350);
            g.drawString("Press M to Return to Menu", 300, 380);
            return;
        }

        player.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);

        if (bossSpawned && boss != null) {
            boss.draw(g);

            for (BossBullet bossBullet : bossBullets) {
                bossBullet.draw(g);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        player.update();
        spawnEnemies();

        if (!bossSpawned && lastTimeDefeated >= 2) {
            boss = new Boss(325, 50);
            bossSpawned = true;
            lastTimeDefeated = 0;
        }

        if (bossDefeated && !bossSpawned) {
            lastTimeDefeated = 0;
            bossDefeated = false;
        }

        if (bossSpawned && boss != null) {
            boss.update();
            if (new Random().nextInt(100) < 2) {
                bossBullets.add(boss.shoot());
            }
        }

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (!bullet.isVisible()) {
                bulletIterator.remove();
            }
        }

        Iterator<BossBullet> bossBulletIterator = bossBullets.iterator();
        while (bossBulletIterator.hasNext()) {
            BossBullet bossBullet = bossBulletIterator.next();
            bossBullet.update();

            if (bossBullet.getBounds().intersects(player.getBounds())) {
                gameOver = true;
            }

            if (!bossBullet.isVisible()) {
                bossBulletIterator.remove();
            }
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update();

            if (enemy.collidesWith(player.getBounds())) {
                gameOver = true;
            }

            boolean hit = false;
            for (Bullet bullet : bullets) {
                if (enemy.collidesWith(bullet.getBounds())) {
                    hit = true;
                    bullet.setVisible(false);
                    break;
                }
            }

            if (hit) {
                enemyIterator.remove();
                score += 10;
                lastTimeDefeated++;
            }
        }

        if (bossSpawned && boss != null && boss.isVisible()) {
            boolean hit = false;
            for (Bullet bullet : bullets) {
                if (boss.collidesWith(bullet.getBounds())) {
                    boss.takeDamage();
                    bullet.setVisible(false);
                    hit = true;
                    break;
                }
            }

            if (hit && !boss.isVisible()) {
                score += 20;
                boss = null;
                bossSpawned = false;
                bossDefeated = true;
            }
        }

        repaint();
    }

    private void spawnEnemies() {
        if (new Random().nextInt(100) < 2) {
            int x = new Random().nextInt(800);
            enemies.add(new Enemy(x, 0));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); 

        if (gameOver) {
            if (key == KeyEvent.VK_R) {
                initializeGame();
                timer.start();
                startMusicCallback.run();  // Restart music on game restart
            } else if (key == KeyEvent.VK_M) {
                stopMusicCallback.run();
                JFrame menuFrame = new JFrame("Game Menu");
                @SuppressWarnings("unused")
                GameMenu menu = new GameMenu();
                menuFrame.dispose(); // Close the game frame
            }
        } else {
            if (key == KeyEvent.VK_W) {
                player.setY(player.getY() - 5);
            }
            if (key == KeyEvent.VK_S) {
                player.setY(player.getY() + 5);
            }
            if (key == KeyEvent.VK_A) {
                player.setX(player.getX() - 5);
            }
            if (key == KeyEvent.VK_D) {
                player.setX(player.getX() + 5);
            }
            if (key == KeyEvent.VK_SPACE) {
                bullets.add(player.shoot());
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public void onGameOver() {
        stopMusicCallback.run(); // Stop music on game over
    }

    public static void main(String[] args) {
        // Example of using start and stop music callbacks
        Runnable startMusic = () -> System.out.println("Music started");
        Runnable stopMusic = () -> System.out.println("Music stopped");

        JFrame frame = new JFrame("Top Down Shooter");
        TopDownShooter game = new TopDownShooter(startMusic, stopMusic);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false); 
        frame.add(game);
        frame.pack(); 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
        game.requestFocusInWindow(); 
    }
}
