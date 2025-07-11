import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the game maze.
 */
public class Maze {
    private Tile[][] tiles;
    private int width;
    private int height;
    private int tileSize;
    private List<Pellet> pellets;
    private int totalPellets;
    private int pelletsEaten;
    
    // Starting positions
    private Position pacmanStart;
    private List<Position> ghostStarts;
    
    /**
     * Creates a new Maze with the specified dimensions.
     * 
     * @param width The width of the maze in tiles
     * @param height The height of the maze in tiles
     * @param tileSize The size of each tile in pixels
     */
    public Maze(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.tiles = new Tile[width][height];
        this.pellets = new ArrayList<>();
        this.ghostStarts = new ArrayList<>();
        this.pelletsEaten = 0;
        
        // Initialize the maze with a default layout
        initializeDefaultMaze();
    }
    
    /**
     * Initializes the maze with a default layout.
     */
    private void initializeDefaultMaze() {
        // 0 = path, 1 = wall, 2 = pellet, 3 = power pellet, 4 = pacman start, 5 = ghost start
        int[][] layout = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 3, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 0, 1, 0, 1, 1, 1, 2, 1, 1, 1, 1},
            {0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0},
            {1, 1, 1, 1, 2, 1, 0, 1, 1, 5, 1, 1, 0, 1, 2, 1, 1, 1, 1},
            {0, 0, 0, 0, 2, 0, 0, 1, 5, 5, 5, 1, 0, 0, 2, 0, 0, 0, 0},
            {1, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 1},
            {0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0},
            {1, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
            {1, 3, 2, 1, 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 1, 2, 3, 1},
            {1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        
        // Create tiles based on the layout
        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[y].length; x++) {
                int type = layout[y][x];
                
                // Handle special tiles
                if (type == 4) { // Pacman start
                    pacmanStart = new Position(x, y);
                    tiles[x][y] = new Tile(Tile.PATH, x, y, tileSize);
                } else if (type == 5) { // Ghost start
                    ghostStarts.add(new Position(x, y));
                    tiles[x][y] = new Tile(Tile.PATH, x, y, tileSize);
                } else {
                    tiles[x][y] = new Tile(type, x, y, tileSize);
                    
                    // Add pellets to the list
                    if (type == Tile.PELLET) {
                        pellets.add(new Pellet(new Position(x, y), false, tileSize));
                        totalPellets++;
                    } else if (type == Tile.POWER_PELLET) {
                        pellets.add(new Pellet(new Position(x, y), true, tileSize));
                        totalPellets++;
                    }
                }
            }
        }
    }
    
    /**
     * Gets the width of the maze in tiles.
     * 
     * @return The maze width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the maze in tiles.
     * 
     * @return The maze height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the size of a tile in pixels.
     * 
     * @return The tile size
     */
    public int getTileSize() {
        return tileSize;
    }
    
    /**
     * Gets the tile at the specified position.
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The tile at the specified position, or null if the position is out of bounds
     */
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[x][y];
        }
        return null;
    }
    
    /**
     * Gets the tile at the specified position.
     * 
     * @param position The position
     * @return The tile at the specified position, or null if the position is out of bounds
     */
    public Tile getTile(Position position) {
        return getTile(position.getX(), position.getY());
    }
    
    /**
     * Checks if the specified position is a wall.
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return True if the position is a wall, false otherwise
     */
    public boolean isWall(int x, int y) {
        Tile tile = getTile(x, y);
        return tile != null && tile.isWall();
    }
    
    /**
     * Checks if the specified position is a wall.
     * 
     * @param position The position
     * @return True if the position is a wall, false otherwise
     */
    public boolean isWall(Position position) {
        return isWall(position.getX(), position.getY());
    }
    
    /**
     * Gets the starting position for Pac-Man.
     * 
     * @return The starting position for Pac-Man
     */
    public Position getPacmanStart() {
        return pacmanStart;
    }
    
    /**
     * Gets the starting positions for ghosts.
     * 
     * @return The starting positions for ghosts
     */
    public List<Position> getGhostStarts() {
        return ghostStarts;
    }
    
    /**
     * Gets the list of pellets in the maze.
     * 
     * @return The list of pellets
     */
    public List<Pellet> getPellets() {
        return pellets;
    }
    
    /**
     * Gets the total number of pellets in the maze.
     * 
     * @return The total number of pellets
     */
    public int getTotalPellets() {
        return totalPellets;
    }
    
    /**
     * Gets the number of pellets that have been eaten.
     * 
     * @return The number of pellets eaten
     */
    public int getPelletsEaten() {
        return pelletsEaten;
    }
    
    /**
     * Checks if all pellets have been eaten.
     * 
     * @return True if all pellets have been eaten, false otherwise
     */
    public boolean allPelletsEaten() {
        return pelletsEaten >= totalPellets;
    }
    
    /**
     * Eats the pellet at the specified position.
     * 
     * @param position The position
     * @return The points earned, or 0 if there was no pellet at the position
     */
    public int eatPellet(Position position) {
        for (Pellet pellet : pellets) {
            if (!pellet.isEaten() && pellet.getPosition().equals(position)) {
                pellet.eat();
                pelletsEaten++;
                return pellet.getPoints();
            }
        }
        return 0;
    }
    
    /**
     * Checks if there is a power pellet at the specified position.
     * 
     * @param position The position
     * @return True if there is a power pellet at the position, false otherwise
     */
    public boolean isPowerPellet(Position position) {
        for (Pellet pellet : pellets) {
            if (!pellet.isEaten() && pellet.getPosition().equals(position) && pellet.isPowerPellet()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Draws the maze on the screen.
     * 
     * @param g The Graphics object to draw with
     */
    public void draw(Graphics g) {
        // Draw tiles
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x][y].draw(g);
            }
        }
    }
}