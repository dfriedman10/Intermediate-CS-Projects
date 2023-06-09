package skyBurger;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Burger extends Rectangle {

	private ArrayList<Image> burgerImgList = new ArrayList<Image>();
	int toppingheight =height / 4;
	Image img;
	int p = 0;

	// constructor takes in variables
	public Burger(int x, int y, int w, int h) {
		super(x, y, w, h);
		height = h;
		//loads bottom bun img adds it to list
		try {
			img = ImageIO.read(new File("SkyBurgerImages/bottomburger.png")).getScaledInstance(w, h,
					Image.SCALE_SMOOTH);
			burgerImgList.add(img);
		} catch (IOException e) {
			System.out.println("Image file not found");
		}

	}

	public void addTopping(Topping t) {
		//using this method you can add toppings to the burger
		burgerImgList.add(t.img);
		//if the burger goes higher than a third up the screen the burger shifts down by the height of the toppings
		if (y-p <= SkyBurger.HEIGHT/3) {
			y += toppingheight ;
			SkyBurger.imageY += toppingheight;
		}
	}

	// draws the image corresponding with this alien/player
	public void draw(Graphics g) {
		p = height / 4;
		//everything in burgerImgList
		for (int i = 0; i < burgerImgList.size(); i++) {
			g.drawImage(burgerImgList.get(i), x, y - p, null);
			p += toppingheight;

		}
	}

}
