import java.awt.Color;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The main game panel that handles the game loop, rendering, and input.
 */
public class GamePanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    // Game constants
    private static final int TILE_SIZE = 25;
    private static final int MAZE_WIDTH = 19;
    private static final int MAZE_HEIGHT = 21;
    private static final int PANEL_WIDTH = MAZE_WIDTH * TILE_SIZE;
    private static final int PANEL_HEIGHT = MAZE_HEIGHT * TILE_SIZE + 50; // Extra space for score
    private static final int DELAY = 150; // Milliseconds between updates (slower for easier gameplay)
    
    // Difficulty settings
    private static final int GHOST_SPEED_EASY = 1;
    private static final int GHOST_SPEED_MEDIUM = 2;
    private static final int GHOST_SPEED_HARD = 3;
    private static final int POWER_DURATION_EASY = 450; // 7.5 seconds
    private static final int POWER_DURATION_MEDIUM = 300; // 5 seconds
    private static final int POWER_DURATION_HARD = 150; // 2.5 seconds
    
    // Game objects
    private Maze maze;
    private PacMan pacman;
    private List<Ghost> ghosts;
    
    // Game state
    private boolean running;
    private boolean gameOver;
    private boolean gameWon;
    private boolean paused;
    private int score;
    private int difficulty; // 0=easy, 1=medium, 2=hard
    
    // High score system
    private HighScore highScore;
    
    // UI elements
    private Rectangle restartButton;
    private Rectangle titleScreenButton;
    private Rectangle resumeButton;
    
    // Timer for the game loop
    private Timer timer;
    
    /**
     * Creates a new GamePanel.
     */
    public GamePanel() {
        // Set up the panel
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        addMouseListener(new MyMouseAdapter());
        
        // Initialize high score system
        highScore = new HighScore();
        
        // Create UI elements
        restartButton = new Rectangle(PANEL_WIDTH / 2 - 130, PANEL_HEIGHT / 2 + 50, 120, 40);
        titleScreenButton = new Rectangle(PANEL_WIDTH / 2 + 10, PANEL_HEIGHT / 2 + 50, 120, 40);
        resumeButton = new Rectangle(PANEL_WIDTH / 2 - 60, PANEL_HEIGHT / 2, 120, 40);
        
        // Set default difficulty
        difficulty = TitleScreen.DIFFICULTY_MEDIUM;
        
        // Initialize the game
        initGame();
    }
    
    /**
     * Initializes the game objects and state.
     */
    private void initGame() {
        // Create the maze
        maze = new Maze(MAZE_WIDTH, MAZE_HEIGHT, TILE_SIZE);
        
        // Create Pac-Man at the starting position
        pacman = new PacMan(maze.getPacmanStart(), TILE_SIZE);
        
        // Set power pellet duration based on difficulty
        int powerDuration;
        switch (difficulty) {
            case TitleScreen.DIFFICULTY_EASY:
                powerDuration = POWER_DURATION_EASY;
                break;
            case TitleScreen.DIFFICULTY_HARD:
                powerDuration = POWER_DURATION_HARD;
                break;
            case TitleScreen.DIFFICULTY_MEDIUM:
            default:
                powerDuration = POWER_DURATION_MEDIUM;
                break;
        }
        pacman.setPowerModeDuration(powerDuration);
        
        // Create ghosts at their starting positions
        ghosts = new ArrayList<>();
        List<Position> ghostStarts = maze.getGhostStarts();
        
        // Set ghost speed based on difficulty
        double ghostSpeed;
        switch (difficulty) {
            case TitleScreen.DIFFICULTY_EASY:
                ghostSpeed = 0.15;
                break;
            case TitleScreen.DIFFICULTY_HARD:
                ghostSpeed = 0.25;
                break;
            case TitleScreen.DIFFICULTY_MEDIUM:
            default:
                ghostSpeed = 0.2;
                break;
        }
        
        if (ghostStarts.size() > 0) {
            // Create a chaser ghost (red)
            Ghost chaserGhost = new Ghost(ghostStarts.get(0), Ghost.TYPE_CHASER, Color.RED);
            chaserGhost.setSpeed(ghostSpeed);
            chaserGhost.setFrightenedDuration(powerDuration);
            ghosts.add(chaserGhost);
            
            // Create random ghosts with different colors if there are more starting positions
            if (ghostStarts.size() > 1) {
                Ghost pinkGhost = new Ghost(ghostStarts.get(1), Ghost.TYPE_RANDOM, Color.PINK);
                pinkGhost.setSpeed(ghostSpeed);
                pinkGhost.setFrightenedDuration(powerDuration);
                ghosts.add(pinkGhost);
            }
            if (ghostStarts.size() > 2) {
                Ghost cyanGhost = new Ghost(ghostStarts.get(2), Ghost.TYPE_RANDOM, Color.CYAN);
                cyanGhost.setSpeed(ghostSpeed);
                cyanGhost.setFrightenedDuration(powerDuration);
                ghosts.add(cyanGhost);
            }
            if (ghostStarts.size() > 3) {
                Ghost orangeGhost = new Ghost(ghostStarts.get(3), Ghost.TYPE_RANDOM, Color.ORANGE);
                orangeGhost.setSpeed(ghostSpeed);
                orangeGhost.setFrightenedDuration(powerDuration);
                ghosts.add(orangeGhost);
            }
        }
        
        // Initialize game state
        running = true;
        gameOver = false;
        gameWon = false;
        paused = false;
        score = 0;
        
        // Start the game loop
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    /**
     * Handles the game loop.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !gameOver && !gameWon && !paused) {
            // Update game objects
            updateGame();
            
            // Check for collisions
            checkCollisions();
            
            // Check for game over conditions
            checkGameOver();
        }
        
        // Repaint the panel
        repaint();
    }
    
    /**
     * Updates the game objects.
     */
    private void updateGame() {
        // Update Pac-Man
        pacman.update(maze);
        
        // Check for pellet collection
        int points = maze.eatPellet(pacman.getPosition());
        if (points > 0) {
            score += points;
            
            // Check if a power pellet was eaten
            if (maze.isPowerPellet(pacman.getPosition())) {
                pacman.activatePowerMode();
                for (Ghost ghost : ghosts) {
                    ghost.frighten();
                }
            }
        }
        
        // Update ghosts
        for (Ghost ghost : ghosts) {
            ghost.update(maze, pacman);
        }
    }
    
    /**
     * Checks for collisions between Pac-Man and ghosts.
     */
    private void checkCollisions() {
        for (Ghost ghost : ghosts) {
            if (pacman.isCollidingWith(ghost)) {
                if (pacman.isPowerMode() && !ghost.isEaten()) {
                    // Pac-Man eats the ghost
                    ghost.eat();
                    score += 200;
                } else if (!ghost.isFrightened() && !ghost.isEaten()) {
                    // Ghost catches Pac-Man
                    gameOver = true;
                    running = false;
                }
            }
        }
    }
    
    /**
     * Checks for game over conditions.
     */
    private void checkGameOver() {
        // Check if all pellets have been eaten
        if (maze.allPelletsEaten()) {
            gameWon = true;
            running = false;
            // Add score to high scores
            boolean added = highScore.addScore(score);
            System.out.println("Game won with score: " + score + ", added to high scores: " + added);
        }
    }
    
    /**
     * Draws the game on the screen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the maze
        maze.draw(g);
        
        // Draw Pac-Man
        pacman.draw(g, TILE_SIZE);
        
        // Draw ghosts
        for (Ghost ghost : ghosts) {
            ghost.draw(g, TILE_SIZE);
        }
        
        // Draw the score
        drawScore(g);
        
        // Draw game over message if the game is over
        if (gameOver) {
            drawGameOver(g);
        }
        
        // Draw game won message if the game is won
        if (gameWon) {
            drawGameWon(g);
        }
        
        // Draw pause screen if the game is paused
        if (paused) {
            drawPauseScreen(g);
        }
    }
    
    /**
     * Draws the score and game information on the screen.
     * 
     * @param g The Graphics object to draw with
     */
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, PANEL_HEIGHT - 20);
        g.drawString("High Score: " + highScore.getHighestScore(), PANEL_WIDTH - 150, PANEL_HEIGHT - 20);
        
        // Draw difficulty level
        String difficultyText = "";
        switch (difficulty) {
            case TitleScreen.DIFFICULTY_EASY:
                difficultyText = "Easy";
                break;
            case TitleScreen.DIFFICULTY_HARD:
                difficultyText = "Hard";
                break;
            case TitleScreen.DIFFICULTY_MEDIUM:
            default:
                difficultyText = "Medium";
                break;
        }
        g.drawString("Difficulty: " + difficultyText, PANEL_WIDTH / 2 - 50, PANEL_HEIGHT - 20);
    }
    
    /**
     * Draws the buttons on the screen.
     * 
     * @param g The Graphics object to draw with
     */
    private void drawButtons(Graphics g) {
        // Draw restart button
        g.setColor(Color.BLUE);
        g.fillRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("RESTART", restartButton.x + 20, restartButton.y + 25);
        
        // Draw title screen button
        g.setColor(Color.GREEN);
        g.fillRect(titleScreenButton.x, titleScreenButton.y, titleScreenButton.width, titleScreenButton.height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("MENU", titleScreenButton.x + 35, titleScreenButton.y + 25);
    }
    
    /**
     * Draws the pause screen.
     * 
     * @param g The Graphics object to draw with
     */
    private void drawPauseScreen(Graphics g) {
        // Draw semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        
        // Draw pause message
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("PAUSED", PANEL_WIDTH / 2 - 80, PANEL_HEIGHT / 2 - 50);
        
        // Draw resume button
        g.setColor(Color.ORANGE);
        g.fillRect(resumeButton.x, resumeButton.y, resumeButton.width, resumeButton.height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("RESUME", resumeButton.x + 25, resumeButton.y + 25);
    }
    
    /**
     * Draws the high scores on the screen.
     * 
     * @param g The Graphics object to draw with
     */
    private void drawHighScores(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("HIGH SCORES", PANEL_WIDTH / 2 - 70, PANEL_HEIGHT / 2 + 120);
        
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        List<Integer> scores = highScore.getHighScores();
        for (int i = 0; i < scores.size(); i++) {
            g.drawString((i + 1) + ". " + scores.get(i), PANEL_WIDTH / 2 - 50, PANEL_HEIGHT / 2 + 150 + i * 20);
        }
    }
    
    /**
     * Restarts the game.
     */
    public void restartGame() {
        // Stop the current timer
        if (timer != null) {
            timer.stop();
        }
        
        // Initialize the game again
        initGame();
    }
    
    /**
     * Sets the difficulty level for the game.
     * 
     * @param difficulty The difficulty level (0=easy, 1=medium, 2=hard)
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    
    /**
     * Gets the current difficulty level.
     * 
     * @return The difficulty level
     */
    public int getDifficulty() {
        return difficulty;
    }
    
    /**
     * Returns to the title screen.
     */
    private void returnToTitleScreen() {
        // Stop the current timer
        if (timer != null) {
            timer.stop();
        }
        
        // Get the parent frame and return to the title screen
        if (getParent() != null) {
            // Try to find the GameFrame by traversing up the component hierarchy
            java.awt.Container parent = getParent();
            while (parent != null && !(parent instanceof GameFrame)) {
                parent = parent.getParent();
            }
            
            if (parent instanceof GameFrame) {
                GameFrame frame = (GameFrame) parent;
                frame.returnToTitleScreen();
                System.out.println("Returning to title screen");
            } else {
                System.err.println("Could not find GameFrame parent");
            }
        } else {
            System.err.println("GamePanel has no parent");
        }
    }
    
    /**
     * Draws the game over message on the screen.
     * 
     * @param g The Graphics object to draw with
     */
    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("GAME OVER", PANEL_WIDTH / 2 - 120, PANEL_HEIGHT / 2);
        
        // Draw buttons
        drawButtons(g);
        
        // Draw high scores
        drawHighScores(g);
    }
    
    /**
     * Draws the game won message on the screen.
     * 
     * @param g The Graphics object to draw with
     */
    private void drawGameWon(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("YOU WIN!", PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2);
        
        // Draw buttons
        drawButtons(g);
        
        // Draw high scores
        drawHighScores(g);
    }
    
    /**
     * Key adapter for handling keyboard input.
     */
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            
            // Handle pause key (P)
            if (key == KeyEvent.VK_P) {
                togglePause();
                return;
            }
            
            // If the game is paused, only allow unpausing
            if (paused) {
                return;
            }
            
            // Handle other keys
            switch (key) {
                case KeyEvent.VK_UP:
                    pacman.setNextDirection(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    pacman.setNextDirection(Direction.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    pacman.setNextDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    pacman.setNextDirection(Direction.RIGHT);
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_R:
                    if (gameOver || gameWon) {
                        restartGame();
                    }
                    break;
            }
        }
    }
    
    /**
     * Toggles the pause state of the game.
     */
    private void togglePause() {
        paused = !paused;
        repaint();
    }
    
    /**
     * Mouse adapter for handling mouse input.
     */
    private class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            handleMouseEvent(e);
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            // Also handle mousePressed events for better button responsiveness
            handleMouseEvent(e);
        }
        
        private void handleMouseEvent(MouseEvent e) {
            System.out.println("Mouse event at: " + e.getPoint());
            
            if (paused) {
                // Check if resume button was clicked
                if (resumeButton.contains(e.getPoint())) {
                    System.out.println("Resume button clicked");
                    togglePause();
                }
            } else if (gameOver || gameWon) {
                // Check if restart button was clicked
                if (restartButton.contains(e.getPoint())) {
                    System.out.println("Restart button clicked");
                    restartGame();
                }
                // Check if title screen button was clicked
                else if (titleScreenButton.contains(e.getPoint())) {
                    System.out.println("Title screen button clicked");
                    returnToTitleScreen();
                } else {
                    System.out.println("Click not on any button. Restart button: " + restartButton + ", Title button: " + titleScreenButton);
                }
            }
        }
    }
}