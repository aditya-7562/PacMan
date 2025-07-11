import javax.swing.SwingUtilities;

/**
 * Main class for the Pac-Man game.
 * Initializes the game window.
 */
public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and display the game window
                new GameFrame();
            }
        });
    }
}