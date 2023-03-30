import java.awt.Color;

public class SmartBot extends Bot {
	
	boolean solved = false;
	SmartBot minion;
	int mode = 0, turnCount = 0;

	public SmartBot(MazeRunner mr, Color c) {
		super(mr, c);
		minion = new SmartBot(mr, Color.cyan, false);
		

	}
	public SmartBot(MazeRunner mr, Color c, boolean dead) {
		super(mr, c);
	}
	
	
	
	public void move() {
		for (int i = 0; i < 20; i ++) {

		try {	
				if (mode == 0) {
					if (moveForward())
						mode = 1;
					else mode = 2;
				}
				else if (mode == 1) {
					turnLeft(); mode = 0;
				}
				else  { 
					turnLeft();
					if (turnCount < 2) turnCount ++;
					else {
						turnCount = 0; mode = 0;
					}
				}
				
				minion.turnLeft();
			
		}
		catch(NullPointerException e) {}
		}
		
	}
}