
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class WeatherFetcher {
	public static int width = 500, height = 400, cityHeight = 80;
	
	public static void run(JTextArea out, String text) {
		text.replace(" ", "+");
		
		try {
			String content = "";
			Document doc = Jsoup.connect("https://www.google.com/search?q="+text+"+weather").get();
			Element sect = doc.getElementById("wob_wc");
			
			content += sect.getElementById("wob_loc").text()+"\n";	//location
			content += sect.getElementById("wob_dts").text()+"\n\n";	//time/date
			content += "It is currently "+sect.select("img").first().attr("alt")+" and "
					+sect.getElementById("wob_ttm").text()+"°C ("+sect.getElementById("wob_tm").text() +
					"°F)\n\n";
			content += "Chance of precipitation is "+sect.getElementById("wob_pp").text()+"\n";
			content += "Humidity is " + sect.getElementById("wob_hm").text() +"\n";
			content += "Winds are blowing "+sect.getElementById("wob_tws").text() +" ("+
					sect.getElementById("wob_ws").text()+")";


			out.setForeground(Color.black);
			out.setText(content);
			
		}
		catch(IOException e) {
			out.setText("Couldn't connect to Google weather");
		}
		catch(NullPointerException e) {
			out.setText("There was a problem accessing this city's weather");
		}
	}

	public static void main(String[] args) {
		
		JTextField in = new JTextField();
		in.setEditable(true);
		in.setPreferredSize(new Dimension(width,cityHeight));
		in.setText("Enter a City");
		in.setForeground(Color.gray);
		in.setMargin(new Insets(10,10,10,10));
		
		// output box & scroll bar setup
		JTextArea out = new JTextArea();
		out.setForeground(Color.gray);
		out.setText("Enter a city to begin");
		out.setEditable(false);
		out.setPreferredSize(new Dimension(width,height-cityHeight));
		out.setMargin(new Insets(10,10,10,10));
		
		// when user hits enter, run our wiki search
		in.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					if (in.getText().trim().equals("Enter a City") ||
							in.getText().trim().equals("")) {
						out.setText("Please enter a city");
						return;
					}
					run(out, in.getText().trim());
				}
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		
		// I'll display a prompt until the user presses on the input box
		in.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				in.setText("");
				in.setForeground(Color.black);
			}
		});
		
		// inner panel setup
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);
		panel.setBorder(BorderFactory.createTitledBorder("Weather Search"));
		panel.add(out);
		panel.add(in);
		
		// frame setup - I'll let the user resize this one
		JFrame frame = new JFrame();
		frame.setSize(width,height);
		frame.setResizable(true);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
	}
}
