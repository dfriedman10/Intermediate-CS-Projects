package Shootas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//box for picking up to get powerups
public class Box extends Rectangle {
	Image box;
	//variables, width and height, x and y coord for box
	public Box(int x, int y, int w, int h) {
		super(x, y, w, h);
		// TODO Auto-generated constructor stub
		try {
			box = ImageIO.read(new File("ShootasImages/boxpic.png")).getScaledInstance(w, h, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			System.out.println("Image file not found");
		}
	}
	//draws the box
	public void draw(Graphics g) {
		g.drawImage(box, x, y, null);
	}
}
