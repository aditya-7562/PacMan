/**
 * Enum representing the four possible movement directions in the game.
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT, NONE;
    
    /**
     * Returns the opposite direction.
     * 
     * @return The opposite direction
     */
    public Direction getOpposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return NONE;
        }
    }
}