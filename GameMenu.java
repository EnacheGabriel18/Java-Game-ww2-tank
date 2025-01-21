import javax.swing.*;
import java.awt.*;//add a data base
import javax.sound.sampled.*;
import java.io.File;

public class GameMenu {
    private JFrame frame;
    private Clip backgroundMusic;
    private boolean isMusicPlaying = false;

    public GameMenu() {
        // Initialize the main frame
        frame = new JFrame("Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        frame.setUndecorated(true); // Remove window borders (optional)
        frame.setLayout(new BorderLayout());

        // Load background music
        loadMusic("resources/background_music.wav");

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("WW2 TANK GAME");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48)); // Larger font for title
        titleLabel.setForeground(new Color(210, 180, 140)); // Light brown color for the text
        titlePanel.setBackground(new Color(34, 139, 34)); // Dark green background for title
        titlePanel.add(titleLabel);

        // Main Menu Panel
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new GridBagLayout()); // Use GridBagLayout for better centering control
        mainMenu.setBackground(new Color(210, 180, 140)); // Light brown background

        // Set up GridBag constraints for centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Add space between buttons

        JButton startButton = new JButton("Start");
        JButton optionsButton = new JButton("Options");
        JButton exitButton = new JButton("Exit");

        styleButton(startButton);
        styleButton(optionsButton);
        styleButton(exitButton);

        // Add buttons to the main menu
        mainMenu.add(startButton, gbc);

        gbc.gridy = 1;
        mainMenu.add(optionsButton, gbc);

        gbc.gridy = 2;
        mainMenu.add(exitButton, gbc);

        // Add the title panel and main menu to the frame
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(mainMenu, BorderLayout.CENTER);

        // Options Panel
        JPanel optionsMenu = new JPanel();
        optionsMenu.setLayout(new GridBagLayout());
        optionsMenu.setBackground(new Color(210, 180, 140)); // Light brown background

        JCheckBox musicCheckBox = new JCheckBox("Background Music", true);
        styleCheckBox(musicCheckBox);

        JButton backButton = new JButton("Back");
        styleButton(backButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        optionsMenu.add(musicCheckBox, gbc);

        gbc.gridy = 1;
        optionsMenu.add(backButton, gbc);

        // Switch to Options Menu
        optionsButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(optionsMenu);
            frame.revalidate();
            frame.repaint();
        });

        // Back to Main Menu
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(titlePanel, BorderLayout.NORTH); // Add title back
            frame.add(mainMenu, BorderLayout.CENTER); // Add main menu back
            frame.revalidate();
            frame.repaint();
        });

        // Toggle Background Music
        musicCheckBox.addActionListener(e -> {
            if (musicCheckBox.isSelected()) {
                startMusic();
            } else {
                stopMusic();
            }
        });

        // Start Button Action
        startButton.addActionListener(e -> {
            frame.dispose(); // Close the menu frame
            SwingUtilities.invokeLater(() -> {
                JFrame gameFrame = new JFrame("Top Down Shooter");
                TopDownShooter game = new TopDownShooter(this::startMusic, this::stopMusic);

                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setResizable(false);
                gameFrame.add(game);
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setVisible(true);
                game.requestFocusInWindow();
            });
        });

        // Exit Button Action
        exitButton.addActionListener(e -> {
            stopMusic(); // Stop music before exiting
            System.exit(0); // Exit the application
        });

        // Show the frame
        frame.pack();  // Make sure the frame sizes correctly after adding components
        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 100, 0)); // Darker green background
        button.setForeground(new Color(210, 180, 140)); // Light brown text color
        button.setFont(new Font("SansSerif", Font.BOLD, 32)); // Larger font for buttons
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 70)); // Larger button size
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setBackground(new Color(210, 180, 140)); // Light brown background
        checkBox.setForeground(new Color(34, 139, 34)); // Dark green text
        checkBox.setFont(new Font("SansSerif", Font.BOLD, 24)); // Larger font for the checkbox
    }

    private void loadMusic(String musicFilePath) {
        try {
            File musicFile = new File(musicFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMusic() {
        if (backgroundMusic != null && !isMusicPlaying) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
            backgroundMusic.start();
            isMusicPlaying = true;
        }
    }

    private void stopMusic() {
        if (backgroundMusic != null && isMusicPlaying) {
            backgroundMusic.stop();
            isMusicPlaying = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMenu::new);
    }
}
