
import java.awt.Color;

public class RightBot extends Bot {
	
	String[] modes = {"forward","turnRight","turnLeft"};
	int mode = 0;
	int turnCount = 0;

	public RightBot(MazeRunner mr, Color c) {
		super(mr, c);
	}
	
	public void move() {
		if (mode == 0) {
			if (moveForward())
				mode = 1;
			else mode = 2;
		}
		else if (mode == 1) {
			turnLeft();
			if (turnCount < 2) turnCount ++;
			else {
				turnCount = 0; mode = 0;
			}
		}
		else  { 
			turnLeft(); mode = 0;

		}
		
	}
}
