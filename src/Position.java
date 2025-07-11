/**
 * Class representing a position in the game grid.
 */
public class Position {
    private int x;
    private int y;
    
    /**
     * Creates a new Position with the specified coordinates.
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Creates a copy of another Position.
     * 
     * @param position The Position to copy
     */
    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }
    
    /**
     * Gets the x-coordinate.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the y-coordinate.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Sets the x-coordinate.
     * 
     * @param x The new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Sets the y-coordinate.
     * 
     * @param y The new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Moves the position one step in the specified direction.
     * 
     * @param direction The direction to move in
     */
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
            default:
                break;
        }
    }
    
    /**
     * Checks if this position is equal to another position.
     * 
     * @param obj The object to compare with
     * @return True if the positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
    
    /**
     * Returns a string representation of this position.
     * 
     * @return A string in the format "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}