// Program to analyze actor connections from an imdb database

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

public class KBGraphics {
	
	private static final int WIDTH = 600, HEIGHT = 600;
	private JTextField box1, box2;
	private JTextArea displayarea;
	private LabeledGraph<String, String> graph;
	
	// runs a bfs to find the path between 2 actors
	public void connectingActors() {
		String a1 = box1.getText().trim(), a2 = box2.getText().trim();
		if (a1.length() == 0 || a2.length() == 0) {
			displayarea.setText("\n  Please enter 2 actors to start the search");
			return;
		}
		String output = "";
		for (String s : KevinBacon.actorSearch(a1, a2, graph))
			output += s;
		displayarea.setText("\n  " + output);
	}
	
	public void furthestActor() {
		String a1 = box1.getText().trim();
		if (a1.length() == 0) {
			displayarea.setText("\n  Please enter an actor in box #1 to start the search");
			return;
		}
		String output = "";
		for (String s : KevinBacon.furthestActor(a1, graph))
			output += s;
		displayarea.setText("\n  " + output);
	}
	
	public void mostConnected() {
		String a1 = box1.getText().trim();
		if (a1.length() == 0) {
			displayarea.setText("\n  Please enter an actor in box #1 to start the search");
			return;
		}
		String output = "";
		for (String s : KevinBacon.mostConnected(a1, graph))
			output += s;
		displayarea.setText("\n  " + output);
	}
	
	public void avgConnectivity() {
		String a1 = box1.getText().trim();
		if (a1.length() == 0) {
			displayarea.setText("\n  Please enter an actor in box #1 to start the search");
			return;
		}
		displayarea.setText("\n  " + KevinBacon.avgConnectivity(a1, graph));
	}
	
	public KBGraphics() {
		
		// the main container
		JFrame frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// the inner container
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);
		panel.setBorder(BorderFactory.createTitledBorder("The Kevin Bacon Game"));
		
		// text container
		displayarea = new JTextArea();
		displayarea.setEditable(false);
		
		// create and add listeners to the buttons
		JButton button1 = new JButton("Actors' Connecting Movies");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectingActors();
			}
		});
		JButton button2 = new JButton("Furthest Actor");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				furthestActor();
			}
		});
		JButton button3 = new JButton("Actor's Average Connectivity");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				avgConnectivity();
			}
		});
		JButton button4 = new JButton("Most Direct Connections");
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostConnected();
			}
		});
		
		JLabel label1 = new JLabel(), label2 = new JLabel();
		label1.setPreferredSize(new Dimension(70, 20));
		label1.setText("Actor #1: ");
		label2.setText("Actor #2: ");
		label1.setBackground(panel.getBackground());
		label2.setBackground(panel.getBackground());
		box1 = new JTextField(); box2 = new JTextField();
		box1.getInputMap().put(KeyStroke.getKeyStroke('\n'), "ENTER");
		box1.getActionMap().put("ENTER", new EnterAction());
		box2.getInputMap().put(KeyStroke.getKeyStroke('\n'), "ENTER");
		box2.getActionMap().put("ENTER", new EnterAction());
		box1.setPreferredSize(new Dimension(150, 20));
		box2.setPreferredSize(new Dimension(150, 20));
		JPanel innerPanel4 = new JPanel();
		innerPanel4.add(label1); innerPanel4.add(box1);
		innerPanel4.add(label2); innerPanel4.add(box2);
		panel.add(innerPanel4);
		
		// add a scroll bar to the text area
		JScrollPane scroll = new JScrollPane (displayarea);
		scroll.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(400,475));
		panel.add(scroll);
		
		// create three containers for the three button rows 
		JPanel innerPanel2 = new JPanel(),innerPanel3 = new JPanel();
		innerPanel2.add(button1);
		innerPanel2.add(button2);
		innerPanel3.add(button3);
		innerPanel3.add(button4);
		panel.add(innerPanel2);
		panel.add(innerPanel3);
		
		// final setup on the frame
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	
		// beginning text display
		displayarea.setText("\n   Loading Files, please wait...");
		box1.setEditable(false); box2.setEditable(false);
		
		try {
			graph = KevinBacon.loadFiles();
			displayarea.setText("\n   Files loaded. Enter an actor's name to begin.");
			box1.setEditable(true); box2.setEditable(true);
		}
		catch(IOException e) {
			displayarea.setText("Error loading files, please try again.");
		}
	}
	
	private class EnterAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			connectingActors();
		}
	}
	
	public static void main(String[] args) {
		new KBGraphics();
	}
}
