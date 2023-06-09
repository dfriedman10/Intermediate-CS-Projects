package Shootas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class ShootasLaser extends PlayerThing {
	
	// constructor takes a location, size and the name of the image file as parameters
	public ShootasLaser(int x, int y, int w, int h, String imgName) {
		super(x, y, w, h, imgName, null, true);
	}
	//speed the bullet will move at
	public void moveX(int dX) {
		x += dX;
	}
	
}