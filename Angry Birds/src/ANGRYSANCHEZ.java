//ANGRY SANCHEZ - MADE BY ERIC TORII

//note: text location and size value exempt from magic numbers
//EXTRA HELP: 
	
	//Mr.Friedman template
	//Mr. Friedman help with PORTALS (working both)
	//Gage Grimes help: glitches with bottles getting stuck on target, and glitch where you can move bottle mid-launch

//KEY CONTROLS: drag the bottle with your mouse and it will launch in the opposite direction.
//				press R to reset level at anytime, press and hold SPACE for instructions
//EXTRA FEATURE: Trampolines & Portals

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class ANGRYSANCHEZ implements KeyListener{
	
	//CONTROL BOARD FOR STARTING BOTTLES (count) one time, for testing, TRAMPOLINE MULTIPLIER (multiplier)	
	int score = 0;
	int count = 5;	
	//count that is reset to
	int resetcount = 5;
	int trampMultiplyValue = 2;
	int portalMultiplyValue = 3;
	int totalMultiplierStart = 1;
	int binScore = 100;
	
	// the width/height of the window - feel free to change these
	final int screenWidth = 1395, screenHeight = 850;
	
	// the number of enemies in the game - feel free to change
	final int NUM_TARGETS = 6;
	
	// a constant for the gravitational pull on our 'bird' - again, feel free to change
	final double GRAVITY = .7;

	// hints...
	int[] targetX = {5, 650, 780, 20, 1200, 1270};
	int[] targetY = {330, 670, 660, 10, 160, 520};
	int[] targetSize = {80, 87, 95, 65, 107, 80};
	boolean[] showing = {true, true, true, true, true, true};
	
	int binNumber = 5;
	//starting multiplier for trampoline and portal (x1 to begin)
	int trampMultiplier = 1;
	int portalMultiplier = 1;
	int startingMultiplier = 1;
	
	//keep track if the bird is in the air and moving or not
	boolean launched = false; 
	Image backgroundImage;
	Image sanchezImage;
	Image sanchezFaceImage;
	Image bottleImage;
	Image binImage;
	Image trampolineImage;
	Image trampolineVertImage;
	Image portalImage;
	double xSpeed = 0; double ySpeed = 0;
	int mousePressX = 0; int mousePressY = 0;
	int mouseReleaseX = 0; int mouseReleaseY = 0;
	
	//for dividing the reverse to the pull force
	int mouseDivisor = 7;
	int bottleX = 170; int bottleY = 530; 
	int bottleXStart = 170; int bottleYStart = 530;
	int bottleWidth = 60; int bottleLength = 60; 
	int portal1X = 850; int portal1Y = -20;int portal1Width = 350; int portal1Length = 50;
	int portal2X = 1360; int portal2Y = 230; int portal2Width = 50; int portal2Length = 350;
	int tramp1X = -25; int tramp1Y = 400; int tramp1Width = 90; int tramp1Length = 300;
	int tramp2X = 330; int tramp2Y = 715; int tramp2Width = 290; int tramp2Length = 90;
	int tramp3X = 980; int tramp3Y = 590; int tramp3Width = 285; int tramp3Length = 80;
	int tramp4X = 810; int tramp4Y = 750; int tramp4Width = 200; int tramp4Length = 80;
	
	//location of score and count values
	int scoreX = 1150;
	int scoreY = 750;
	int countX = 1210;
	int countY = 810;
	
	//boolean to toggle on and off the rules menu with spacebar
	boolean showRules = false;
	
	//toggles for portals
	boolean portal1Switch = true;
	boolean portal2Switch = true;
	int endGameMessageSize = 50;
	int adjustmentForCountReset = 1;
	
	//tiers of score messages (each higher tier you get a better ending message, like "you suck" at 200 pts to "wow" at 2000 pts, for example
	int tier1Score = 800;
	int tier2Score = 1500;
	int tier3Score = 5000;
	int tier4Score = 10000;
	int tier5Score = 100000;
	
	// this method is for setting up any arrays that need filling in and loading images. 
	// This method will run once at the start of the game.
	public void setup() {

		// example of loading an image file - edit to suit your project
		try {
			
			backgroundImage = ImageIO.read(new File("ANGRY SANCHEZ BACKGROUND blur.jpg"));
			sanchezImage = ImageIO.read(new File("sanchez.png"));
			sanchezFaceImage = ImageIO.read(new File("sanchezface.png"));
			bottleImage = ImageIO.read(new File("bottle.png"));
			binImage = ImageIO.read(new File("small recycle bin.png"));
			trampolineImage = ImageIO.read(new File("trampoline.png"));
			trampolineVertImage = ImageIO.read(new File("trampoline vertical.png"));
			portalImage = ImageIO.read(new File("portal.png"));

		} 
		
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	//other visual elements (ridding code of magic numbers)
	
	//out zones (wider than actual screen)
	int leftOutZone = -80;
	int rightOutZone = 1600;
	int topOutZone = -200;
	int bottomOutzone = 870;
	int hitBoxAdjustmentGap50 = 50;
	int hitBoxAdjustmentGap10 = 10;
	int titleFontSize = 90;
	int endGameMessageX = 250;
	int endGameMessageY = 520;
	int endScreenFinalScoreTextX = 250;
	int endScreenFinalScoreTextY = 450;
	int multiplierX = 1170; int multiplierY = 130;
	int sanchezImageX = 150; int sanchezImageY = 540; int sanchezImageWidth = 150; int sanchezImageLength = 270; 
	int sanchezFaceImageX = 200; int sanchezFaceImageY = 572; int sanchezFaceImageWidth = 55; int sanchezFaceImageLength = 55; 
	int outerWhiteBoxX = 550; int outerWhiteBoxY = 200; int outerWhiteBoxWidth = 350; int outerWhiteBoxLength = 500;
	int blackFrameX = 570; int blackFrameY = 220; int blackFrameWidth = 310; int blackFrameLength = 460;
	int innerWhiteBoxX = 572; int innerWhiteBoxY = 222; int innerWhiteBoxWidth = 306; int innerWhiteBoxLength = 456;
	
	// move your 'bird' and apply any gravitational pull 
	public void moveBird() {
		// your code here
		
		if(count > 0) {
			
			//moves bird
			bottleX += xSpeed;
			bottleY += ySpeed;
			
			if (launched == true) {
				
				ySpeed += GRAVITY;
			}
			
			//bottle is reset when it leaves the screen
			if (bottleY >= bottomOutzone || bottleY <= topOutZone || bottleX <= leftOutZone || bottleX >= rightOutZone) {
				
				resetBottle();
				
			}
			
			//trampoline interactions
			
			//tramp 1
			if (bottleX >= tramp1X && bottleX <= tramp1X + tramp1Width - hitBoxAdjustmentGap50 && bottleY >= tramp1Y) {
				
				xSpeed = -xSpeed;
				
				trampMultiplier *= trampMultiplyValue;
				
			}
			
			//tramp 2
			if (bottleX >= tramp2X && bottleX <= tramp2X + tramp2Width && bottleY >= tramp2Y && bottleY <= tramp2Y + tramp2Length - hitBoxAdjustmentGap50) {
				
				ySpeed = -ySpeed;
				portal1Switch = true;
				portal2Switch = true;
				
				trampMultiplier *= trampMultiplyValue;
			}
			
			//tramp 2 debug - to avoid glitching inside hitbox
			if (bottleX >= tramp2X - hitBoxAdjustmentGap10 && bottleX <= tramp2X + tramp2Width && bottleY >= tramp2Y + hitBoxAdjustmentGap50/2) {
				
				xSpeed = -xSpeed;
				portal1Switch = true;
				portal2Switch = true;
				
			}
			
			//tramp 3
			if (bottleX >= tramp3X && bottleX <= tramp3X + tramp3Width && bottleY >= tramp3Y && bottleY <= tramp3Y + tramp3Length - hitBoxAdjustmentGap50) {
				
				ySpeed = -ySpeed;
				portal1Switch = true;
				portal2Switch = true;
				trampMultiplier *= trampMultiplyValue;
			}
			
			//tramp 4 (lowest trampoline)
			
			if (bottleX >= tramp4X && bottleX <= tramp4X + tramp4Width && bottleY >= tramp4Y && bottleY <= tramp4Y + tramp4Length - hitBoxAdjustmentGap50) {
				
				ySpeed = -ySpeed;
				portal1Switch = true;
				portal2Switch = true;
				trampMultiplier *= trampMultiplyValue;
			}
			
			
			
			//portal 1 (top portal)
			if (portal1Switch == true && bottleX >= portal1X && bottleX <= portal1X + portal1Width && bottleY <= portal1Y + portal1Length - hitBoxAdjustmentGap50) {
				
				bottleY = portal2Y + (bottleX - portal1X);
				bottleX = portal2X;
				double temp = xSpeed;
				xSpeed = ySpeed;
				ySpeed = temp/2;
				
				portalMultiplier *= portalMultiplyValue;
				
				portal1Switch = false;
				portal2Switch = false;
				
			}

			//portal 2 (side portal)
			if (portal2Switch == true && bottleX >= portal2X && bottleY >= portal2Y && bottleY <= portal2Y + portal2Length) {
				
				
				
				bottleX = portal1X + (bottleY - portal2Y);
				bottleY = portal1Y;
				double temp = xSpeed;
				xSpeed = -ySpeed;
				ySpeed = temp/2;
				
				portalMultiplier *= portalMultiplyValue;
				
				portal1Switch = false;
				portal2Switch = false;
				
			}			
		}				
	}
	
	// check for any collisions between your 'bird' and the targets.
	// if there is a collision, address it
	public void checkHits() {
		// your code here
		
		for (int i = 0; i <= binNumber; i ++) {
			
			//calculating when the distance between 2 objects is 0
			double dist = Math.sqrt(Math.pow(bottleX - targetX [i], 2) + Math.pow(bottleY - targetY [i], 2));
			
			if ((showing[i]) && (dist <= (bottleWidth/2) + targetSize[i]/2 && dist <= (bottleLength/2) + targetSize [i]/2)) {	
				
				showing [i] = false;
				score += binScore * trampMultiplier * portalMultiplier;
				
				resetBottle();
				
			}
		}
	}

	//moves bottle back to Sanchez hand
	public void resetBottle() {
		
		bottleX = bottleXStart;
		bottleY = bottleYStart;
		ySpeed = 0;
		xSpeed = 0;
		
		launched = false;
		
		portal1Switch = true;
		portal2Switch = true;
		
		count -= 1;
		trampMultiplier = startingMultiplier;
		portalMultiplier = startingMultiplier;
		
	}
	
	// what you want to happen at the moment when the mouse is first pressed down.
	public void mousePressed(int mouseX, int mouseY) {
		// your code here
			
		mousePressX = mouseX;
		mousePressY = mouseY;
			
	}
	
	// what you want to happen when the mouse button is released
	public void mouseReleased(int mouseX, int mouseY) {
		// your code here
		
		//need to save coordinate of original variable
		//calculate how far from int mouseX int mousY
		
		if (launched == false) {
			
			xSpeed = (mousePressX - mouseX)/mouseDivisor;
			ySpeed = (mousePressY - mouseY)/mouseDivisor;
			launched = true;
				
		}		
	}
	
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			showRules = true;	
		}
		
		if (e.getKeyChar() == 'r') {
			
			count = resetcount;
			score = 0;
			
			
			for (int i = 0; i <= binNumber; i ++) {
				
				showing [i] = true;	
				
			}
			
			resetBottle();
			count += adjustmentForCountReset;
					
		}	
		
	}
	
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			showRules = false;	
					
		}	
	}

	public void draw(Graphics g) {
		
		
	//NOTE: MAGIC NUMBERS FOR SIZE AND LOCATION OF TEXT IS ALLOWED BY MR.FRIEDMAN
		
		
	
		//background image
		g.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
		
		//title
		g.setColor(Color.red);
		g.setFont(new Font("Impact", Font.BOLD, titleFontSize));
		g.drawString("ANGRY SANCHEZ", 62, 141);
		
		g.setColor(Color.cyan);
		g.setFont(new Font("Impact", Font.BOLD, titleFontSize));
		g.drawString("ANGRY SANCHEZ", 68, 149);
		
		g.setColor(Color.black);
		g.setFont(new Font("Impact", Font.BOLD, titleFontSize));
		g.drawString("ANGRY SANCHEZ", 65, 145);
		
		g.setColor(Color.black);
		g.setFont(new Font("Impact", Font.BOLD, 40));
		g.drawString("GAME BY STUDIO TORII", 65, 225);
		g.setFont(new Font("comfortaa", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("HOLD SPACEBAR FOR RULES", 65, 260);
		
		//trampolines

		g.drawImage(trampolineVertImage, tramp1X, tramp1Y, tramp1Width, tramp1Length, null);
		g.drawImage(trampolineImage, tramp2X, tramp2Y, tramp2Width, tramp2Length, null);
		g.drawImage(trampolineImage, tramp3X, tramp3Y, tramp3Width, tramp3Length, null);
		g.drawImage(trampolineImage, tramp4X, tramp4Y, tramp4Width, tramp4Length, null);
		
		//points and bottles left text
		g.setColor(Color.white);
		g.setFont(new Font("Impact", Font.BOLD, 65));
		g.drawString("PTS:", 1000, 745);
		g.setFont(new Font("Impact", Font.BOLD, 40));
		g.drawString("Bottles Left:", 1000, 800);
		g.setFont(new Font("Impact", Font.BOLD, 20));
		
		//Mr.Sanchez body
		g.drawImage(sanchezImage, sanchezImageX, sanchezImageY, sanchezImageWidth, sanchezImageLength, null);
		
		//Sanchez face
		g.drawImage(sanchezFaceImage, sanchezFaceImageX, sanchezFaceImageY, sanchezFaceImageWidth, sanchezFaceImageLength, null);
		
		//water bottle
		g.drawImage(bottleImage, bottleX, bottleY, bottleWidth, bottleLength, null);

		
		//recycling bins (under if statement to turn them off(
		for (int i = 0; i <= binNumber; i ++) {
			
			if (showing [i] == true){
				
				g.drawImage(binImage, targetX [i], targetY [i], targetSize [i], targetSize [i], null);
			}
			
		}
		
		//portal
		g.drawImage(portalImage, portal1X, portal1Y, portal1Width, portal1Length, null);
		g.drawImage(portalImage, portal2X, portal2Y, portal2Width, portal2Length, null);
		
		//SCORE
		g.setFont(new Font("Impact", Font.BOLD, 80));
		g.setColor(Color.white);
		g.drawString(String.valueOf(score), scoreX, scoreY);

		//BOTTLES LEFT
		g.setFont(new Font("Impact", Font.BOLD, 60));
		g.setColor(Color.white);
		g.drawString(String.valueOf(count), countX, countY);
		
		//shows total multiplier
		g.setFont(new Font("Comfortaa", Font.BOLD, 130));
		g.setColor(Color.white);
		g.drawString(String.valueOf("x" + (totalMultiplierStart * portalMultiplier * trampMultiplier)), multiplierX, multiplierY);
		
		//rules menu
		if(showRules) {
			
			//draw white box
			g.setColor(Color.white);
			g.fillRect(outerWhiteBoxX, outerWhiteBoxY, outerWhiteBoxWidth, outerWhiteBoxLength);		
			g.setColor(Color.black);
			g.fillRect(blackFrameX, blackFrameY, blackFrameWidth, blackFrameLength);
			g.setColor(Color.white);
			g.fillRect(innerWhiteBoxX, innerWhiteBoxY, innerWhiteBoxWidth, innerWhiteBoxLength);
			
			//text within white box
			g.setFont(new Font("IMPACT", Font.BOLD, 60));
			g.setColor(Color.black);
			g.drawString("---RULES---", 605, 290);
			g.setFont(new Font("IMPACT", Font.BOLD, 30));
			g.setColor(Color.black);
			g.drawString("1. Hit the recycling", 605, 350);
			g.setFont(new Font("IMPACT", Font.BOLD, 30));
			g.setColor(Color.black);
			g.drawString("bins for 100 pts", 630, 390);
			g.setFont(new Font("IMPACT", Font.BOLD, 30));
			g.setColor(Color.black);
			g.drawString("2. Trampoline = x2 pts", 605, 430);
			g.setFont(new Font("IMPACT", Font.BOLD, 30));
			g.setColor(Color.black);
			g.drawString("3. Portal = x3 pts", 605, 480);
			g.setFont(new Font("IMPACT", Font.BOLD, 30));
			g.setColor(Color.blue);
			g.drawString("Press R to restart", 605, 650);
			
		}
		
		//display the game score at the end of the game + fun message
		if(count <= 0) {
			
			g.setFont(new Font("Comfortaa", Font.BOLD, 80));
			g.setColor(Color.white);
			g.drawString("FINAL SCORE:", endScreenFinalScoreTextX, endScreenFinalScoreTextY);
			g.setFont(new Font("Comfortaa", Font.BOLD, 150));
			g.setColor(Color.white);
			g.drawString(String.valueOf(score), 850, 450);
			g.setFont(new Font("Comfortaa", Font.BOLD, 30));
			g.setColor(Color.white);
			g.drawString("Press R to restart", 250, 570);
			
			//below code to tell player how well they did when game ends
			if (score <= tier1Score) {
				
				g.setFont(new Font("Comfortaa", Font.BOLD, endGameMessageSize));
				g.setColor(Color.white);
				g.drawString("Yeah sorry, you suck at this.", endGameMessageX, endGameMessageY);	
				
			}
			
			if (score > tier1Score && score <= tier2Score) {
				
				g.setFont(new Font("Comfortaa", Font.BOLD, endGameMessageSize));
				g.setColor(Color.white);
				g.drawString("Not bad.", endGameMessageX, endGameMessageY);	
				
			}
			
			if (score > tier2Score && score <= tier3Score) {
				
				g.setFont(new Font("Comfortaa", Font.BOLD, endGameMessageSize));
				g.setColor(Color.white);
				g.drawString("That's pretty impressive.", endGameMessageX, endGameMessageY);	
				
			}
			
			if (score > tier3Score && score <= tier4Score) {
				
				g.setFont(new Font("Comfortaa", Font.BOLD, endGameMessageSize));
				g.setColor(Color.white);
				g.drawString("You're cracked!", endGameMessageX, endGameMessageY);	
				
			}
			
			if (score > tier4Score && score <= tier5Score) {
				
				g.setFont(new Font("Comfortaa", Font.BOLD, endGameMessageSize));
				g.setColor(Color.white);
				g.drawString("ABSOLUTELY INSANE", endGameMessageX, endGameMessageY);	
				
			}
			
			if (score > tier5Score) {
				
				g.setFont(new Font("Comfortaa", Font.BOLD, endGameMessageSize));
				g.setColor(Color.white);
				g.drawString("That was not a bug, that was a feature.", endGameMessageX, endGameMessageY);	
				
			}
			
		}	
	}
	
	
	
	
	// ************** DON'T TOUCH THE BELOW CODE ********************** //
	
	public ANGRYSANCHEZ() {
		setup();
		
		JFrame frame = new JFrame();
		frame.setSize(screenWidth,screenHeight);
		JPanel canvas = new JPanel() {
			public void paint(Graphics g) {
				draw(g);
			}
		};
		canvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				ANGRYSANCHEZ.this.mousePressed(e.getX(),e.getY());	
			}
			public void mouseReleased(MouseEvent e) {
				ANGRYSANCHEZ.this.mouseReleased(e.getX(),e.getY());
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		//this is to add the keylistner, to activate R and spacebar
		frame.addKeyListener(this);
		
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while (true) {
			moveBird();
			checkHits();
			canvas.repaint();
			
			try {Thread.sleep(20);} 
			catch (InterruptedException e) {}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		
		new ANGRYSANCHEZ();
	
	}
}


