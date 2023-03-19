
import java.awt.Color;

public abstract class Bot {
	
	private MazeRunner maze;
	public Color color;
	
	public Bot(MazeRunner mr, Color c) {
		maze = mr;
		color = c;
	}
	
	public boolean moveForward() {
		return maze.move(this);
	}
	
	public void turnLeft() {
		maze.turnLeft(this);
	}
	
	// use this to move through the maze.
	// You are only allowed to call move once per turn.
	public abstract void move();
}
