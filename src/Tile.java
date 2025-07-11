import java.awt.Color;
import java.awt.Graphics;

/**
 * Class representing a tile in the game maze.
 */
public class Tile {
    // Tile types
    public static final int WALL = 1;
    public static final int PATH = 0;
    public static final int PELLET = 2;
    public static final int POWER_PELLET = 3;
    
    private int type;
    private int x;
    private int y;
    private int size;
    private boolean hasPellet;
    private boolean hasPowerPellet;
    
    /**
     * Creates a new Tile with the specified type and position.
     * 
     * @param type The type of tile (WALL, PATH, PELLET, or POWER_PELLET)
     * @param x The x-coordinate in the grid
     * @param y The y-coordinate in the grid
     * @param size The size of the tile in pixels
     */
    public Tile(int type, int x, int y, int size) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.size = size;
        this.hasPellet = type == PELLET;
        this.hasPowerPellet = type == POWER_PELLET;
    }
    
    /**
     * Gets the type of this tile.
     * 
     * @return The tile type
     */
    public int getType() {
        return type;
    }
    
    /**
     * Gets the x-coordinate of this tile.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the y-coordinate of this tile.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Gets the size of this tile.
     * 
     * @return The tile size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Checks if this tile is a wall.
     * 
     * @return True if this tile is a wall, false otherwise
     */
    public boolean isWall() {
        return type == WALL;
    }
    
    /**
     * Checks if this tile has a pellet.
     * 
     * @return True if this tile has a pellet, false otherwise
     */
    public boolean hasPellet() {
        return hasPellet;
    }
    
    /**
     * Checks if this tile has a power pellet.
     * 
     * @return True if this tile has a power pellet, false otherwise
     */
    public boolean hasPowerPellet() {
        return hasPowerPellet;
    }
    
    /**
     * Removes the pellet from this tile.
     */
    public void removePellet() {
        hasPellet = false;
    }
    
    /**
     * Removes the power pellet from this tile.
     */
    public void removePowerPellet() {
        hasPowerPellet = false;
    }
    
    /**
     * Draws this tile on the screen.
     * 
     * @param g The Graphics object to draw with
     */
    public void draw(Graphics g) {
        // Draw the tile background
        if (isWall()) {
            g.setColor(Color.BLUE);
            g.fillRect(x * size, y * size, size, size);
        }
        
        // Draw pellet if present
        if (hasPellet) {
            g.setColor(Color.WHITE);
            g.fillOval(x * size + size/3, y * size + size/3, size/3, size/3);
        }
        
        // Draw power pellet if present
        if (hasPowerPellet) {
            g.setColor(Color.WHITE);
            g.fillOval(x * size + size/4, y * size + size/4, size/2, size/2);
        }
    }
}