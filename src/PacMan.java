import java.awt.Color;
import java.awt.Graphics;

/**
 * Class representing the player character (Pac-Man).
 */
public class PacMan {
    private Position position;
    private Direction direction;
    private Direction nextDirection;
    private int speed;
    private boolean powerMode;
    private int powerModeTimer;
    private int powerModeDuration = 300; // Default duration in game ticks (about 5 seconds at 60 FPS)
    private int size;
    
    /**
     * Creates a new Pac-Man at the specified position.
     * 
     * @param position The starting position
     * @param size The size of Pac-Man in pixels
     */
    public PacMan(Position position, int size) {
        this.position = position;
        this.direction = Direction.NONE;
        this.nextDirection = Direction.NONE;
        this.speed = 1;
        this.powerMode = false;
        this.powerModeTimer = 0;
        this.size = size;
    }
    
    /**
     * Gets the current position of Pac-Man.
     * 
     * @return The current position
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Gets the current direction of Pac-Man.
     * 
     * @return The current direction
     */
    public Direction getDirection() {
        return direction;
    }
    
    /**
     * Sets the next direction for Pac-Man to move in.
     * 
     * @param direction The next direction
     */
    public void setNextDirection(Direction direction) {
        this.nextDirection = direction;
    }
    
    /**
     * Checks if Pac-Man is in power mode.
     * 
     * @return True if Pac-Man is in power mode, false otherwise
     */
    public boolean isPowerMode() {
        return powerMode;
    }
    
    /**
     * Activates power mode for Pac-Man.
     */
    public void activatePowerMode() {
        powerMode = true;
        powerModeTimer = powerModeDuration;
    }
    
    /**
     * Sets the duration of power mode.
     * 
     * @param duration The duration in game ticks
     */
    public void setPowerModeDuration(int duration) {
        this.powerModeDuration = duration;
    }
    
    /**
     * Updates Pac-Man's position and state.
     * 
     * @param maze The maze
     */
    public void update(Maze maze) {
        // Try to change direction if a next direction is set
        if (nextDirection != Direction.NONE) {
            tryChangeDirection(maze);
        }
        
        // Move in the current direction if possible
        if (direction != Direction.NONE) {
            move(maze);
        }
        
        // Update power mode timer
        if (powerMode) {
            powerModeTimer--;
            if (powerModeTimer <= 0) {
                powerMode = false;
            }
        }
    }
    
    /**
     * Tries to change Pac-Man's direction to the next direction.
     * 
     * @param maze The maze
     */
    private void tryChangeDirection(Maze maze) {
        // Create a temporary position to test the move
        Position testPos = new Position(position);
        testPos.move(nextDirection);
        
        // Check if the move is valid (not into a wall)
        if (!maze.isWall(testPos)) {
            direction = nextDirection;
            nextDirection = Direction.NONE;
        }
    }
    
    /**
     * Moves Pac-Man in the current direction.
     * 
     * @param maze The maze
     */
    private void move(Maze maze) {
        // Create a temporary position to test the move
        Position testPos = new Position(position);
        testPos.move(direction);
        
        // Check if the move is valid (not into a wall)
        if (!maze.isWall(testPos)) {
            position = testPos;
        }
    }
    
    /**
     * Checks if Pac-Man is colliding with a ghost.
     * 
     * @param ghost The ghost to check collision with
     * @return True if Pac-Man is colliding with the ghost, false otherwise
     */
    public boolean isCollidingWith(Ghost ghost) {
        return position.equals(ghost.getPosition());
    }
    
    /**
     * Draws Pac-Man on the screen.
     * 
     * @param g The Graphics object to draw with
     * @param tileSize The size of a tile in pixels
     */
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.YELLOW);
        g.fillOval(
            position.getX() * tileSize, 
            position.getY() * tileSize, 
            tileSize, 
            tileSize
        );
    }
}