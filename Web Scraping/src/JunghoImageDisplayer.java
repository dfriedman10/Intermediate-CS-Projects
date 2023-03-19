import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JunghoImageDisplayer {

	public static void main(String[] args) {
		try {
			 
	        //Connect to the website and get the html
	        Document doc = Jsoup.connect("https://www.google.com/").get();
	
	        //Get all elements with img tag ,
	        Element img = doc.getElementsByTag("img").first();
	        String src = img.absUrl("src");
     
            //Exctract the name of the image from the src attribute
            int indexname = src.lastIndexOf("/");
     
            if (indexname == src.length()) {
                src = src.substring(1, indexname);
            }
     
            indexname = src.lastIndexOf("/");
            String name = src.substring(indexname, src.length());
     
            System.out.println(name);
     
            //Open a URL Stream
            URL url = new URL(src);
            BufferedImage image = ImageIO.read(url);
            
            JFrame frame = new JFrame() {
            		public void paint(Graphics g) {
            			g.drawImage(image, 0,0,100,100,null);
            		}
            };
            frame.setSize(500,500);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    } catch (IOException ex) {
	        System.err.println("There was an error");
	    }
	}
}
