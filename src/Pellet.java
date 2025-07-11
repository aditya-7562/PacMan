import java.awt.Color;
import java.awt.Graphics;

/**
 * Class representing a pellet in the game.
 */
public class Pellet {
    private Position position;
    private boolean isPowerPellet;
    private boolean isEaten;
    private int size;
    private int points;
    
    /**
     * Creates a new Pellet at the specified position.
     * 
     * @param position The position of the pellet
     * @param isPowerPellet Whether this is a power pellet
     * @param size The size of the pellet in pixels
     */
    public Pellet(Position position, boolean isPowerPellet, int size) {
        this.position = position;
        this.isPowerPellet = isPowerPellet;
        this.isEaten = false;
        this.size = size;
        this.points = isPowerPellet ? 50 : 10; // Power pellets are worth more points
    }
    
    /**
     * Gets the position of this pellet.
     * 
     * @return The pellet's position
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Checks if this is a power pellet.
     * 
     * @return True if this is a power pellet, false otherwise
     */
    public boolean isPowerPellet() {
        return isPowerPellet;
    }
    
    /**
     * Checks if this pellet has been eaten.
     * 
     * @return True if this pellet has been eaten, false otherwise
     */
    public boolean isEaten() {
        return isEaten;
    }
    
    /**
     * Gets the point value of this pellet.
     * 
     * @return The point value
     */
    public int getPoints() {
        return points;
    }
    
    /**
     * Marks this pellet as eaten.
     */
    public void eat() {
        isEaten = true;
    }
    
    /**
     * Draws this pellet on the screen.
     * 
     * @param g The Graphics object to draw with
     * @param tileSize The size of a tile in pixels
     */
    public void draw(Graphics g, int tileSize) {
        if (!isEaten) {
            g.setColor(Color.WHITE);
            if (isPowerPellet) {
                // Draw a larger pellet for power pellets
                g.fillOval(
                    position.getX() * tileSize + tileSize/4, 
                    position.getY() * tileSize + tileSize/4, 
                    tileSize/2, 
                    tileSize/2
                );
            } else {
                // Draw a smaller pellet for regular pellets
                g.fillOval(
                    position.getX() * tileSize + tileSize/3, 
                    position.getY() * tileSize + tileSize/3, 
                    tileSize/3, 
                    tileSize/3
                );
            }
        }
    }
}