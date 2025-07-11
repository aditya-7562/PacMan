# Pac-Man Java Clone

A simple Pac-Man clone implemented in Java using Swing and AWT libraries.

## Description

This project is a basic implementation of the classic Pac-Man arcade game. The player controls Pac-Man through a maze, eating pellets while avoiding ghosts. Power pellets allow Pac-Man to temporarily eat the ghosts for bonus points.

## Features

- Tile-based maze with walls, paths, and pellets
- Player-controlled Pac-Man character
- Multiple ghosts with different behaviors
- Power pellets that allow Pac-Man to eat ghosts
- Score tracking
- Game over and win conditions

## Controls

- **Arrow Keys**: Move Pac-Man
- **Esc**: Exit the game

## How to Run

### Option 1: Using an IDE

1. Open the project in your favorite Java IDE (Eclipse, IntelliJ IDEA, etc.)
2. Run the `Main.java` file

### Option 2: Command Line

1. Navigate to the project directory
2. Compile the Java files:
   ```
   javac -d bin src/*.java
   ```
3. Run the game:
   ```
   java -cp bin Main
   ```

## Game Rules

- Move Pac-Man around the maze to eat all the pellets
- Avoid the ghosts - if they catch you, you lose
- Eat power pellets to temporarily make ghosts vulnerable
- Eat vulnerable ghosts for bonus points
- Complete the level by eating all pellets

## Project Structure

- `Main.java`: Entry point of the application
- `GameFrame.java`: Sets up the game window
- `GamePanel.java`: Handles the game loop, rendering, and input
- `PacMan.java`: Represents the player character
- `Ghost.java`: Represents the enemy characters
- `Maze.java`: Stores the maze layout
- `Pellet.java`: Represents collectible items
- `Tile.java`: Represents a single tile in the maze
- `Direction.java`: Enum for movement directions
- `Position.java`: Utility class for positions in the maze

⚠️ Note: This is a desktop Java application built with Swing and AWT. It does not run in web browsers. To play the game, please clone the repository and run it locally using a Java IDE or the command line.

## Credits

This game was created as a simple Java implementation of the classic Pac-Man game.