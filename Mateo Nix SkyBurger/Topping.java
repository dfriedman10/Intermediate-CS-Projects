package skyBurger;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Topping extends Rectangle {
			//variables to store information
			Image img;
			String type;
			
			// constructor takes a location and the name of the image file as parameters
			public Topping(int x, int y, int w, int h, String imgName, String type) {
				super(x, y, w, h);
				//makes the type accessible
				this.type = type;
				//loads the image
				try {
					img = ImageIO.read(new File(imgName)).getScaledInstance(w, h, Image.SCALE_SMOOTH);
				} catch (IOException e) {
					System.out.println("Image file not found");
				}
				
			}
			
			// draws the image corresponding topping
			public void draw(Graphics g) {
				g.drawImage(img, x, y, null);
			}

}
