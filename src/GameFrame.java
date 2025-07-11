import javax.swing.JFrame;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main game window that contains the title screen and game panel.
 */
public class GameFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TitleScreen titleScreen;
    private GamePanel gamePanel;
    
    // Card names for the card layout
    private static final String TITLE_SCREEN = "TitleScreen";
    private static final String GAME_PANEL = "GamePanel";
    
    /**
     * Creates a new GameFrame.
     */
    public GameFrame() {
        // Set up the frame
        setTitle("Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Create the card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create the title screen and game panel
        titleScreen = new TitleScreen(this);
        gamePanel = new GamePanel();
        
        // Add the panels to the card layout
        mainPanel.add(titleScreen, TITLE_SCREEN);
        mainPanel.add(gamePanel, GAME_PANEL);
        
        // Add the main panel to the frame
        add(mainPanel);
        
        // Show the title screen initially
        cardLayout.show(mainPanel, TITLE_SCREEN);
        
        // Pack the frame to fit the preferred size of its components
        pack();
        
        // Center the frame on the screen
        setLocationRelativeTo(null);
        
        // Make the frame visible
        setVisible(true);
    }
    
    /**
     * Starts the game by switching to the game panel.
     * 
     * @param difficulty The selected difficulty level
     */
    public void startGame(int difficulty) {
        gamePanel.setDifficulty(difficulty);
        gamePanel.restartGame(); // Ensure game starts with fresh state
        cardLayout.show(mainPanel, GAME_PANEL);
        gamePanel.requestFocusInWindow(); // Give focus to the game panel for keyboard input
    }
    
    /**
     * Returns to the title screen.
     */
    public void returnToTitleScreen() {
        cardLayout.show(mainPanel, TITLE_SCREEN);
        titleScreen.requestFocusInWindow();
    }
}