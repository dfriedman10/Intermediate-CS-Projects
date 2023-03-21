
// class that generates a maze, then uses student-built bots
// to solve the maze blindly

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class MazeRunner {
	
	private int speed = 100; // default is 100- smaller = slower
	
	private boolean animate = false;	// whether you want to animate maze creation

	
	// constructs and adds the bots competing into the maze
	private void addBots() {
		Bot[] bots = {
				new SuperBot(this, Color.black), new RandomBot(this, Color.blue)};
		for (Bot b : bots)
			robots.put(b, new RobotInfo());
	}
	
	// ****************************************************************//
	// feel free to peruse the below code, but you don't need to touch it.
	
	private int WWIDTH = 700, WHEIGHT = 700;
	public final int ROWS = 25, COLS = 25;
	private int BOXW = WWIDTH / ROWS, BOXH = WHEIGHT / COLS;
	private int count = 0;
	private boolean paused = false;
	private double PCTFILLED = .8;

	// true = white, false = black
	private Maze maze;
	
	private HashMap<Bot, RobotInfo> robots = new HashMap<Bot, RobotInfo>();
	
	private Bot lastbot = null;
	
	private JFrame frame;
	

		
	// class to keep track of robots' locations
	private class RobotInfo {
		private Point loc = new Point(maze.start.x, maze.start.y), dir = new Point(1,0);
		public String toString() { return loc.x + ", " + loc.y;}
	}
	
	public boolean move(Bot b) {
		
		// not allowed to move twice in one turn
		if (b.equals(lastbot)) {
			System.out.println(b.getClass() + " tried to move twice in one turn. Disqualified");
			robots.put(b, null);
			return false;
		}
		lastbot = b;
		
		// tries to move the bot in its current direction
		RobotInfo r = robots.get(b);
		if (r.loc.x+r.dir.x < 0) return false;
		if (maze.grid[r.loc.y+r.dir.y][r.loc.x+r.dir.x]) {
			r.loc.x += r.dir.x; r.loc.y+=r.dir.y;
			return true;
		}
		
		// if it runs into a wall
		else return false;
	}
	
	public void turnLeft(Bot b) {
		
		// not allowed to move twice in one turn
		if (b.equals(lastbot) && robots.size()>1) {
			System.out.println(b.getClass() + " tried to move twice in one turn. Disqualified");
			robots.put(b, null);
			return;
		}
		lastbot = b;
		// turns the bot left
		RobotInfo r = robots.get(b);
		if (r.dir.x == 1) r.dir = new Point(0,-1);
		else if (r.dir.y == -1) r.dir = new Point(-1,0);
		else if (r.dir.x == -1) r.dir = new Point(0,1);
		else r.dir = new Point(1,0);
	}

	public MazeRunner() {
		
		maze = new Maze(ROWS, COLS, PCTFILLED);


		frame = new JFrame();
		frame.setSize(WWIDTH, WHEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel canvas = new JPanel() {
			public void paintComponent(Graphics g) {
				g.setColor(Color.white);
				g.fillRect(0, 0, WWIDTH, WHEIGHT);
				g.setColor(Color.BLACK);
				int extraPixelsW = WWIDTH - BOXW * COLS;
				int extraPixelsH = WHEIGHT - BOXH * ROWS;
				for (int i = 0; i < maze.grid.length; i++) {
					int shiftH = i < extraPixelsH ? 1 : 0;
					for (int j = 0; j < maze.grid[i].length; j++) {
						int shiftW = j < extraPixelsW ? 1 : 0;
						if (!maze.grid[i][j])
							g.fillRect(j * BOXW + Math.min(j, extraPixelsW), i * BOXH + Math.min(i, extraPixelsH),
									BOXW + shiftW, BOXH + shiftH);
						else
							g.drawRect(j * BOXW + Math.min(j, extraPixelsW), i * BOXH + Math.min(i, extraPixelsH),
									BOXW + shiftW, BOXH + shiftH);

					}
				}
//				g.setColor(Color.GREEN);
//				if (goal != null)
//					g.fillRect(goal.x * BOXW + Math.min(extraPixelsW, goal.x), goal.y * BOXH + Math.min(extraPixelsH, goal.y),
//						BOXW, BOXH);
				for (Bot b : robots.keySet()) {
					if (robots.get(b) == null) continue;
					g.setColor(b.color);
					g.fillOval(robots.get(b).loc.x * BOXW + Math.min(extraPixelsW, robots.get(b).loc.x),
							robots.get(b).loc.y * BOXH + Math.min(extraPixelsH, robots.get(b).loc.y), BOXW, BOXH);
				}
				
//				g.setColor(Color.blue);
//				g.fillRect(p1.x * BOXW + Math.min(extraPixelsW, p1.x), p1.y * BOXH + Math.min(extraPixelsH, p1.y),
//						BOXW, BOXH);
//				g.setColor(Color.blue);
//				g.fillRect(p2.x * BOXW + Math.min(extraPixelsW, p2.x), p2.y * BOXH + Math.min(extraPixelsH, p2.y),
//						BOXW, BOXH);
			}
		};
		
		// resizes the maze as the user drags edges
		canvas.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				WWIDTH = canvas.getWidth();
				WHEIGHT = canvas.getHeight();
				BOXW = WWIDTH / ROWS;
				BOXH = WHEIGHT / COLS;
			}
		});

		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, false), "Pause");
		canvas.getActionMap().put("Pause", new PauseAction());
		
		frame.getContentPane().repaint();
		
		maze.fillMaze(animate, frame);
		
		addBots();
		
		// runs the maze until a robot has reached the end
		while (true) {
			if (!paused) {
				lastbot = null;
				for(Bot b : robots.keySet()) {
					if (robots.get(b) == null) continue;
					b.move();
				}
				count++;
				checkForWin();
			}
			try {
				Thread.sleep(2000/speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			frame.getContentPane().repaint();
		}
	}

	private class PauseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			paused = !paused;
		}
	}
	
	private Bot checkForWin() {
		for (Bot b : robots.keySet())  {
			if (robots.get(b) == null) continue;
			if (robots.get(b).loc.x == COLS - 1) {
				System.out.println(b.getClass() + ": "+count);
				robots.put(b, null);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		new MazeRunner();
	}
}
