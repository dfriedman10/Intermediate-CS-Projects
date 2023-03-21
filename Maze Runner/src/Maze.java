
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Maze {
	

	public final int ROWS, COLS;

	// true = white, false = black
	public boolean[][] grid;
	
	private final int ANIMATESPEED = 10;
	
	private final double PCTFILLED;
	
	private ArrayList<Point> mainPathPoints;
	
	public Point start;
	
	private final double[] DIRECTIONWEIGHTS = {4, 3, 1}; // right, up/down, left
	
	
	public Maze(int rows, int cols, double pctFilled) {
		ROWS = rows;
		COLS = cols;
		PCTFILLED = pctFilled;
		grid = new boolean[ROWS][COLS];
	}
	
	
	public void fillMaze(boolean animate, JFrame frame) {
		grid = new boolean[ROWS][COLS];
		mainPathPoints = new ArrayList<Point>();
		Point cp = new Point(0, (int)(Math.random() * ROWS));
		start = cp;
		grid[cp.y][cp.x] = true;
		
		while (cp.x < COLS - 2) {
			if ((cp = randPoint(cp))== null) {
				fillMaze(animate, frame);
				return;
			}
			grid[cp.y][cp.x] = true;
			if (animate) {
				try {
					Thread.sleep(ANIMATESPEED);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.getContentPane().repaint();
			}
			mainPathPoints.add(cp);
		}
		grid[cp.y][cp.x+1] = true;
		
		if (animate)
			frame.getContentPane().repaint();
		
		while (pctFilled() < PCTFILLED && buildBranch(animate, frame));		
	}
	
	// returns a random neighbor that isn't already white and 
	// isn't adjacent to any white squares
	private Point randPoint(Point cp) {
		ArrayList<Point> nei = neighbors(cp, false);
		for (int i = 0; i < nei.size(); i++) {
			Point p = nei.get(i);
			if (p.x == COLS-2) return p;
			if (grid[p.y][p.x]) {
				nei.remove(i);
				i--;
			}
			
			else if (p.y < grid.length-1&& p.y+1 != cp.y && grid[p.y + 1][p.x]) {
				nei.remove(i); i--;}
			else if (p.x < grid[0].length-1 && p.x+1 != cp.x && grid[p.y][p.x+1]) {
				nei.remove(i);i--;}
			else if (p.y > 0 && p.y-1 != cp.y && grid[p.y - 1][p.x]) {
				nei.remove(i);i--;}
			else if (p.x > 0 && p.x-1 != cp.x && grid[p.y][p.x-1]) {
				nei.remove(i);i--;}
		}
		
		if (nei.size() == 0) return null;
		
		ArrayList<Double> prob = new ArrayList<Double>();
		double sum = 0;
		for (int i = 0; i < nei.size(); i++) {
			if (nei.get(i).x > cp.x)
				prob.add(DIRECTIONWEIGHTS[0]);
			else if (nei.get(i).x == cp.x)
				prob.add(DIRECTIONWEIGHTS[1]);
			else
				prob.add(DIRECTIONWEIGHTS[2]);
			
			sum += prob.get(i);
		}
		
		for (int i = 0; i < prob.size(); i++) {
			prob.set(i, prob.get(i)/sum);
		}
		for (int i = 1; i < prob.size(); i++) {
			prob.set(i, prob.get(i) + prob.get(i-1));
		}
		
		double randprob = Math.random();
		for (int i = 0; i < nei.size()-1; i++)
			if (prob.get(i) > randprob)
				return nei.get(i);
		
		return nei.get(nei.size()-1);
	}
	
	// returns the legal neighbors of point p
	private ArrayList<Point> neighbors(Point p, boolean filled) {
		ArrayList<Point> neighbors = new ArrayList<Point>();
		if (p.y > 1 && grid[p.y - 1][p.x] == filled)
			neighbors.add(new Point(p.x, p.y - 1));
		if (p.y < grid.length-2 && grid[p.y + 1][p.x] == filled)
			neighbors.add(new Point(p.x, p.y + 1));
		if (p.x > 1 && grid[p.y][p.x - 1] == filled)
			neighbors.add(new Point(p.x - 1, p.y));
		if (p.x < grid[0].length-2 && grid[p.y][p.x + 1]== filled)
			neighbors.add(new Point(p.x + 1, p.y));
		return neighbors;
	}
	// adds a side branch to the grid
	private boolean buildBranch(boolean animate, JFrame frame) {
		Point lastP = null;
		Point lastDir = null;
		int branchLength = (int)(Math.random()*grid.length+grid.length/4);
		if (mainPathPoints.size() <= 0) 
			return false;
		Point cp = mainPathPoints.remove((int)(Math.random()*mainPathPoints.size()));
		for (int i = 0; i < branchLength && cp.y < grid.length-1; i++) {
			if (animate) {
				try {
					Thread.sleep(75);
				} catch (InterruptedException e) {
					//  Auto-generated catch block
					e.printStackTrace();
				}
				frame.getContentPane().repaint();
			}
			cp = randPoint(cp);
			if (cp == null) {
				if (lastDir!= null && lastP.x+lastDir.x < grid[0].length-1 && lastP.x+lastDir.x > 0
						&& lastP.y+lastDir.y < grid.length && lastP.y+lastDir.y > 0)
					grid[lastP.y+lastDir.y][lastP.x+lastDir.x] = true;
				return true;
			}
			grid[cp.y][cp.x] = true;
			if (lastP!= null) lastDir = new Point(cp.x-lastP.x,cp.y-lastP.y);
			lastP = cp;
		}
		return true;
	}
	
	public double pctFilled() {
		double numFilled = 0;
		for (boolean[] row : grid) 
			for (boolean b : row) 
				if (b) numFilled++;
					
		return numFilled/(grid.length*grid[0].length);
	}

}
