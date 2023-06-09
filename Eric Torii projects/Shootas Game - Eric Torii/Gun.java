package Shootas;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class Gun{

	int speed, ammo, damage;
	Image img;
	private Image imgRight, imgLeft;
	
	public Gun(int damage, int ammo, int speed, String imgName, int width, int height, String gunImgName) {
		this.speed = speed;
		this.ammo = ammo; 
		this.damage = damage;
		
		try {
			if (gunImgName!=null) {
				imgLeft = ImageIO.read(new File(imgName + "Left.png")).getScaledInstance(width, height, Image.SCALE_SMOOTH);
				imgRight = ImageIO.read(new File(imgName +"Right.png")).getScaledInstance(width, height, Image.SCALE_SMOOTH);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public abstract void reload();
	
	public void draw(Graphics g, int speedx, boolean lastFacedRight, int x, int y) {
		if (speedx < 0 || (speedx == 0 && lastFacedRight == false)) {
			g.drawImage(imgLeft, x - (90/3), y + (3*90/5), null);
		}
		if (speedx > 0 || (speedx == 0 && lastFacedRight == true)) {
			g.drawImage(imgRight, x + (4*90/6), y + (3*90/5), null);
		}
		
	}
}
