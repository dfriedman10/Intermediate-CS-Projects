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


public class NadineCoronaCases {
	
	public final int WIDTH = 1000, HEIGHT = 700;
	
	private JTextField in;	
	private JTextArea out;
	private JFrame frame;
	
	private HashMap<String, String> countryData = new HashMap<String,String>();
	private HashMap<String, String> countryNames = new HashMap<String,String>();
	

	public NadineCoronaCases() {
		
		in = new JTextField();
		in.setEditable(true);
		in.setPreferredSize(new Dimension(WIDTH,100));
		in.setText("Enter a country");
		in.setForeground(Color.gray);
		in.setMargin(new Insets(10,10,10,10));
		
		
		in.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) 
					getInfo(in.getText().trim());		
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
		
		// output box & scroll bar setup
		out = new JTextArea();
		out.setEditable(false);
		out.setMargin(new Insets(10,10,10,10));
		out.setLineWrap(true);
		out.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane (out);
		scroll.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(WIDTH, HEIGHT-100));
		
		// inner panel setup
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);
		panel.setBorder(BorderFactory.createTitledBorder("Corona Updates"));
		panel.add(scroll);
		panel.add(in);
		
		
		Document doc;
		try {
			doc = Jsoup.connect("https://www.worldometers.info/coronavirus/?").get();
		
		
	
		
		Elements countries = doc.select("tr");
		
		ArrayList<String> labels = new ArrayList<String>();
		for (Element label : doc.select("tr").get(0).select("th")) {
			labels.add(label.text());
		}
		//labels.remove(0);
		
		for (Element row : countries) {
			Elements countryLink = row.select("a");
			if (countryLink.size() > 0) {
				String text = ""; int i = 0;
				for (Element data : row.select("td")) {

					if (i!=0&&i!=12) {
						text += labels.get(i)+": ";
						text += (data.text().length()==0? "0":data.text())+"\n";
					}
					else if (i==0) {
						countryNames.put(countryLink.attr("href").split("/")[1].replace("-", " "), data.text());
					}
					i++;
				}
				
					countryData.put(countryLink.attr("href").split("/")[1].replace("-", " "), text);
				i=0;
				
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// frame setup - I'll let the user resize this one
		frame = new JFrame();
		frame.setSize(WIDTH,HEIGHT);
		frame.setResizable(true);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(100, 0);
		frame.setVisible(true);
	}
		
	// scrapes text from a webpage
	public void getInfo(String topic) {
		
		// changes text boxes while searching
		in.setText("");
		out.setText("Searching database...");
		out.update(out.getGraphics());
		
		if(countryNames.get(topic.toLowerCase()) != null)
		out.setText(countryNames.get(topic.toLowerCase()) +"\n\n"+countryData.get(topic.toLowerCase()));
		else
			out.setText("Country name not in database");
	}
	
	public static void main(String args[]){
		new NadineCoronaCases();
	}
}