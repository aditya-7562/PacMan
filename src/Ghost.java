import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Class representing a ghost enemy in the game.
 */
public class Ghost {
    // Ghost states
    public static final int STATE_NORMAL = 0;
    public static final int STATE_FRIGHTENED = 1;
    public static final int STATE_EATEN = 2;
    
    // Ghost types
    public static final int TYPE_RANDOM = 0;
    public static final int TYPE_CHASER = 1;
    
    private Position position;
    private Position startPosition;
    private Direction direction;
    private double speed;
    private int state;
    private int type;
    private Color color;
    private Random random;
    private int frightenedTimer;
    private int frightenedDuration = 300; // Default duration in game ticks (about 5 seconds at 60 FPS)
    
    /**
     * Creates a new Ghost at the specified position.
     * 
     * @param position The starting position
     * @param type The type of ghost (TYPE_RANDOM or TYPE_CHASER)
     * @param color The color of the ghost
     */
    public Ghost(Position position, int type, Color color) {
        this.position = position;
        this.startPosition = new Position(position);
        this.direction = Direction.UP; // Start moving up
        this.speed = 0.2;
        this.state = STATE_NORMAL;
        this.type = type;
        this.color = color;
        this.random = new Random();
        this.frightenedTimer = 0;
    }
    
    /**
     * Gets the current position of the ghost.
     * 
     * @return The current position
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Gets the current state of the ghost.
     * 
     * @return The current state
     */
    public int getState() {
        return state;
    }
    
    /**
     * Checks if the ghost is in the frightened state.
     * 
     * @return True if the ghost is frightened, false otherwise
     */
    public boolean isFrightened() {
        return state == STATE_FRIGHTENED;
    }
    
    /**
     * Checks if the ghost has been eaten.
     * 
     * @return True if the ghost has been eaten, false otherwise
     */
    public boolean isEaten() {
        return state == STATE_EATEN;
    }
    
    /**
     * Sets the ghost to the frightened state.
     */
    public void frighten() {
        if (state != STATE_EATEN) {
            state = STATE_FRIGHTENED;
            frightenedTimer = frightenedDuration;
            // Reverse direction when frightened
            direction = direction.getOpposite();
        }
    }
    
    /**
     * Sets the speed of the ghost.
     * 
     * @param speed The speed value (0.0 to 1.0)
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**
     * Gets the current speed of the ghost.
     * 
     * @return The current speed
     */
    public double getSpeed() {
        return speed;
    }
    
    /**
     * Sets the duration of the frightened state.
     * 
     * @param duration The duration in game ticks
     */
    public void setFrightenedDuration(int duration) {
        this.frightenedDuration = duration;
    }
    
    /**
     * Sets the ghost to the eaten state.
     */
    public void eat() {
        state = STATE_EATEN;
    }
    
    /**
     * Resets the ghost to its starting position and normal state.
     */
    public void reset() {
        position = new Position(startPosition);
        state = STATE_NORMAL;
        direction = Direction.UP;
    }
    
    /**
     * Updates the ghost's position and state.
     * 
     * @param maze The maze
     * @param pacman The player character
     */
    public void update(Maze maze, PacMan pacman) {
        // Update frightened timer
        if (state == STATE_FRIGHTENED) {
            frightenedTimer--;
            if (frightenedTimer <= 0) {
                state = STATE_NORMAL;
            }
        }
        
        // If eaten, move back to the starting position
        if (state == STATE_EATEN) {
            // Simple implementation: just reset the ghost
            reset();
            return;
        }
        
        // Determine the next direction based on the ghost type
        Direction nextDirection = determineNextDirection(maze, pacman);
        
        // Move in the determined direction
        if (nextDirection != Direction.NONE) {
            direction = nextDirection;
            Position nextPos = new Position(position);
            nextPos.move(direction);
            position = nextPos;
        }
    }
    
    /**
     * Determines the next direction for the ghost to move in.
     * 
     * @param maze The maze
     * @param pacman The player character
     * @return The next direction to move in
     */
    private Direction determineNextDirection(Maze maze, PacMan pacman) {
        // If the ghost is at a wall or at an intersection, choose a new direction
        Position nextPos = new Position(position);
        nextPos.move(direction);
        
        if (maze.isWall(nextPos) || isAtIntersection(maze)) {
            // Get possible directions (excluding the opposite of the current direction)
            Direction[] possibleDirections = getPossibleDirections(maze);
            
            if (possibleDirections.length == 0) {
                // No possible directions, try including the opposite direction
                Direction opposite = direction.getOpposite();
                nextPos = new Position(position);
                nextPos.move(opposite);
                if (!maze.isWall(nextPos)) {
                    return opposite;
                }
                return Direction.NONE;
            }
            
            // Choose a direction based on the ghost type and state
            if (state == STATE_FRIGHTENED) {
                // When frightened, move randomly
                return possibleDirections[random.nextInt(possibleDirections.length)];
            } else if (type == TYPE_CHASER && state == STATE_NORMAL) {
                // Chaser ghost: try to move towards Pac-Man
                return getDirectionTowardsPacMan(possibleDirections, pacman);
            } else {
                // Random ghost or other states: move randomly
                return possibleDirections[random.nextInt(possibleDirections.length)];
            }
        }
        
        // Continue in the current direction
        return direction;
    }
    
    /**
     * Checks if the ghost is at an intersection.
     * 
     * @param maze The maze
     * @return True if the ghost is at an intersection, false otherwise
     */
    private boolean isAtIntersection(Maze maze) {
        // Count the number of possible directions
        int count = 0;
        for (Direction dir : Direction.values()) {
            if (dir != Direction.NONE && dir != direction.getOpposite()) {
                Position nextPos = new Position(position);
                nextPos.move(dir);
                if (!maze.isWall(nextPos)) {
                    count++;
                }
            }
        }
        
        // If there are more than 1 possible directions (excluding the current direction and its opposite),
        // then the ghost is at an intersection
        return count > 1;
    }
    
    /**
     * Gets the possible directions the ghost can move in.
     * 
     * @param maze The maze
     * @return An array of possible directions
     */
    private Direction[] getPossibleDirections(Maze maze) {
        // Count the number of possible directions
        int count = 0;
        Direction[] allDirections = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        for (Direction dir : allDirections) {
            if (dir != direction.getOpposite()) {
                Position nextPos = new Position(position);
                nextPos.move(dir);
                if (!maze.isWall(nextPos)) {
                    count++;
                }
            }
        }
        
        // Create an array of possible directions
        Direction[] possibleDirections = new Direction[count];
        int index = 0;
        for (Direction dir : allDirections) {
            if (dir != direction.getOpposite()) {
                Position nextPos = new Position(position);
                nextPos.move(dir);
                if (!maze.isWall(nextPos)) {
                    possibleDirections[index++] = dir;
                }
            }
        }
        
        return possibleDirections;
    }
    
    /**
     * Gets the direction that moves the ghost towards Pac-Man.
     * 
     * @param possibleDirections The possible directions the ghost can move in
     * @param pacman The player character
     * @return The direction that moves the ghost towards Pac-Man
     */
    private Direction getDirectionTowardsPacMan(Direction[] possibleDirections, PacMan pacman) {
        // If there are no possible directions, return NONE
        if (possibleDirections.length == 0) {
            return Direction.NONE;
        }
        
        // Find the direction that minimizes the distance to Pac-Man
        Direction bestDirection = possibleDirections[0];
        double minDistance = Double.MAX_VALUE;
        
        for (Direction dir : possibleDirections) {
            Position nextPos = new Position(position);
            nextPos.move(dir);
            
            // Calculate the distance to Pac-Man
            double distance = calculateDistance(nextPos, pacman.getPosition());
            
            // Update the best direction if this one is better
            if (distance < minDistance) {
                minDistance = distance;
                bestDirection = dir;
            }
        }
        
        return bestDirection;
    }
    
    /**
     * Calculates the Euclidean distance between two positions.
     * 
     * @param pos1 The first position
     * @param pos2 The second position
     * @return The distance between the two positions
     */
    private double calculateDistance(Position pos1, Position pos2) {
        int dx = pos1.getX() - pos2.getX();
        int dy = pos1.getY() - pos2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Draws the ghost on the screen.
     * 
     * @param g The Graphics object to draw with
     * @param tileSize The size of a tile in pixels
     */
    public void draw(Graphics g, int tileSize) {
        if (state == STATE_FRIGHTENED) {
            g.setColor(Color.BLUE);
        } else if (state == STATE_EATEN) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(color);
        }
        
        g.fillRect(
            position.getX() * tileSize, 
            position.getY() * tileSize, 
            tileSize, 
            tileSize
        );
    }
}