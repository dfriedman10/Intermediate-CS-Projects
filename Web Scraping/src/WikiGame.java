import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


public class WikiGame {
	
	private final int WIDTH = 500, HEIGHT = 300, INHEIGHT = 70;
	private JTextField in, in2;
	private JTextArea out;
	
	public WikiGame() {
		
		// input box setup
		in = new JTextField();
		in.setEditable(true);
		in.setPreferredSize(new Dimension(WIDTH,INHEIGHT));
		in.setText("Enter a starting topic");
		in.setForeground(Color.gray);
		in.setMargin(new Insets(10,10,10,10));
		
		in2 = new JTextField();
		in2.setEditable(true);
		in2.setPreferredSize(new Dimension(WIDTH,INHEIGHT));
		in2.setText("Enter a target topic");
		in2.setForeground(Color.gray);
		in2.setMargin(new Insets(10,10,10,10));
		
		// when user hits enter, run our wiki search
		in.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					if (in.getText().trim().equals("Enter a starting topic") ||
							in.getText().trim().equals("")) {
						out.setText("Enter a starting topic");
						return;
					}
					if (in2.getText().trim().equals("Enter a target topic") ||
							in2.getText().trim().equals("")) {
						out.setText("Enter a target topic");
						return;
					}
					run(in.getText().trim(), in2.getText().trim());
				}
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		
		in2.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					if (in.getText().trim().equals("Enter a starting topic") ||
							in.getText().trim().equals("")) {
						out.setText("Enter a starting topic");
						return;
					}
					if (in2.getText().trim().equals("Enter a target topic") ||
							in2.getText().trim().equals("")) {
						out.setText("Enter a target topic");
						return;
					}
					run(in.getText().trim(), in2.getText().trim());
				}
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		
		// I'll display a prompt until the user presses on the input box
		in.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				in.setText("");
				in.setForeground(Color.black);
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		in2.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				in2.setText("");
				in2.setForeground(Color.black);
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		// output box & scroll bar setup
		out = new JTextArea();
		out.setForeground(Color.gray);
		out.setText("Enter starting and target topics to begin");
		out.setEditable(false);
		out.setMargin(new Insets(10,10,10,10));
		JScrollPane scroll = new JScrollPane (out);
		scroll.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(WIDTH, HEIGHT-INHEIGHT));
		
		// inner panel setup
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);
		panel.setBorder(BorderFactory.createTitledBorder("Wikipedia Game"));
		panel.add(scroll);
		JPanel innerPanel = new JPanel();
		BoxLayout boxlayout2 = new BoxLayout(innerPanel, BoxLayout.X_AXIS);
		innerPanel.setLayout(boxlayout2);
		innerPanel.add(in);
		innerPanel.add(in2);
		panel.add(innerPanel);
		
		// frame setup - I'll let the user resize this one
		JFrame frame = new JFrame();
		frame.setSize(WIDTH,HEIGHT);
		frame.setResizable(true);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200, 100);
		frame.setVisible(true);	
	}
	
	public void run(String begin, String end){
		try {
			in.setText("");
			in2.setText("");
			out.setText("Searching database...");
			out.update(out.getGraphics());
			
			begin = begin.replace(' ', '+');
			
			// starting doc
			String curr = "https://en.wikipedia.org/w/index.php?search="+begin+"&title=Special%3ASearch&profile=advanced&fulltext=1&advancedSearch-current=%7B%22namespaces%22%3A%5B0%5D%7D&ns0=1";
			String endUrl = "https://en.wikipedia.org/w/index.php?search="+end.replace(' ', '+')+"&title=Special%3ASearch&profile=advanced&fulltext=1&advancedSearch-current=%7B%22namespaces%22%3A%5B0%5D%7D&ns0=1";
			Document page = Jsoup.connect(curr).get();
			if (page.select("p.mw-search-createlink").size() > 0) {
				if (page.select("div.searchdidyoumean").size() > 0) {
					out.setText("Starting topic not found.\nDid you mean " + page.select("div.searchdidyoumean").first().select("a").first().text() +"?");
					resetInBoxes();
					return;
				}
				else if (page.select("ul.mw-search-results").size() > 0) 
					curr = "https://en.wikipedia.org" + page.select("ul.mw-search-results").first().select("a").first().attr("href");
				else {
					out.setText("Starting topic not in Wikipedia database");
					resetInBoxes();
					return;
				}
			}
			else 
				curr = "https://en.wikipedia.org" + page.select("ul.mw-search-results").first().select("a").first().attr("href");

			if (Jsoup.connect(endUrl).get().select("p.mw-search-exists").size()==0) {
				if (page.select("div.searchdidyoumean").size() != 0) 
					out.setText("Target topic not found.\nDid you mean " + page.select("div.searchdidyoumean").first().select("a").first().text() +"?");
				else 
					out.setText("Target topic not in Wikipedia database");
				resetInBoxes();
				return;
			}
			
			
			page = Jsoup.connect(curr).get();
			
			if (page.select("div#bodyContent").first().select("p").text().contains("may refer to")) {
				int i = 1;
				while (page.select("div#bodyContent").first().select("ul").get(i).select("a").first().attr("href").contains("#"))
					i++;
				curr = "https://en.wikipedia.org"+page.select("div#bodyContent").first().select("ul").get(i).select("a").first().attr("href");
			}
			
			ArrayList<String> toVisit = new ArrayList<String>();
			toVisit.add(curr);
			ArrayList<String> visited = new ArrayList<String>();
			HashMap<String, String> path = new HashMap<String, String>();
			path.put(curr, null);
			
			while (!toVisit.isEmpty()) {
				visited.add(curr = toVisit.remove(0));
				page = Jsoup.connect(curr).get();
				
				Elements links = page.select("div#bodyContent").select("a");
				
				for (int i = 0; i < links.size(); i++) {
					String url = links.get(i).attr("abs:href");
					
					if (url.contains("cite") || !url.contains("https://en.wikipedia.org/wiki/")
							|| url.substring(6).contains(":") || url.contains("#"))
						continue;
					
					if (url.toLowerCase().contains(end.toLowerCase().replace(' ', '_'))) {
						String[] name = url.substring(url.indexOf("/wiki/") + 6).toLowerCase().split("_");
						String[] target = end.toLowerCase().split(" ");
						boolean cont = false;
						for (String t : target) {
							boolean contained = false;
							for (String n : name)
								if (n.equalsIgnoreCase(t))
									contained = true;
							if (!contained) {
								cont = true;
								break;
							}
						}
						if (!cont) {
							path.put(url, curr);
							backTrace(path, url);
							return;
						}
					}
					
					if (!visited.contains(url) && !toVisit.contains(url)) {
						toVisit.add(url);
						path.put(url, curr);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void backTrace(HashMap<String, String> path, String end) {
		ArrayList<String> links = new ArrayList<String>();
		while (end != null) {
			links.add(end.substring(end.indexOf("wiki/")+5).replace('_', ' '));
			end = path.get(end);
		}
		String output = "";
		for (int i = links.size()-1; i >=0; i--)
			output += links.get(i) + "\n";
		
		resetInBoxes();
		out.setForeground(Color.black);
		out.setText(output);
	}
	
	public void resetInBoxes() {
		in.setForeground(Color.gray);
		in2.setForeground(Color.gray);
		in.setText("Enter a starting topic");
		in2.setText("Enter a target topic");
	}
	
	public static void main(String[] args) {
		new WikiGame();                 
	}
}