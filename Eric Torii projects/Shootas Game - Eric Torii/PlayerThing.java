package Shootas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//this player thing contributes to both the player, and the gun of the player 
public class PlayerThing extends Rectangle {
	
	private Image imgRight, imgLeft;
	private String imgName;
	boolean gravityOn = true;
	public int speedx = 0, speedy = 0;
	public boolean lastFacedRight = true;
	Image pistolLeft, pistolRight, uziLeft, uziRight, sniperLeft, sniperRight, ARLeft, ARRight;
	//creates a new gun as a variable
	public Gun gun= new Pistol();
	//adds the gunImgName here as well so I can use it in the constructor
	private String gunImgName;
	
	// constructor takes a location, size and the name of the image file as parameters
	public PlayerThing(int x, int y, int w, int h, String imgName, String gunImgName, boolean startRight) {
		super(x, y, w, h);
		try {
			//two images, so there
			imgLeft = ImageIO.read(new File(imgName + "Left.png")).getScaledInstance(w, h, Image.SCALE_SMOOTH);
			imgRight = ImageIO.read(new File(imgName + "Right.png")).getScaledInstance(w, h, Image.SCALE_SMOOTH);
			//this keeps track of facing left or right to work on the png left and right
			lastFacedRight = startRight;
			//this moves the gunImg name to here, so can be use !null function in draw method
			this.gunImgName = gunImgName;

		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		//move image locally
		this.imgName = imgName;
	}
	
	// moves x by the inputted amount for player 1
	public void move() {
		x += speedx;
		y += speedy;
	}
	
	// draws the image corresponding with this player
	public void draw(Graphics g) {
		
		//depending on left or right, displays the the left and right images
		if (speedx < 0 || (speedx == 0 && lastFacedRight == false)) {
			
			g.drawImage(imgLeft, x, y, null);
		}
		else {
			g.drawImage(imgRight, x, y, null);
		}
		//draws bullets without guns on them, only players with guns
		if (gunImgName != null) {
			gun.draw(g, speedx, lastFacedRight, x, y);
		}
		
		if (speedx > 0) lastFacedRight = true;
		else if (speedx< 0) lastFacedRight = false;

	}
	public String getImgName() {
		return imgName;
	}
}
