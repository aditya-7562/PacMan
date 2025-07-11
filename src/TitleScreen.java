import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Title screen for the Pac-Man game.
 */
public class TitleScreen extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private static final int WIDTH = 475; // 19 * 25
    private static final int HEIGHT = 575; // 21 * 25 + 50
    
    // Difficulty levels
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;
    
    private Rectangle startButton;
    private Rectangle easyButton;
    private Rectangle mediumButton;
    private Rectangle hardButton;
    private GameFrame parent;
    private int selectedDifficulty;
    
    /**
     * Creates a new title screen.
     * 
     * @param parent The parent GameFrame
     */
    public TitleScreen(GameFrame parent) {
        this.parent = parent;
        
        // Set up the panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Set default difficulty
        selectedDifficulty = DIFFICULTY_MEDIUM;
        
        // Create the buttons
        startButton = new Rectangle(WIDTH / 2 - 60, HEIGHT / 2 + 100, 120, 40);
        easyButton = new Rectangle(WIDTH / 2 - 140, HEIGHT / 2, 80, 30);
        mediumButton = new Rectangle(WIDTH / 2 - 40, HEIGHT / 2, 80, 30);
        hardButton = new Rectangle(WIDTH / 2 + 60, HEIGHT / 2, 80, 30);
        
        // Add mouse listener for button clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (startButton.contains(e.getPoint())) {
                    startGame();
                } else if (easyButton.contains(e.getPoint())) {
                    selectedDifficulty = DIFFICULTY_EASY;
                    repaint();
                } else if (mediumButton.contains(e.getPoint())) {
                    selectedDifficulty = DIFFICULTY_MEDIUM;
                    repaint();
                } else if (hardButton.contains(e.getPoint())) {
                    selectedDifficulty = DIFFICULTY_HARD;
                    repaint();
                }
            }
        });
    }
    
    /**
     * Starts the game by switching to the game panel.
     */
    private void startGame() {
        parent.startGame(selectedDifficulty);
    }
    
    /**
     * Gets the selected difficulty level.
     * 
     * @return The selected difficulty level
     */
    public int getSelectedDifficulty() {
        return selectedDifficulty;
    }
    
    /**
     * Draws the title screen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw title
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("PAC-MAN", WIDTH / 2 - 120, HEIGHT / 3);
        
        // Draw instructions
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Use arrow keys to move", WIDTH / 2 - 100, HEIGHT / 2 - 60);
        g.drawString("Eat all pellets to win", WIDTH / 2 - 100, HEIGHT / 2 - 40);
        g.drawString("Press P to pause", WIDTH / 2 - 100, HEIGHT / 2 - 20);
        
        // Draw difficulty label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Select Difficulty:", WIDTH / 2 - 80, HEIGHT / 2 - 10);
        
        // Draw difficulty buttons
        // Easy button
        if (selectedDifficulty == DIFFICULTY_EASY) {
            g.setColor(Color.GREEN.darker());
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(easyButton.x, easyButton.y, easyButton.width, easyButton.height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("EASY", easyButton.x + 20, easyButton.y + 20);
        
        // Medium button
        if (selectedDifficulty == DIFFICULTY_MEDIUM) {
            g.setColor(Color.YELLOW.darker());
        } else {
            g.setColor(Color.YELLOW);
        }
        g.fillRect(mediumButton.x, mediumButton.y, mediumButton.width, mediumButton.height);
        g.setColor(Color.BLACK);
        g.drawString("MEDIUM", mediumButton.x + 10, mediumButton.y + 20);
        
        // Hard button
        if (selectedDifficulty == DIFFICULTY_HARD) {
            g.setColor(Color.RED.darker());
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(hardButton.x, hardButton.y, hardButton.width, hardButton.height);
        g.setColor(Color.BLACK);
        g.drawString("HARD", hardButton.x + 20, hardButton.y + 20);
        
        // Draw difficulty description
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        switch (selectedDifficulty) {
            case DIFFICULTY_EASY:
                g.drawString("Slower ghosts, longer power-up time", WIDTH / 2 - 140, HEIGHT / 2 + 50);
                break;
            case DIFFICULTY_MEDIUM:
                g.drawString("Standard ghost speed and power-up time", WIDTH / 2 - 140, HEIGHT / 2 + 50);
                break;
            case DIFFICULTY_HARD:
                g.drawString("Faster ghosts, shorter power-up time", WIDTH / 2 - 140, HEIGHT / 2 + 50);
                break;
        }
        
        // Draw start button
        g.setColor(Color.BLUE);
        g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("START", startButton.x + 25, startButton.y + 25);
        
        // Draw credits
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Enhanced with restart, high score, and difficulty features", WIDTH / 2 - 170, HEIGHT - 30);
    }
}