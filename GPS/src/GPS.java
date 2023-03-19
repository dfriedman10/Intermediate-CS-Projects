import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GPS extends JPanel {
	
	private final boolean dijkstra = true;
		
	// constants that are predefined and won't change
	private final int width = 1100, height = 700;
	private final int circleWidth = 15;
	private Image img;
	private LocationGraph<String>.Vertex clicked;
	private ArrayList<LocationGraph<String>.Vertex> solution;
	LocationGraph<String> graph = new LocationGraph<String>();

	// very simple main method to get the game going
	public static void main(String[] args) {
		new GPS(); 
	}
 
	// does complicated stuff to initialize the graphics and key listeners
	public GPS() {
		loadGraph();
		img = getToolkit().getImage("World Map.jpg");
		img = getToolkit().getImage(getClass().getClassLoader().getResource("resources/World Map.jpg"));

		JFrame frame = new JFrame();
		frame.setSize(width+2, height+24);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(this);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		this.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				clicked = cityOn(e.getX(),e.getY());
				solution = null;
				repaint();
			}

			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (clicked != null) {
					LocationGraph<String>.Vertex cityOn = cityOn(e.getX(),e.getY());
					if (cityOn != null && !cityOn.equals(clicked)) {
						solution = dijkstra? graph.dijkstraSearch(clicked.info,cityOn(e.getX(),e.getY()).info) : 
							graph.BFSsearch(clicked.info,cityOn(e.getX(),e.getY()).info);
					}
				}
				
				repaint();
			}
		});
		this.setFocusable(true);
	}

	// defines what we want to happen anytime we draw the game.
	public void paint(Graphics g) {
		Font f = new Font("Serif", Font.BOLD, 15);
		g.setFont(f);
		g.setColor(Color.white);
		g.fillRect(0,0,width, height);
		g.drawImage(img,0,20, this);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		
		
		for (LocationGraph<String>.Vertex v : graph.vertices.values()) {
			g.setColor(Color.blue);
			g.fillOval(v.x, v.y, circleWidth, circleWidth);
			for (LocationGraph<String>.Edge e : v.edges) {
				LocationGraph<String>.Vertex v2 = e.getNeighbor(v);
				g2.drawLine(v.x+circleWidth/2, v.y+circleWidth/2,v2.x+circleWidth/2, v2.y+circleWidth/2);
				if (clicked != null)  {
					g.setColor(Color.red);
					g.drawString(""+(int)v.distance(v2), (v.x+v2.x)/2, (v.y+v2.y)/2+10);
					g.setColor(Color.blue);
				}
			}
			g.setColor(Color.BLACK);
			g.drawString(v.info, v.x-20, v.y+25);
			
		}
		if (solution != null) {
			g.setColor(Color.red);
			for (int i = 1; i < solution.size();i++) {
				LocationGraph<String>.Vertex v1 = solution.get(i),
						v2 = solution.get(i-1);
				g.fillOval(v1.x, v1.y, circleWidth, circleWidth);
				g2.drawLine(v1.x+circleWidth/2, v1.y+circleWidth/2,v2.x+circleWidth/2, v2.y+circleWidth/2);
			}
		}
		if (clicked != null) {
			g.setColor(Color.red);
			g.fillOval(clicked.x, clicked.y, circleWidth, circleWidth);
		}
	}

	private LocationGraph<String>.Vertex cityOn(int x, int y) {
		for (LocationGraph<String>.Vertex v : graph.vertices.values()) {
			if (x - v.x <= 25 && x - v.x >= 0)
				if (y - v.y <= 25 && y - v.y >= 0) {
					return v;
				}
		}
		return null;
	}
	
	public void loadGraph() {
		
		try {
//			BufferedReader in = new BufferedReader(new FileReader("Cities.txt"));
			BufferedReader in = new BufferedReader(
				    new InputStreamReader(
				        getClass().getClassLoader().getResourceAsStream(
				            "resources/Cities.txt")));
			String line = "";
			while ( (line = in.readLine()) != null) {
				String[] info = line.split(",");
				graph.addVertex(info[0], Integer.parseInt(info[1]),Integer.parseInt(info[2]));
			}
			in.close();
			
			in = new BufferedReader(
				    new InputStreamReader(
				        getClass().getClassLoader().getResourceAsStream(
				            "resources/Cities.txt")));			while ( (line = in.readLine()) != null) {
				String[] info = line.split(",");
				for (int i = 3; i < info.length; i++) 
					graph.connect(info[0], info[i]);
				
			}
			in.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}


