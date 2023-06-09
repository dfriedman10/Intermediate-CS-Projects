package Shootas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Platform extends Rectangle {
	//these are the variables that are used in ShootasLaser
	public Platform(int x, int y, int w, int h) {
		super(x, y, w, h);
		// TODO Auto-generated constructor stub
	}
	//draws the graphic for the platform
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
	   	g.fillRect(x, y, width, height);
	}
	//speed the wall will move at
	public void moveX(double wallMoveSpeed) {
		x += wallMoveSpeed;
	}
}
