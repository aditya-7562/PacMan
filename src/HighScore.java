import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for managing high scores in the game.
 */
public class HighScore {
    private static final String HIGH_SCORE_FILE = System.getProperty("user.dir") + File.separator + "highscores.txt";
    private static final int MAX_HIGH_SCORES = 5;
    
    private List<Integer> highScores;
    
    /**
     * Creates a new HighScore object and loads existing high scores.
     */
    public HighScore() {
        highScores = new ArrayList<>();
        loadHighScores();
    }
    
    /**
     * Loads high scores from the file.
     */
    private void loadHighScores() {
        File file = new File(HIGH_SCORE_FILE);
        System.out.println("Loading high scores from: " + file.getAbsolutePath());
        
        if (file.exists()) {
            System.out.println("High score file exists");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        int score = Integer.parseInt(line.trim());
                        highScores.add(score);
                        System.out.println("Loaded high score: " + score);
                    } catch (NumberFormatException e) {
                        // Ignore invalid lines
                        System.err.println("Invalid score format: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading high scores: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("High score file does not exist yet. Will be created when a score is added.");
            // Try to create the directory if it doesn't exist
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                System.out.println("Created parent directories: " + created);
            }
        }
        
        // Sort high scores in descending order
        Collections.sort(highScores, Collections.reverseOrder());
        
        // Trim to maximum number of high scores
        while (highScores.size() > MAX_HIGH_SCORES) {
            highScores.remove(highScores.size() - 1);
        }
        
        System.out.println("Loaded " + highScores.size() + " high scores");
    }
    
    /**
     * Saves high scores to the file.
     */
    private void saveHighScores() {
        File file = new File(HIGH_SCORE_FILE);
        System.out.println("Saving high scores to: " + file.getAbsolutePath());
        
        // Ensure the parent directory exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            System.out.println("Created parent directories: " + created);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int score : highScores) {
                writer.write(Integer.toString(score));
                writer.newLine();
                System.out.println("Saved high score: " + score);
            }
            System.out.println("Successfully saved " + highScores.size() + " high scores");
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Adds a new score to the high scores list if it qualifies.
     * 
     * @param score The score to add
     * @return True if the score was added as a high score, false otherwise
     */
    public boolean addScore(int score) {
        // Check if the score qualifies as a high score
        if (highScores.size() < MAX_HIGH_SCORES || score > highScores.get(highScores.size() - 1)) {
            highScores.add(score);
            Collections.sort(highScores, Collections.reverseOrder());
            
            // Trim to maximum number of high scores
            while (highScores.size() > MAX_HIGH_SCORES) {
                highScores.remove(highScores.size() - 1);
            }
            
            // Save the updated high scores
            saveHighScores();
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets the current high scores.
     * 
     * @return The list of high scores in descending order
     */
    public List<Integer> getHighScores() {
        return new ArrayList<>(highScores);
    }
    
    /**
     * Gets the highest score.
     * 
     * @return The highest score, or 0 if there are no high scores
     */
    public int getHighestScore() {
        if (highScores.isEmpty()) {
            return 0;
        }
        return highScores.get(0);
    }
}