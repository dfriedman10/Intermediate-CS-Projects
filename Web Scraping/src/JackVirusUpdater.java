
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JackVirusUpdater {
	public static final int WIDTH = 400, LENGTH = 300, BAR = 75;
	private Document doc;
	private JTextArea output;

	public String[] search(String country) {
			Element table = doc.select("table#main_table_countries_today").get(0);
			Element rows = table.select("tbody").get(0);

			for (Element row : rows.select("tr")) {

				String rowCountry = row.select("td").get(1).text();
				if (country.equalsIgnoreCase(rowCountry)) {
					

					for (Element r : row.select("td"))
						System.out.println(r.text()+"\n");
					
					String[] out = new String[6];
					out[0] ="Total Cases: "+row.select("td").get(2).text();
					out[1] = "Active Cases: "+row.select("td").get(8).text();
					out[2] = "New Cases: "+row.select("td").get(3).text();
					out[3] = "Total Deaths: "+row.select("td").get(4).text();
					out[4] = "New Deaths: "+row.select("td").get(5).text();
					out[5] = "Total Recovered: "+row.select("td").get(6).text();
					return out;
				}
				
			}
		return null;
	}

	public static void main(String args[]) {
		try {
			new JackVirusUpdater();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public JackVirusUpdater() throws IOException {
		doc = Jsoup.connect("https://www.worldometers.info/coronavirus/#countries").get();
		JTextField input = new JTextField();
		input.setEditable(true);
		input.setPreferredSize(new Dimension(WIDTH, BAR));
		input.setText("Enter the name of the country you would like to be updated about");
		input.setForeground(Color.BLACK);
		input.setEditable(true);
		input.setMargin(new Insets(10, 10, 10, 10));

		output = new JTextArea();
		output.setForeground(Color.BLACK);
		output.setEditable(false);
		output.setPreferredSize(new Dimension(WIDTH, LENGTH - BAR));
		output.setMargin(new Insets(10, 10, 10, 10));

		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, 1);
		panel.setLayout(layout);
		panel.add(output);
		panel.add(input);
		JFrame frame = new JFrame();
		frame.setSize(WIDTH, LENGTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);

		input.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				input.setText("");
			}
		});
		input.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {

				
			}

			public void keyPressed(KeyEvent e) {if (e.getKeyChar() == '\n') {
				String[] out = search(input.getText().trim());
				if (out == null) 
					output.setText("Country/Continent not found");
				else {
					String o = "";
					for (String s : out) 
						o += s + "\n";
					output.setText(o);
				}
			}
			}

			public void keyReleased(KeyEvent keyevent) {
			}
		});

	}

}
